package gargoyle.sexbomb.game.ui

import gargoyle.sexbomb.game.beans.Cell
import gargoyle.sexbomb.game.beans.Field
import gargoyle.sexbomb.game.beans.Flag
import gargoyle.sexbomb.game.beans.Game
import gargoyle.sexbomb.game.beans.Status
import gargoyle.sexbomb.game.event.GameEvent
import gargoyle.sexbomb.game.event.GameListener
import gargoyle.sexbomb.game.event.StatusChangedGameEvent
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.net.URL
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.SwingUtilities

class Board(game: Game, resBgTitle: URL?) : JComponent(), GameListener {
    private val game: Game
    private lateinit var bgTitle: Image
    private var highlight: Point? = null

    init {
        if (!::bgTitle.isInitialized) {
            bgTitle = ImageIcon(resBgTitle).image
        }
        this.game = game
        game.source.addGameListener(this)
        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                onMouseMoved(e.x, e.y)
            }
        })
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (this@Board.game.isLost) {
                    this@Board.game.start()
                } else if (this@Board.game.isWonLevel) {
                    if (!this@Board.game.next()) {
                        this@Board.game.wonGame()
                    }
                } else if (this@Board.game.isWonGame) {
                    game.reset()
                } else if (this@Board.game.isGame) {
                    val field = this@Board.game.field
                    val clicked = getCLickedCell(e.x, e.y)
                    val cell = field.getCellAt(clicked.x, clicked.y)
                    if (cell != null) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (cell.flag == null) {
                                if (cell.isMine) {
                                    this@Board.game.lost()
                                }
                                field.open(clicked.x, clicked.y)
                            }
                            if (field.isOpen()) {
                                this@Board.game.wonLevel()
                            }
                        }
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (!cell.isOpen) {
                                field.flag(clicked.x, clicked.y)
                            }
                        }
                    }
                }
                repaint()
            }

            override fun mouseExited(e: MouseEvent) {
                highlight = null
            }
        })
    }

    private fun getCLickedCell(x: Int, y: Int): Point {
        val field = game.field
        return Point(x * field.width / width, y * field.height / height)
    }

    override fun getSize(): Dimension {
        return if (!game.isResize) super.getSize() else preferredSize
    }

    override fun onGameEvent(event: GameEvent) {
        if (event is StatusChangedGameEvent) {
            refine(event.status)
        }
    }

    private fun onMouseMoved(x: Int, y: Int) {
        if (game.isGame) {
            val prev = highlight
            highlight = getCLickedCell(x, y)
            if (isVisible && (prev == null || prev.x != highlight!!.x || prev.y != highlight!!.y)) {
                repaint()
            }
        }
    }

    override fun paint(g: Graphics) {
        if (!game.fieldInitialized() || game.level == null) {
            super.paint(g)
            paintTitle(g)
            return
        }
        if (game.isWonGame || game.isWonLevel) {
            val image = game.image
            if (image != null) {
                g.drawImage(
                    image, 0, 0, width, height, 0, 0, image.getWidth(this), image.getHeight(this),
                    this
                )
            }
        }
        (g as Graphics2D).composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA)
        if (game.isGame || game.isLost) {
            val field = game.field
            for (x in 0 until field.width) {
                for (y in 0 until field.height) {
                    paintCell(g, x, y)
                }
            }
        }
    }

    override fun getPreferredSize(): Dimension {
        val image = game.image
        if (image != null) {
            val w = image.getWidth(this)
            val h = image.getHeight(this)
            if (w != 0 && h != 0) {
                return Dimension(w, h)
            }
        }
        return Dimension(bgTitle.getWidth(this), bgTitle.getHeight(this))
    }

    override fun getMaximumSize(): Dimension {
        return if (!game.isResize) size else preferredSize
    }

    override fun getMinimumSize(): Dimension {
        return if (!game.isResize) size else preferredSize
    }

    override fun getWidth(): Int {
        return size.width
    }

    override fun getHeight(): Int {
        return size.height
    }

    private fun paintCell(g: Graphics, x: Int, y: Int) {
        val field = game.field
        val cell = field.getCellAt(x, y)
        val rectangle = getCellRect(x, y, field.width, field.height, Rectangle(width, height))
        paintCellImage(g, x, y, field, cell, rectangle)
        paintCellBorder(g, x, y, cell, rectangle)
        if (game.skinInitialized()) {
            val skin = game.skin
            if (game.isGame || game.isLost) {
                if (cell!!.isOpen) {
                    when (cell.count) {
                        0 -> paintImage(g, skin.open0, rectangle)
                        1 -> paintImage(g, skin.open1, rectangle)
                        2 -> paintImage(g, skin.open2, rectangle)
                        3 -> paintImage(g, skin.open3, rectangle)
                        4 -> paintImage(g, skin.open4, rectangle)
                        5 -> paintImage(g, skin.open5, rectangle)
                        6 -> paintImage(g, skin.open6, rectangle)
                        7 -> paintImage(g, skin.open7, rectangle)
                        8 -> paintImage(g, skin.open8, rectangle)
                    }
                } else {
                    paintImage(g, skin.closed, rectangle)
                    if (cell.flag != null) {
                        when (cell.flag) {
                            Flag.FLAG -> paintImage(g, skin.flag, rectangle)
                            Flag.QUESTION -> paintImage(g, skin.question, rectangle)
                            else -> {}
                        }
                    }
                }
            }
            if (game.isLost) {
                if (cell!!.isOpen && cell.isMine) {
                    paintImage(g, skin.mined, rectangle)
                } else if (!cell.isOpen && cell.flag != null && cell.isMine) {
                    paintImage(g, skin.mine, rectangle)
                } else if (!cell.isOpen && cell.flag != null && !cell.isMine) {
                    paintImage(g, skin.noMine, rectangle)
                }
            }
        } else {
            if (!cell!!.isOpen) {
                paintCellFlag(g, cell, rectangle)
            }
            if (cell.isOpen && !cell.isMine) {
                paintCellCount(g, cell, rectangle)
            }
            if (game.isLost) {
                if (cell.isOpen && cell.isMine) {
                    paintCellBombed(g, rectangle)
                } else if (!cell.isOpen) {
                    paintCellIfBomb(g, cell, rectangle)
                }
            }
        }
    }

    private fun paintCellBombed(g: Graphics, rectangle: Rectangle) {
        val c = g.color
        g.color = Color.RED
        val mark = CHAR_MARK_BOMBED
        val fm = getFontMetrics(font)
        val bb = fm.getStringBounds(mark, g).bounds
        g.drawString(
            mark, rectangle.x + (rectangle.width - bb.width) / 2,
            rectangle.y + (rectangle.height - bb.height) / 2 + fm.ascent
        )
        g.color = c
    }

    private fun paintCellBorder(g: Graphics, x: Int, y: Int, cell: Cell?, rectangle: Rectangle) {
        val c = g.color
        g.color = if (highlight != null && x == highlight!!.x && y == highlight!!.y) Color.YELLOW else Color.BLACK
        g.draw3DRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, !cell!!.isOpen)
        g.color = c
    }

    private fun paintCellCount(g: Graphics, cell: Cell?, rectangle: Rectangle) {
        if (cell!!.isOpen && cell.count > 0) {
            val count = cell.count.toString()
            val fm = getFontMetrics(font)
            val bb = fm.getStringBounds(count, g).bounds
            g.drawString(
                count, rectangle.x + (rectangle.width - bb.width) / 2,
                rectangle.y + (rectangle.height - bb.height) / 2 + fm.ascent
            )
        }
    }

    private fun paintCellFlag(g: Graphics, cell: Cell, rectangle: Rectangle) {
        val c = g.color
        g.color =
            if (cell.flag == null) Color.BLACK else if (cell.flag == Flag.FLAG) Color.RED else Color.GREEN
        val flag = if (cell.flag == null) "" else if (cell.flag == Flag.FLAG) CHAR_FLAG else CHAR_QUESTION
        val fm = getFontMetrics(font)
        val bb = fm.getStringBounds(flag, g).bounds
        g.drawString(
            flag, rectangle.x + (rectangle.width - bb.width) / 2,
            rectangle.y + (rectangle.height - bb.height) / 2 + fm.ascent
        )
        g.color = c
    }

    private fun paintCellIfBomb(g: Graphics, cell: Cell, rectangle: Rectangle) {
        val c = g.color
        g.color =
            if (cell.flag == null) if (cell.isMine) Color.GRAY else Color.WHITE else if (cell.isMine) Color.RED else Color.GREEN
        val mark = "*"
        val fm = getFontMetrics(font)
        val bb = fm.getStringBounds(mark, g).bounds
        if (cell.flag != null || cell.isMine) {
            g.drawString(
                mark, rectangle.x + (rectangle.width - bb.width) / 2,
                rectangle.y + (rectangle.height - bb.height) / 2 + fm.ascent
            )
        }
        g.color = c
    }

    private fun paintCellImage(g: Graphics, x: Int, y: Int, field: Field, cell: Cell?, rectangle: Rectangle) {
        val img = if (cell!!.isOpen) game.image else game.cover
        if (img != null) {
            val rect = getCellRect(
                x, y, field.width, field.height,
                Rectangle(img.getWidth(this), img.getHeight(this))
            )
            g.drawImage(
                img, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height,
                rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, this
            )
        } else {
            val c = g.color
            g.color = if (cell.isOpen) Color.GRAY else if (cell.flag == null) Color.YELLOW else Color.white
            g.fill3DRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, !cell.isOpen)
            g.color = c
        }
    }

    private fun paintImage(g: Graphics, img: Image?, rectangle: Rectangle) {
        if (img != null) {
            g.drawImage(
                img, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, 0,
                0, img.getWidth(this), img.getHeight(this), this
            )
        }
    }

    private fun paintTitle(g: Graphics) {
        if (::bgTitle.isInitialized) {
            g.drawImage(
                bgTitle, 0, 0, width, height, 0, 0, bgTitle.getWidth(this), bgTitle.getHeight(this),
                this
            )
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun refine(status: Status) {
        val field = game.field
        val rectangle = getCellRect(0, 0, field.width, field.height, Rectangle(width, height))
        font = font.deriveFont(rectangle.getHeight().toFloat())
        if (game.isResize) {
            val image = game.image
            val img = image ?: bgTitle
            val size = Dimension(img.getWidth(this), img.getHeight(this))
            setSizes(size)
        }
    }

    private fun setSizes(size: Dimension) {
        setSize(size)
        maximumSize = size
        minimumSize = size
        preferredSize = size
    }

    companion object {
        private const val ALPHA = 0.7f
        private const val CHAR_FLAG = "F"
        private const val CHAR_MARK_BOMBED = "X"
        private const val CHAR_QUESTION = "?"
        private fun getCellRect(x: Int, y: Int, width: Int, height: Int, rectangle: Rectangle): Rectangle {
            val rect = Rectangle()
            rect.width = rectangle.width / width - 1
            rect.height = rectangle.height / height - 1
            rect.x = (x * rectangle.getWidth() / width).toInt()
            rect.y = (y * rectangle.getHeight() / height).toInt()
            return rect
        }
    }
}
