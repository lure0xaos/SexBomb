package gargoyle.sexbomb.game.event

import java.io.Serializable
import java.util.*

class GameEventSource : Serializable {

    private val listeners: MutableList<GameListener> = Collections.synchronizedList(mutableListOf())
    fun addGameListener(listener: GameListener) {
        listeners.add(listener)
    }

    fun fireGameEvent(event: GameEvent) {
        for (listener in listeners) {
            listener.onGameEvent(event)
        }
    }

    fun removeGameListener(listener: GameListener) {
        listeners.remove(listener)
    }

}
