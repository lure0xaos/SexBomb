package gargoyle.sexbomb

import gargoyle.sexbomb.game.beans.Game
import gargoyle.sexbomb.game.beans.Skin
import gargoyle.sexbomb.game.beans.Status
import gargoyle.sexbomb.game.event.GameEvent
import gargoyle.sexbomb.game.event.GameListener
import gargoyle.sexbomb.game.event.StatusChangedGameEvent
import gargoyle.sexbomb.game.ui.AboutForm
import gargoyle.sexbomb.game.ui.Board
import gargoyle.sexbomb.res.Help
import gargoyle.sexbomb.services.CampaignInfo
import gargoyle.sexbomb.services.SkinInfo
import gargoyle.sexbomb.util.applet.GXApplet
import gargoyle.sexbomb.util.log.Log
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.*

class SexBomb : GXApplet(), GameListener {
    private lateinit var board: Board
    private lateinit var game: Game
    private fun createMenuBar(): JMenuBar {
        val menuBar = JMenuBar()
        menuBar.add(createMenuFile())
        menuBar.add(createMenuEdit())
        menuBar.add(createMenuHelp())
        return menuBar
    }

    private fun createMenuCampaign(): JMenu {
        val menu = JMenu("Campaign")
        try {
            ServiceLoader.load(CampaignInfo::class.java)
                .forEach { campaign -> menu.add(JMenuItem(campaign.name).apply { addActionListener { newGame(campaign) } }) }
        } catch (e1: Exception) {
            Log.error(e1, e1.localizedMessage)
        }
        return menu
    }

    private fun createMenuEdit(): JMenu {
        val menu = JMenu(STR_EDIT)
        menu.add(createMenuLaF())
        menu.add(createMenuSkin())
        val itmResize: JMenuItem = JCheckBoxMenuItem(object : AbstractAction(STR_RESIZE) {
            override fun actionPerformed(e: ActionEvent) {
                val resize = (e.source as AbstractButton).isSelected
                onResizeCheck(resize)
            }
        })
        menu.add(itmResize)
        return menu
    }

    override fun doInit() {
        if (::board.isInitialized) {
            remove(board)
        }
        layout = BorderLayout()
        showStatus(STR_WELCOME)
        game = Game(this)
        board = Board(this, game, Help.getTitleUrl())
        game.source.addGameListener(this)
        board.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                onBoardClick()
            }
        })
        val menuBar = createMenuBar()
        val scr = JScrollPane(board)
        scr.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        scr.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        scr.autoscrolls = true
        add(scr, BorderLayout.CENTER)
        setJMenuBar(menuBar)
        if (isApplication) {
            game.isResize = (true)
            pack()
            size = board.size
        }
    }

    private fun createMenuFile(): JMenu {
        val menu = JMenu(STR_FILE)
        menu.add(createMenuCampaign())
        if (isApplication) {
            val itmExit = JMenuItem(object : AbstractAction(STR_EXIT) {
                override fun actionPerformed(e: ActionEvent) {
                    exit()
                }
            })
            menu.add(itmExit)
        }
        return menu
    }

    private fun createMenuHelp(): JMenu {
        val menu = JMenu(STR_HELP)
        val itmHelp = JMenuItem(object : AbstractAction(STR_HELP_HTML) {
            override fun actionPerformed(e: ActionEvent) {
                showDocument(Help.getHelpUrl(), "_blank")
            }
        })
        menu.add(itmHelp)
        val itmAbout = JMenuItem(object : AbstractAction(STR_ABOUT) {
            override fun actionPerformed(e: ActionEvent) {
                val dialog: JDialog = AboutForm(rootFrame!!)
                dialog.isVisible = true
            }
        })
        menu.add(itmAbout)
        return menu
    }

    private fun createMenuLaF(): JMenu {
        val menu = JMenu(STR_LOOK_FEEL)
        for (laf in UIManager.getInstalledLookAndFeels()) {
            val item = JMenuItem(object : AbstractAction(laf.name) {
                override fun actionPerformed(e: ActionEvent) {
                    changeLookAndFeel(laf.className, this@SexBomb)
                }
            })
            menu.add(item)
        }
        return menu
    }

    private fun createMenuSkin(): JMenu {
        val menu = JMenu(STR_SKIN)
        for (skinInfo in ServiceLoader.load(SkinInfo::class.java)) {
            val skin = try {
                Skin(this, skinInfo)
            } catch (e1: Exception) {
                Log.error(e1, "cannot load skin ${skinInfo.name}")
                null
            }
            if (skin != null) {
                menu.add(JMenuItem(object : AbstractAction(skin.name) {
                    override fun actionPerformed(e: ActionEvent) {
                        setSkin(skin)
                    }
                }))
            }
        }
        return menu
    }

    override fun doDestroy() {}
    private fun onBoardClick() {
        renew(game.status)
    }

    override fun doStart() {}
    override fun doStop() {}
    private fun newGame(campaign: CampaignInfo) {
        game.start(campaign)
    }

    override fun onGameEvent(event: GameEvent) {
        if (event is StatusChangedGameEvent) {
            renew(event.status)
        }
    }

    private fun onLost() {
        showStatus(STR_LOST)
    }

    private fun onRenewGame(game: Game) {
        showStatus(game.statusString)
    }

    private fun onResizeCheck(resize: Boolean) {
        game.isResize = (resize)
        board.refine(game.status)
    }

    private fun onWonGame() {
        showStatus(STR_WON_GAME)
    }

    private fun onWonLevel() {
        showStatus(STR_WON_LEVEL)
    }

    private fun pack() {
        SwingUtilities.getWindowAncestor(this).pack()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun renew(status: Status) {
        if (game.isWonGame) {
            onWonGame()
        }
        if (game.isWonLevel) {
            onWonLevel()
        }
        if (game.isLost) {
            onLost()
        }
        if (game.isGame) {
            onRenewGame(game)
        }
        repaint()
    }

    private fun setSkin(skin: Skin) {
        game.skin = (skin)
        repaint()
    }

    companion object {
        private const val PREFIX_CAMPAIGN = "campaign."
        private const val PREFIX_SKIN = "skin."
        private const val STR_ABOUT = "About"
        private const val STR_EDIT = "Edit"
        private const val STR_EXIT = "Exit"
        private const val STR_FILE = "File"
        private const val STR_HELP = "Help"
        private const val STR_HELP_HTML = "Help"
        private const val STR_LOOK_FEEL = "Look&Feel"
        private const val STR_LOST = "You Lost!"
        private const val STR_RESIZE = "Resize"
        private val STR_SKIN = Skin::class.java.simpleName
        private const val STR_WELCOME = "Welcome!"
        private const val STR_WON_GAME = "You Won!"
        private const val STR_WON_LEVEL = "You Won!"

        @JvmStatic
        fun main(args: Array<String>) {
            run(SexBomb::class, args)
        }
    }
}
