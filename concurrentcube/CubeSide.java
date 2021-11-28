package concurrentcube;

public class CubeSide {

    private final int size;
    private final int side;
    private int[][] colors;

    public CubeSide (int size, int side) {
        this.size = size;
        this.side = side;
        colors = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                colors[i][j] = side;
        }
    }

    public int[] getRow (int number) {
        int[] row = new int[size];
        System.arraycopy(colors[number], 0, row, 0, size);
        return row;
    }

    public int[] getColumn (int number) {
        int[] column = new int[size];
        for (int i = 0; i < size; i++) {
            column[i] = colors[i][number];
        }
        return column;
    }

    public void changeRow (int number, int[] newRow) { //TODO exeption if wrong size
        if (size >= 0)
            System.arraycopy(newRow, 0, colors[number], 0, size);
    }

    public void changeColumn (int number, int[] newColumn) {
        for (int i = 0; i < size; i++) {
            colors[i][number] = newColumn[i];
        }
    }

    public void rotateClockwise() {
        // Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
        int[][] colorsCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(colors[i], 0, colorsCopy[i], 0, size);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                colors[j][size - 1 - i] = colorsCopy[i][j];
             //   colors[i][j] = colorsCopy[j][size - 1 - i];
            }
        }
    }

    public void rotateCounterclockwise() {
        int[][] colorsCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(colors[i], 0, colorsCopy[i], 0, size);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                colors[i][j] = colorsCopy[j][size - 1 - i];
            }
        }
    }

    public String showside() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(colors[i][j]);
            }
        }
        return s.toString();
    }
    public void printSide() {
        for (int i = 0; i < size; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < size; j++) {
                s.append(colors[i][j]);
            }
            System.out.println(s);
        }
    }

    public int countColor(int color) {
        int r = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (colors[i][j] == color)
                    r++;
            }
        }
        return r;
    }

}
