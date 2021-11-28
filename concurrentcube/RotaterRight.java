package concurrentcube;

public class RotaterRight extends Rotater{
    private final Cube cube;
    public RotaterRight (Cube cube) {
        this.cube = cube;
    }

    @Override
    public void rotate(int size, int layer) {
        CubeSide top = cube.getTop();
        CubeSide back = cube.getBack();
        CubeSide bottom = cube.getBottom();
        CubeSide front = cube.getFront();
        int[] temp = top.getColumn(size - 1 - layer);
        top.changeColumn(size - 1 - layer, front.getColumn(size - 1 - layer));
        front.changeColumn(size - 1 - layer, bottom.getColumn(size - 1 - layer));
        bottom.changeColumn(size - 1 - layer, back.getColumn(layer));
        back.changeColumn(layer, temp);
        if (layer == 0)
            cube.getRight().rotateClockwise();
        if (layer == size - 1)
            cube.getLeft().rotateCounterclockwise();
    }
}
