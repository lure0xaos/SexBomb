package gargoyle.sexbomb.game.event;

import gargoyle.sexbomb.game.beans.Game;
import gargoyle.sexbomb.game.beans.Status;

public class StatusChangedGameEvent extends GameEvent {
    private final Status status;

    public StatusChangedGameEvent(Game game, Status status) {
        super(game);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
