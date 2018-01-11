package gargoyle.sexbomb.game.beans

import java.io.Serializable
import java.util.*

class Field : Serializable {
    private lateinit var cells: Array<Array<Cell>>
    var flag: Int = 0
        private set
    var height: Int = 0
        private set
    var left: Int = 0
        private set
    var mines: Int = 0
        private set
    var minesLeft: Int = 0
        private set
    var open: Int = 0
        private set
    var width: Int = 0
        private set

    private fun count(x: Int, y: Int): Int {
        var c = 0
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) {
                    continue
                }
                val xx = x + dx
                val yy = y + dy
                if (xx >= 0 && yy >= 0 && xx < width && yy < height && cells[xx][yy].isMine) {
                    c++
                }
            }
        }
        return c
    }

    fun flag(x: Int, y: Int) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return
        }
        val cell = cells[x][y]
        if (cell.isOpen) {
            return
        }
        val flag = cell.flag
        if (flag == null) {
            cell.flag = Flag.FLAG
            this.flag++
            minesLeft--
        }
        if (flag == Flag.FLAG) {
            cell.flag = Flag.QUESTION
        }
        if (flag == Flag.QUESTION) {
            cell.flag = null
            this.flag--
            minesLeft++
        }
    }

    fun getCellAt(x: Int, y: Int): Cell? {
        return if (x < 0 || y < 0 || x >= width || y >= height) null else cells[x][y]
    }

    private fun init(mines: Int) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val cell = cells[x][y]
                cell.reset()
            }
        }
        for (b in 0 until mines) {
            var x: Int
            var y: Int
            do {
                x = rnd(width)
                y = rnd(height)
            } while (cells[x][y].isMine)
            cells[x][y].isMine = (true)
        }
        open = 0
        flag = 0
        this.mines = mines
        minesLeft = mines
        left = width * height
    }

    private fun init(width: Int, height: Int) {
        if (!::cells.isInitialized || this.width != width || this.height != height) {
            cells = Array(width.also { this.width = it }) { Array(height.also { this.height = it }) { Cell() } }
            for (x in 0 until width) {
                for (y in 0 until height) {
                    cells[x][y] = Cell()
                }
            }
        } else {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val cell = cells[x][y]
                    cell.reset()
                }
            }
        }
        open = 0
        flag = 0
        mines = 0
        minesLeft = 0
        left = width * height
    }

    fun init(width: Int = this.width, height: Int = this.height, mines: Int = this.mines) {
        init(width, height)
        init(mines)
    }

    fun isOpen(): Boolean {
        return left == mines
    }

    fun open(x: Int, y: Int) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return
        }
        val cell = cells[x][y]
        if (cell.isOpen) {
            return
        }
        cell.count = count(x, y)
        cell.flag = null
        cell.isOpen = true
        open++
        left--
        if (cell.isMine) {
            return
        }
        if (cell.count == 0) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    if (dx != 0 || dy != 0) {
                        open(x + dx, y + dy)
                    }
                }
            }
        }
    }

    companion object {
        private val random = Random()
        private fun rnd(b: Int): Int {
            return random.nextInt(b)
        }
    }
}
