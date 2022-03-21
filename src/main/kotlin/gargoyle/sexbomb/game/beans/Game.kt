package gargoyle.sexbomb.game.beans

import gargoyle.sexbomb.game.event.GameEventSource
import gargoyle.sexbomb.game.event.StatusChangedGameEvent
import java.awt.Image
import java.io.Serializable
import javax.swing.ImageIcon

class Game : Serializable {
    val source: GameEventSource = GameEventSource()
    lateinit var campaign: Campaign
        private set

    lateinit var cover: Image
        private set
    lateinit var field: Field
        private set

    fun fieldInitialized() = ::field.isInitialized

    var image: Image? = null
        private set
    var level: Level? = null
        private set
    var isResize = false
    lateinit var skin: Skin
    var status: Status = Status.NULL
        private set

    fun skinInitialized() = ::skin.isInitialized

    val statusString: String
        get() = String.format(
            STR_STATUS__S_S_D,
            campaign.name,
            level?.name,
            this.field.left - this.field.mines
        )
    val isGame: Boolean
        get() = status == Status.GAME
    val isLost: Boolean
        get() = status == Status.LOST
    val isWonGame: Boolean
        get() = status == Status.WON_GAME
    val isWonLevel: Boolean
        get() = status == Status.WON_LEVEL

    fun lost() {
        status = Status.LOST
        source.fireGameEvent(StatusChangedGameEvent(this, Status.LOST))
    }

    operator fun next(): Boolean {
        level = campaign.getNextLevel(level)
        if (level == null) {
            image = null
            status = Status.WON_GAME
            source.fireGameEvent(StatusChangedGameEvent(this, Status.WON_GAME))
            return false
        }
        field.init(level!!.width, level!!.height, level!!.mines)
        if (level!!.image != null) {
            image = ImageIcon(level!!.image).image
        }
        if (level!!.cover != null) {
            cover = ImageIcon(level!!.cover).image
        }
        status = Status.GAME
        source.fireGameEvent(StatusChangedGameEvent(this, Status.GAME))
        return true
    }

    fun reset() {
        status = Status.NULL
    }

    fun start() {
        if (!::field.isInitialized) {
            field = Field()
        }
        level = null
        next()
    }

    fun start(campaign: Campaign) {
        this.campaign = campaign
        start()
    }

    fun wonGame() {
        status = Status.WON_GAME
        source.fireGameEvent(StatusChangedGameEvent(this, Status.WON_GAME))
    }

    fun wonLevel() {
        status = Status.WON_LEVEL
        source.fireGameEvent(StatusChangedGameEvent(this, Status.WON_LEVEL))
    }

    companion object {
        private const val STR_STATUS__S_S_D = "%s | %s | Left: %d"
    }
}
