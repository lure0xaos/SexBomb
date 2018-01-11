package gargoyle.sexbomb.game.beans

import gargoyle.sexbomb.game.event.GameEventSource
import gargoyle.sexbomb.game.event.StatusChangedGameEvent
import gargoyle.sexbomb.services.CampaignInfo
import gargoyle.sexbomb.services.LevelInfo
import gargoyle.sexbomb.util.applet.Applet
import java.awt.Image
import java.io.Serializable

class Game(private val applet: Applet) : Serializable {
    val source: GameEventSource = GameEventSource()
    lateinit var campaign: CampaignInfo
        private set

    var cover: Image? = null
        private set
    lateinit var field: Field
        private set

    fun fieldInitialized(): Boolean = ::field.isInitialized

    var image: Image? = null
        private set
    var level: LevelInfo? = null
        private set
    var isResize: Boolean = false
    lateinit var skin: Skin
    var status: Status = Status.NULL
        private set

    fun skinInitialized(): Boolean = ::skin.isInitialized

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
    val isInit: Boolean
        get() = status == Status.NULL

    fun lost() {
        status = Status.LOST
        source.fireGameEvent(StatusChangedGameEvent(this, Status.LOST))
    }

    operator fun next(): Boolean {
        val nextLevel = getNextLevel(level)
        if (nextLevel == null) {
            image = null
            status = Status.WON_GAME
            source.fireGameEvent(StatusChangedGameEvent(this, Status.WON_GAME))
            return false
        }
        level = nextLevel
        field.init(nextLevel.width, nextLevel.height, nextLevel.mines)
        if (nextLevel.image != null) {
            image = nextLevel.image?.let { applet.getImage(it) }
        }
        if (nextLevel.cover != null) {
            cover = nextLevel.cover?.let { applet.getImage(it) }
        }
        status = Status.GAME
        source.fireGameEvent(StatusChangedGameEvent(this, Status.GAME))
        return true
    }

    fun getNextLevel(current: LevelInfo?): LevelInfo? {
        if (campaign.levels.isEmpty()) {
            return null
        }
        if (current == null) {
            return campaign.levels[0]
        }
        for (i in campaign.levels.indices) {
            val level = campaign.levels[i]
            if (level == current) {
                val next = i + 1
                return if (next == campaign.levels.size) null else campaign.levels[next]
            }
        }
        return null
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

    fun start(campaign: CampaignInfo) {
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
