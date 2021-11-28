package concurrentcube;

public abstract class Rotater {
    private Cube cube;
    public abstract void rotate(int size, int layer);
 /*   public Rotater (int side, Cube cube) { TODO
        Rotater r;
        switch (side) {
            case 0:
                r = new RotaterUp(cube);
                break;
            case 1:
                r = new RotaterLeft(cube);
                break;
            case 2:
                r = new RotaterFront(cube);
                break;
            case 3:
                r = new RotaterRight(cube);
                break;
            case 4:
                r = new RotaterBack(cube);
                break;
            case 5:
                r = new RotaterBottom(cube);
                break;
            default:
                r = null;
        }
    }*/
}
