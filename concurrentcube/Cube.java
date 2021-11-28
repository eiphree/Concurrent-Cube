package concurrentcube;

import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;

public class Cube {
    public static final int T = 0; //top
    public static final int L = 1; //left
    public static final int F = 2; //front
    public static final int R = 3; //right
    public static final int B = 4; //back
    public static final int D = 5; //down (bottom)

    private final int size;
    private final CubeSide[] sides;
    private final BiConsumer<Integer, Integer> beforeRotation;
    private final BiConsumer<Integer, Integer> afterRotation;
    private final Runnable beforeShowing;
    private final Runnable afterShowing;

    private Semaphore mutex = new Semaphore(1);
    private Semaphore showing = new Semaphore(0);
    private Semaphore[] planeSemaphore = new Semaphore[3];
    private Semaphore[] layerSemaphore;
    private int whoseTurn = 0; //0 - top/bootom | 1 - left/right | 2 - front/back
    private int waitingShows = 0;
    private int[] waitingRotations = {0, 0, 0};
    private int[] waitingOnLayer;
    private int[] performingOnLayer;
    private int performingShows = 0;
    private int[] performingRotations = {0, 0, 0};


    public Cube(int size,
                BiConsumer<Integer, Integer> beforeRotation,
                BiConsumer<Integer, Integer> afterRotation,
                Runnable beforeShowing,
                Runnable afterShowing
    ) {
        this.size = size;
        this.beforeRotation = beforeRotation;
        this.afterRotation = afterRotation;
        this.beforeShowing = beforeShowing;
        this.afterShowing = afterShowing;

        sides = new CubeSide[6];
        for (int i = 0; i < 6; i++)
            sides[i] = new CubeSide(size, i);

        layerSemaphore = new Semaphore[size];
        for (int i = 0; i < size; i++)
            layerSemaphore[i] = new Semaphore(0);

        waitingOnLayer = new int[size];
        performingOnLayer = new int[size];
        for (int i = 0; i < size; i++) {
            waitingOnLayer[i] = 0;
            performingOnLayer[i] = 0;
        }

        for (int i = 0; i < 3; i++)
            planeSemaphore[i] = new Semaphore(0);

    }

    public CubeSide getTop() {
        return sides[T];
    }

    public CubeSide getFront() {
        return sides[F];
    }

    public CubeSide getLeft() {
        return sides[L];
    }

    public CubeSide getRight() {
        return sides[R];
    }

    public CubeSide getBottom() {
        return sides[D];
    }

    public CubeSide getBack() {
        return sides[B];
    }

    int getPlane(int side) {
        switch (side) {
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 0;
            default:
                return side;
        }
    }

    private int standardizedLayer(int side, int layer) {
        if (side >= 3)
            return this.size - 1 - layer;
        return layer;
    }

    public void rotate(int side, int layer) throws InterruptedException {
        int plane = getPlane(side);
        mutex.acquire();
        if (waitingRotationsSum() + performingRotationsSum() == 0) {
            whoseTurn = plane;  //executing if no other rotates are waiting nor performing
        }

        if (whoseTurn != plane || waitingShows + performingShows > 0
                || performingRotationsSum() - performingRotations[plane] > 0) {
            waitingRotations[plane]++;
            mutex.release();
            planeSemaphore[plane].acquire();        //mutex inherited
            waitingRotations[plane]--;
        }

        performingRotations[plane]++;

        if (waitingRotations[plane] > 0) {
            planeSemaphore[plane].release();
        } else {
            mutex.release();
        }

        int standLayer = standardizedLayer(side, layer);
        mutex.acquire();
        if (waitingOnLayer[standLayer] + performingOnLayer[standLayer] > 0) {
            waitingOnLayer[standLayer]++;
            mutex.release();
            layerSemaphore[standLayer].acquire();       //mutex inherited
            waitingOnLayer[standLayer]--;
        }

        performingOnLayer[standLayer]++;
        mutex.release();

        beforeRotation.accept(side, layer);
        Rotater r;
        switch (side) {
            case 0:
                r = new RotaterUp(this);
                break;
            case 1:
                r = new RotaterLeft(this);
                break;
            case 2:
                r = new RotaterFront(this);
                break;
            case 3:
                r = new RotaterRight(this);
                break;
            case 4:
                r = new RotaterBack(this);
                break;
            case 5:
                r = new RotaterBottom(this);
                break;
            default:
                r = null;
        }
        r.rotate(this.size, layer);
        afterRotation.accept(side, layer);

        mutex.acquireUninterruptibly();
        performingRotations[plane]--;
        performingOnLayer[standLayer]--;

        if (waitingOnLayer[standLayer] > 0) {
            layerSemaphore[standLayer].release();
        } else {
            mutex.release();
        }
        mutex.acquireUninterruptibly();
        if (performingRotationsSum() == 0 && waitingShows > 0) {
            whoseTurn = (whoseTurn + 1) % 3;
            showing.release();
        }
        else {
            if (whoseTurn == plane) {
                if (waitingRotations[(whoseTurn + 1) % 3] > 0) {
                    whoseTurn = (whoseTurn + 1) % 3;
                } else if (waitingRotations[(whoseTurn + 2) % 3] > 0) {
                    whoseTurn = (whoseTurn + 2) % 3;
                }
            }
            if (performingRotationsSum() == 0 && waitingRotations[whoseTurn] > 0)
                planeSemaphore[whoseTurn].release();
            else
                mutex.release();

        }
        //  }
    }

    private int performingRotationsSum() {
        return performingRotations[0] + performingRotations[1] + performingRotations[2];
    }

    private int waitingRotationsSum() {
        return waitingRotations[0] + waitingRotations[1] + waitingRotations[2];
    }

    public String show() throws InterruptedException {
        mutex.acquire();
        if (performingRotationsSum() + waitingRotationsSum() > 0) {
            waitingShows++;
            mutex.release();
            showing.acquire();          //mutex inherited
            waitingShows--;
        }
        performingShows++;
        if (waitingShows > 0) {
            showing.release();
        } else {
            mutex.release();
        }

        beforeShowing.run();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            s.append(sides[i].showside());
        }
        afterShowing.run();

        mutex.acquireUninterruptibly();
        performingShows--;
        if (performingShows == 0) {
            if (waitingRotations[whoseTurn] > 0) {
                planeSemaphore[whoseTurn].release();
            } else if (waitingRotations[(whoseTurn + 1) % 3] > 0) {
                whoseTurn = (whoseTurn + 1) % 3;
                planeSemaphore[whoseTurn].release();
            } else if (waitingRotations[(whoseTurn + 2) % 3] > 0) {
                whoseTurn = (whoseTurn + 2) % 3;
                planeSemaphore[whoseTurn].release();
            }
        }
        else if (waitingShows > 0) {
            showing.release();
        }
        else {
            mutex.release();
        }
        return s.toString();
    }

    public void print() {
        System.out.println("Top:");
        getTop().printSide();
        System.out.println("Left:");
        getLeft().printSide();
        System.out.println("Front:");
        getFront().printSide();
        System.out.println("Right:");
        getRight().printSide();
        System.out.println("Back:");
        getBack().printSide();
        System.out.println("Bottom:");
        getBottom().printSide();
    }

    public int countColor(int color) {
        int r = 0;
        for (int i = 0; i < 6; i++) {
            r += sides[i].countColor(color);
        }
        return r;
    }

}