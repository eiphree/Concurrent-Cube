package concurrentcube;

public class RotaterLeft extends Rotater{
    private final Cube cube;
    public RotaterLeft (Cube cube) {
        this.cube = cube;
    }

    @Override
    public void rotate(int size, int layer) {
        CubeSide top = cube.getTop();
        CubeSide back = cube.getBack();
        CubeSide bottom = cube.getBottom();
        CubeSide front = cube.getFront();
        int[] temp = top.getColumn(layer);
        top.changeColumn(layer, back.getColumn(size - 1 - layer));
        back.changeColumn(size - 1 - layer, bottom.getColumn(layer));
        bottom.changeColumn(layer, front.getColumn(layer));
        front.changeColumn(layer, temp);
        if (layer == 0)
            cube.getLeft().rotateClockwise();
        if (layer == size - 1)
            cube.getRight().rotateCounterclockwise();
    }
}
/*
        CubeSide front = cube.getFront();
        CubeSide right = cube.getRight();
        CubeSide back = cube.getBack();
        CubeSide left = cube.getLeft();
        int [] temp = front.getRow(layer);
        front.changeRow(layer, right.getRow(layer));
        right.changeRow(layer, back.getRow(layer));
        back.changeRow(layer, left.getRow(layer));
        left.changeRow(layer, temp);
 */