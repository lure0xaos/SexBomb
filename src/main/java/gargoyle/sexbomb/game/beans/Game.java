package gargoyle.sexbomb.game.beans;

import gargoyle.sexbomb.game.event.GameEventSource;
import gargoyle.sexbomb.game.event.StatusChangedGameEvent;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.Serializable;

public class Game implements Serializable {
    private static final String STR_STATUS__S_S_D = "%s | %s | Left: %d";
    public final GameEventSource source;
    private Campaign campaign;
    private transient Image cover;
    private Field field;
    private transient Image image;
    private Level level;
    private boolean resize;
    private Skin skin;
    private Status status;

    public Game() {
        status = null;
        field = null;
        image = null;
        cover = null;
        level = null;
        skin = null;
        campaign = null;
        source = new GameEventSource();
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public Image getCover() {
        return cover;
    }

    public Field getField() {
        return field;
    }

    public Image getImage() {
        return image;
    }

    public Level getLevel() {
        return level;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusString() {
        return String.format(STR_STATUS__S_S_D, campaign.getName(), level.getName(), field.getLeft() - field.getMines());
    }

    public boolean isGame() {
        return field != null && status == Status.GAME;
    }

    public boolean isLost() {
        return field != null && status == Status.LOST;
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isWonGame() {
        return field != null && status == Status.WON_GAME;
    }

    public boolean isWonLevel() {
        return field != null && status == Status.WON_LEVEL;
    }

    public void lost() {
        status = Status.LOST;
        source.fireGameEvent(new StatusChangedGameEvent(this, Status.LOST));
    }

    public boolean next() {
        level = campaign.getNextLevel(level);
        if (level == null) {
            image = null;
            status = Status.WON_GAME;
            source.fireGameEvent(new StatusChangedGameEvent(this, Status.WON_GAME));
            return false;
        }
        field.init(level.getWidth(), level.getHeight(), level.getMines());
        if (level.getImage() != null) {
            image = new ImageIcon(level.getImage()).getImage();
        }
        if (level.getCover() != null) {
            cover = new ImageIcon(level.getCover()).getImage();
        }
        status = Status.GAME;
        source.fireGameEvent(new StatusChangedGameEvent(this, Status.GAME));
        return true;
    }

    public void reset() {
        status = null;
    }

    public void start() {
        if (field == null) {
            field = new Field();
        }
        level = null;
        next();
    }

    public void start(Campaign campaign) {
        this.campaign = campaign;
        start();
    }

    public void wonGame() {
        status = Status.WON_GAME;
        source.fireGameEvent(new StatusChangedGameEvent(this, Status.WON_GAME));
    }

    public void wonLevel() {
        status = Status.WON_LEVEL;
        source.fireGameEvent(new StatusChangedGameEvent(this, Status.WON_LEVEL));
    }
}
