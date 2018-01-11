package gargoyle.sexbomb.game.beans;

import java.io.Serializable;

public class Cell implements Serializable {
    private int count;
    private Flag flag;
    private boolean mine;
    private boolean open;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void reset() {
        this.count = 0;
        this.flag = null;
        this.mine = false;
        this.open = false;
    }
}
