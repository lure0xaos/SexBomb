package gargoyle.sexbomb;

import gargoyle.sexbomb.game.beans.Campaign;
import gargoyle.sexbomb.game.beans.Field;
import gargoyle.sexbomb.game.beans.Game;
import gargoyle.sexbomb.game.beans.Skin;
import gargoyle.sexbomb.game.beans.Status;
import gargoyle.sexbomb.game.event.GameEvent;
import gargoyle.sexbomb.game.event.GameListener;
import gargoyle.sexbomb.game.event.StatusChangedGameEvent;
import gargoyle.sexbomb.game.ui.AboutForm;
import gargoyle.sexbomb.game.ui.Board;
import gargoyle.sexbomb.util.applet.GXApplet;
import gargoyle.sexbomb.util.ini.PropertyMap;
import gargoyle.sexbomb.util.log.Log;
import gargoyle.sexbomb.util.res.Res;
import gargoyle.sexbomb.util.res.Resources;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public final class SexBomb extends GXApplet implements GameListener {
    private static final String PREFIX_CAMPAIGN = "campaign.";
    private static final String PREFIX_SKIN = "skin.";
    private static final String RES_HELP = "res/help.html";
    private static final String RES_TITLE = "res/title.gif";
    private static final String STR_ABOUT = "About";
    private static final String STR_EDIT = "Edit";
    private static final String STR_EXIT = "Exit";
    private static final String STR_FILE = "File";
    private static final String STR_HELP = "Help";
    private static final String STR_HELP_HTML = "Help";
    private static final String STR_LOOK_FEEL = "Look&Feel";
    private static final String STR_LOST = "You Lost!";
    private static final String STR_RESIZE = "Resize";
    private static final String STR_SKIN = Skin.class.getSimpleName();
    private static final String STR_WELCOME = "Welcome!";
    private static final String STR_WON_GAME = "You Won!";
    private static final String STR_WON_LEVEL = "You Won!";
    private Board board;
    private Game game;

    public SexBomb() throws HeadlessException {
        super(new Resources(true, Res.nearClassUrl(SexBomb.class, "")));
    }

    public static void main(String[] args) {
        GXApplet.run(SexBomb.class, args);
    }

    private JMenuBar createMenuBar(URL configUrl, Charset charset) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenuFile(configUrl, charset));
        menuBar.add(createMenuEdit(configUrl, charset));
        menuBar.add(createMenuHelp());
        return menuBar;
    }

    private JMenu createMenuCampaign(URL configUrl, Charset charset) {
        JMenu menu = new JMenu(Campaign.class.getSimpleName());
        try (Reader reader = new InputStreamReader(configUrl.openStream(), charset)) {
            PropertyMap file = new PropertyMap(reader);
            Set<Map.Entry<String, String>> entries = file.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if (entry.getKey().startsWith(PREFIX_CAMPAIGN)) {
                    URL url = Res.url(configUrl, entry.getValue());
                    if (url != null) {
                        Campaign campaign = new Campaign(url);
                        JMenuItem item = new JMenuItem(campaign.getName());
                        item.addActionListener(e -> newGame(campaign));
                        menu.add(item);
                    }
                }
            }
        } catch (IOException e1) {
            Log.error(e1.getLocalizedMessage(), e1);
        }
        return menu;
    }

    private JMenu createMenuEdit(URL configUrl, Charset charset) {
        JMenu menu = new JMenu(STR_EDIT);
        menu.add(createMenuLaF());
        menu.add(createMenuSkin(configUrl, charset));
        JMenuItem itmResize = new JCheckBoxMenuItem(new AbstractAction(STR_RESIZE) {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean resize = ((AbstractButton) e.getSource()).isSelected();
                onResizeCheck(resize);
            }
        });
        menu.add(itmResize);
        return menu;
    }

    @Override
    protected void doInit() {
        resources.addRoot(getDocumentBase());
        resources.addRoot(getCodeBase());
        if (board != null) {
            remove(board);
        }
        setLayout(new BorderLayout());
        showStatus(STR_WELCOME);
        game = new Game();
        board = new Board(game, getClass().getResource(RES_TITLE));
        game.source.addGameListener(this);
        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onBoardClick();
            }
        });
        URL configUrl = Res.nearClassUrl(SexBomb.class, String.format("%s.properties", SexBomb.class.getSimpleName()));
        JMenuBar menuBar = createMenuBar(configUrl, Charset.defaultCharset());
        JScrollPane scr = new JScrollPane(board);
        scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scr.setAutoscrolls(true);
        add(scr, BorderLayout.CENTER);
        setJMenuBar(menuBar);
        if (isApplication()) {
            game.setResize(true);
            pack();
            setSize(board.getSize());
        }
    }

    private JMenu createMenuFile(URL configUrl, Charset charset) {
        JMenu menu = new JMenu(STR_FILE);
        menu.add(createMenuCampaign(configUrl, charset));
        if (isApplication()) {
            JMenuItem itmExit = new JMenuItem(new AbstractAction(STR_EXIT) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    exit();
                }
            });
            menu.add(itmExit);
        }
        return menu;
    }

    private JMenu createMenuHelp() {
        JMenu menu = new JMenu(STR_HELP);
        JMenuItem itmHelp = new JMenuItem(new AbstractAction(STR_HELP_HTML) {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDocument(SexBomb.class.getResource(RES_HELP), "_blank");
            }
        });
        menu.add(itmHelp);
        JMenuItem itmAbout = new JMenuItem(new AbstractAction(STR_ABOUT) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new AboutForm(getRootFrame());
                dialog.setVisible(true);
            }
        });
        menu.add(itmAbout);
        return menu;
    }

    private JMenu createMenuLaF() {
        JMenu menu = new JMenu(STR_LOOK_FEEL);
        for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            JMenuItem item = new JMenuItem(new AbstractAction(laf.getName()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    changeLookAndFeel(laf.getClassName());
                }
            });
            menu.add(item);
        }
        return menu;
    }

    private JMenu createMenuSkin(URL configUrl, Charset charset) {
        JMenu menu = new JMenu(STR_SKIN);
        try (Reader reader = new InputStreamReader(configUrl.openStream(), charset)) {
            PropertyMap file = new PropertyMap(reader);
            Set<Map.Entry<String, String>> entries = file.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if (entry.getKey().startsWith(PREFIX_SKIN)) {
                    URL url = Res.url(configUrl, entry.getValue());
                    if (url != null) {
                        Skin skin = new Skin(url, charset);
                        JMenuItem item = new JMenuItem(new AbstractAction(skin.getName()) {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                setSkin(skin);
                            }
                        });
                        menu.add(item);
                    }
                }
            }
        } catch (IOException e1) {
            Log.error(e1.getLocalizedMessage(), e1);
        }
        return menu;
    }

    @Override
    protected void doDestroy() {
    }

    private void onBoardClick() {
        renew(game.getStatus());
    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {
    }

    private void newGame(Campaign campaign) {
        game.start(campaign);
    }

    @Override
    public void onGameEvent(GameEvent event) {
        if (event instanceof StatusChangedGameEvent) {
            renew(((StatusChangedGameEvent) event).getStatus());
        }
    }

    private void onLost() {
        showStatus(STR_LOST);
    }

    private void onRenewGame(Game game) {
        Field field = game.getField();
        if (field != null) {
            showStatus(game.getStatusString());
        }
    }

    private void onResizeCheck(boolean resize) {
        game.setResize(resize);
        board.refine(game.getStatus());
    }

    private void onWonGame() {
        showStatus(STR_WON_GAME);
    }

    private void onWonLevel() {
        showStatus(STR_WON_LEVEL);
    }

    private void pack() {
        SwingUtilities.getWindowAncestor(this).pack();
    }

    @SuppressWarnings("LawOfDemeter")
    private void renew(Status status) {
        if (game == null) {
            return;
        }
        if (game.isWonGame()) {
            onWonGame();
        }
        if (game.isWonLevel()) {
            onWonLevel();
        }
        if (game.isLost()) {
            onLost();
        }
        if (game.isGame()) {
            onRenewGame(game);
        }
        repaint();
    }

    private void setSkin(Skin skin) {
        game.setSkin(skin);
        repaint();
    }
}
