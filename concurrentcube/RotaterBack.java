package concurrentcube;

public class RotaterBack extends Rotater {
    private final Cube cube;
    public RotaterBack (Cube cube) {
        this.cube = cube;
    }

    @Override
    public void rotate(int size, int layer) {
        CubeSide top = cube.getTop();
        CubeSide left = cube.getLeft();
        CubeSide bottom = cube.getBottom();
        CubeSide right = cube.getRight();
        int[] temp = top.getRow(layer);
        top.changeRow(layer, right.getColumn(size - 1 - layer));
        right.changeColumn(size - 1 - layer, bottom.getRow(size - 1 - layer));
        bottom.changeRow(size - 1 - layer, left.getColumn(layer));
        left.changeColumn(layer, temp);
        if (layer == 0)
            cube.getBack().rotateClockwise();
        if (layer == size - 1)
            cube.getFront().rotateCounterclockwise();
    }
}
