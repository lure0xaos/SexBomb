package gargoyle.sexbomb.game.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameEventSource implements Serializable {
    private static final long serialVersionUID = 4900683671382095005L;
    private final transient List<GameListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }

    public void fireGameEvent(GameEvent event) {
        for (GameListener listener : listeners) {
            listener.onGameEvent(event);
        }
    }

    public void removeGameListener(GameListener listener) {
        listeners.remove(listener);
    }
}
