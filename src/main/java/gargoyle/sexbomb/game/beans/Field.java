package gargoyle.sexbomb.game.beans;

import java.io.Serializable;
import java.util.Random;

public class Field implements Serializable {
    private static final Random random = new Random();
    private Cell[][] cells;
    private int flag;
    private int height;
    private int left;
    private int mines;
    private int minesLeft;
    private int open;
    private int width;

    private static int rnd(int b) {
        return random.nextInt(b);
    }

    private int count(int x, int y) {
        int c = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int xx = x + dx;
                int yy = y + dy;
                if (xx >= 0 && yy >= 0 && xx < width && yy < height && cells[xx][yy].isMine()) {
                    c++;
                }
            }
        }
        return c;
    }

    public void flag(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        Cell cell = cells[x][y];
        if (cell.isOpen()) {
            return;
        }
        Flag flag = cell.getFlag();
        if (flag == null) {
            cell.setFlag(Flag.FLAG);
            this.flag++;
            minesLeft--;
        }
        if (flag == Flag.FLAG) {
            cell.setFlag(Flag.QUESTION);
        }
        if (flag == Flag.QUESTION) {
            cell.setFlag(null);
            this.flag--;
            minesLeft++;
        }
    }

    public Cell getCellAt(int x, int y) {
        return x < 0 || y < 0 || x >= width || y >= height ? null : cells[x][y];
    }

    public int getFlag() {
        return flag;
    }

    public int getHeight() {
        return height;
    }

    public int getLeft() {
        return left;
    }

    public int getMines() {
        return mines;
    }

    public int getMinesLeft() {
        return minesLeft;
    }

    public int getOpen() {
        return open;
    }

    public int getWidth() {
        return width;
    }

    public void init() {
        init(width, height, mines);
    }

    private void init(int mines) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = cells[x][y];
                cell.reset();
            }
        }
        for (int b = 0; b < mines; b++) {
            int x;
            int y;
            do {
                x = rnd(width);
                y = rnd(height);
            } while (cells[x][y].isMine());
            cells[x][y].setMine(true);
        }
        open = 0;
        flag = 0;
        this.mines = mines;
        minesLeft = mines;
        left = width * height;
    }

    private void init(int width, int height) {
        if (cells == null || this.width != width || this.height != height) {
            cells = new Cell[this.width = width][this.height = height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    cells[x][y] = new Cell();
                }
            }
        } else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Cell cell = cells[x][y];
                    cell.reset();
                }
            }
        }
        open = 0;
        flag = 0;
        mines = 0;
        minesLeft = 0;
        left = width * height;
    }

    public void init(int width, int height, int mines) {
        init(width, height);
        init(mines);
    }

    public boolean isOpen() {
        return left == mines;
    }

    public void open(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        Cell cell = cells[x][y];
        if (cell.isOpen()) {
            return;
        }
        cell.setCount(count(x, y));
        cell.setFlag(null);
        cell.setOpen(true);
        open++;
        left--;
        if (cell.isMine()) {
            return;
        }
        if (cell.getCount() == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {
                        open(x + dx, y + dy);
                    }
                }
            }
        }
    }
}
