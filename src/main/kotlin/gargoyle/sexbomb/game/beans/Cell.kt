package gargoyle.sexbomb.game.beans

import java.io.Serializable

class Cell : Serializable {
    var count = 0
    var flag: Flag? = null
    var isMine = false
    var isOpen = false
    fun reset() {
        count = 0
        flag = null
        isMine = false
        isOpen = false
    }
}
