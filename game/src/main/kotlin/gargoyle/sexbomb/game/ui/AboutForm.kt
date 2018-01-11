package gargoyle.sexbomb.game.ui

import gargoyle.sexbomb.res.Help
import gargoyle.sexbomb.util.log.Log
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Frame
import java.awt.event.ActionEvent
import java.io.IOException
import javax.swing.*

class AboutForm(frame: Frame) : JDialog(frame, true) {
    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        init(frame)
        preferredSize = Dimension(frame.width, frame.height / 2)
        pack()
        setLocation(frame.x, frame.y + (frame.height - height) / 2)
    }

    private fun destroy() {
        isVisible = false
        dispose()
    }

    private fun init(frame: Frame) {
        title = frame.title
        layout = BorderLayout()
        try {
            val content = JEditorPane(Help.getCopyrightUrl())
            content.isEditable = false
            add(content, BorderLayout.CENTER)
        } catch (e: IOException) {
            Log.error(e, e.localizedMessage)
        }
        val header = JLabel(title)
        header.font = font.deriveFont(font.size2D * 2)
        add(header, BorderLayout.NORTH)
        add(JButton(object : AbstractAction(STR_OK) {
            override fun actionPerformed(e: ActionEvent) {
                destroy()
            }
        }), BorderLayout.SOUTH)
        invalidate()
    }

    companion object {
        private const val STR_OK = "OK"
    }
}
