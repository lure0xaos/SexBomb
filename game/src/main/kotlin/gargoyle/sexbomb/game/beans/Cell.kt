package gargoyle.sexbomb.game.beans

import java.io.Serializable

class Cell : Serializable {
    var count: Int = 0
    var flag: Flag? = null
    var isMine: Boolean = false
    var isOpen: Boolean = false
    fun reset() {
        count = 0
        flag = null
        isMine = false
        isOpen = false
    }
}
