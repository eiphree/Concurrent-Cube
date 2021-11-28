package concurrentcube;

public class RotaterBottom extends Rotater{
    private final Cube cube;
    public RotaterBottom (Cube cube) {
        this.cube = cube;
    }

    @Override
    public void rotate(int size, int layer) {
        CubeSide front = cube.getFront();
        CubeSide right = cube.getRight();
        CubeSide back = cube.getBack();
        CubeSide left = cube.getLeft();
        int[] temp = front.getRow(size - 1 - layer);
        front.changeRow(size - 1 - layer, left.getRow(size - 1 - layer));
        left.changeRow(size - 1 - layer, back.getRow(size - 1 - layer));
        back.changeRow(size - 1 - layer, right.getRow(size - 1 - layer));
        right.changeRow(size - 1 - layer, temp);
        if (layer == 0)
            cube.getBottom().rotateClockwise();
        if (layer == size - 1)
            cube.getTop().rotateCounterclockwise();
    }
}