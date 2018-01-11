package gargoyle.sexbomb.game.event;

import gargoyle.sexbomb.game.beans.Game;

public abstract class GameEvent {
    private final Game game;

    protected GameEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
