package concurrentcube;

public class RotaterUp extends Rotater {
    private final Cube cube;
    public RotaterUp (Cube cube) {
        this.cube = cube;
    }

    @Override
    public void rotate (int size, int layer) {
        CubeSide front = cube.getFront();
        CubeSide right = cube.getRight();
        CubeSide back = cube.getBack();
        CubeSide left = cube.getLeft();
        int[] temp = front.getRow(layer);
        front.changeRow(layer, right.getRow(layer));
        right.changeRow(layer, back.getRow(layer));
        back.changeRow(layer, left.getRow(layer));
        left.changeRow(layer, temp);
        if (layer == 0)
            cube.getTop().rotateClockwise();
        if (layer == size - 1)
            cube.getBottom().rotateCounterclockwise();
    }
}
