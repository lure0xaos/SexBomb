package gargoyle.sexbomb.game.event;

@FunctionalInterface
public interface GameListener {
    void onGameEvent(GameEvent event);
}
