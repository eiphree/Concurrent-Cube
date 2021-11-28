package concurrentcube;

public class RotaterFront extends Rotater{
    private final Cube cube;
    public RotaterFront (Cube cube) {
        this.cube = cube;
    }

    @Override
    public void rotate(int size, int layer) {
        CubeSide top = cube.getTop();
        CubeSide right = cube.getRight();
        CubeSide bottom = cube.getBottom();
        CubeSide left = cube.getLeft();
        int[] temp = top.getRow(size - 1 - layer);
        top.changeRow(size - 1 - layer, left.getColumn(size - 1 - layer));
        left.changeColumn(size - 1 - layer, bottom.getRow(layer));
        bottom.changeRow(layer, right.getColumn(layer));
        right.changeColumn(layer, temp);
        if (layer == 0)
            cube.getFront().rotateClockwise();
        if (layer == size - 1)
            cube.getBack().rotateCounterclockwise();
    }
}
