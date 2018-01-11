package gargoyle.sexbomb.game.event

import gargoyle.sexbomb.game.beans.Game
import gargoyle.sexbomb.game.beans.Status

class StatusChangedGameEvent(game: Game, val status: Status) : GameEvent(game)
