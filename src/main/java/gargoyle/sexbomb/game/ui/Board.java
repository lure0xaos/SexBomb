package gargoyle.sexbomb.game.ui;

import gargoyle.sexbomb.game.beans.Cell;
import gargoyle.sexbomb.game.beans.Field;
import gargoyle.sexbomb.game.beans.Flag;
import gargoyle.sexbomb.game.beans.Game;
import gargoyle.sexbomb.game.beans.Skin;
import gargoyle.sexbomb.game.beans.Status;
import gargoyle.sexbomb.game.event.GameEvent;
import gargoyle.sexbomb.game.event.GameListener;
import gargoyle.sexbomb.game.event.StatusChangedGameEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;

@SuppressWarnings("LawOfDemeter")
public class Board extends JComponent implements GameListener {
    private static final float ALPHA = 0.7f;
    private static final String CHAR_FLAG = "F";
    private static final String CHAR_MARK_BOMBED = "X";
    private static final String CHAR_QUESTION = "?";
    private final Game game;
    private Image bgTitle;
    private Point highlight;

    public Board(Game game, URL resBgTitle) {
        if (bgTitle == null) {
            bgTitle = new ImageIcon(resBgTitle).getImage();
        }
        this.game = game;
        game.source.addGameListener(this);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e.getX(), e.getY());
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Board.this.game == null) {
                    return;
                }
                if (Board.this.game.isLost()) {
                    Board.this.game.start();
                } else if (Board.this.game.isWonLevel()) {
                    if (!Board.this.game.next()) {
                        Board.this.game.wonGame();
                    }
                } else if (Board.this.game.isWonGame()) {
                    game.reset();
                } else if (Board.this.game.isGame()) {
                    Field field = Board.this.game.getField();
                    Point clicked = getCLickedCell(e.getX(), e.getY());
                    Cell cell = field.getCellAt(clicked.x, clicked.y);
                    if (cell != null) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (cell.getFlag() == null) {
                                if (cell.isMine()) {
                                    Board.this.game.lost();
                                }
                                field.open(clicked.x, clicked.y);
                            }
                            if (field.isOpen()) {
                                Board.this.game.wonLevel();
                            }
                        }
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (!cell.isOpen()) {
                                field.flag(clicked.x, clicked.y);
                            }
                        }
                    }
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                highlight = null;
            }
        });
    }

    private static Rectangle getCellRect(int x, int y, int width, int height, Rectangle rectangle) {
        Rectangle rect = new Rectangle();
        rect.width = rectangle.width / width - 1;
        rect.height = rectangle.height / height - 1;
        rect.x = (int) (x * rectangle.getWidth() / width);
        rect.y = (int) (y * rectangle.getHeight() / height);
        return rect;
    }

    private Point getCLickedCell(int x, int y) {
        Field field = game.getField();
        return new Point(x * field.getWidth() / getWidth(), y * field.getHeight() / getHeight());
    }

    @Override
    public Dimension getSize() {
        return game == null || !game.isResize() ? super.getSize() : getPreferredSize();
    }

    @Override
    public void onGameEvent(GameEvent event) {
        if (event instanceof StatusChangedGameEvent) {
            refine(((StatusChangedGameEvent) event).getStatus());
        }
    }

    private void onMouseMoved(int x, int y) {
        if (game != null && game.isGame()) {
            Point prev = highlight;
            highlight = getCLickedCell(x, y);
            if (isVisible() && (prev == null || prev.x != highlight.x || prev.y != highlight.y)) {
                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (game == null || game.getField() == null || game.getLevel() == null) {
            super.paint(g);
            paintTitle(g);
            return;
        }
        if (game.isWonGame() || game.isWonLevel()) {
            Image image = game.getImage();
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(this), image.getHeight(this),
                        this);
            }
        }
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA));
        if (game.isGame() || game.isLost()) {
            if (game.getField() == null) {
                return;
            }
            Field field = game.getField();
            for (int x = 0; x < field.getWidth(); x++) {
                for (int y = 0; y < field.getHeight(); y++) {
                    paintCell(g, x, y);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (game != null) {
            Image image = game.getImage();
            if (image != null) {
                int w = image.getWidth(this);
                int h = image.getHeight(this);
                if (w != 0 && h != 0) {
                    return new Dimension(w, h);
                }
            }
        }
        if (bgTitle != null) {
            return new Dimension(bgTitle.getWidth(this), bgTitle.getHeight(this));
        }
        return super.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return game == null || !game.isResize() ? getSize() : getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return game == null || !game.isResize() ? getSize() : getPreferredSize();
    }

    @Override
    public int getWidth() {
        return getSize().width;
    }

    @Override
    public int getHeight() {
        return getSize().height;
    }

    private void paintCell(Graphics g, int x, int y) {
        Field field = game.getField();
        Cell cell = field.getCellAt(x, y);
        Rectangle rectangle = getCellRect(x, y, field.getWidth(), field.getHeight(), new Rectangle(getWidth(), getHeight()));
        paintCellImage(g, x, y, field, cell, rectangle);
        paintCellBorder(g, x, y, cell, rectangle);
        if (game.getSkin() != null) {
            Skin skin = game.getSkin();
            if (game.isGame() || game.isLost()) {
                if (cell.isOpen()) {
                    switch (cell.getCount()) {
                        case 0:
                            paintImage(g, skin.getOpen0(), rectangle);
                            break;
                        case 1:
                            paintImage(g, skin.getOpen1(), rectangle);
                            break;
                        case 2:
                            paintImage(g, skin.getOpen2(), rectangle);
                            break;
                        case 3:
                            paintImage(g, skin.getOpen3(), rectangle);
                            break;
                        case 4:
                            paintImage(g, skin.getOpen4(), rectangle);
                            break;
                        case 5:
                            paintImage(g, skin.getOpen5(), rectangle);
                            break;
                        case 6:
                            paintImage(g, skin.getOpen6(), rectangle);
                            break;
                        case 7:
                            paintImage(g, skin.getOpen7(), rectangle);
                            break;
                        case 8:
                            paintImage(g, skin.getOpen8(), rectangle);
                            break;
                    }
                } else {
                    paintImage(g, skin.getClosed(), rectangle);
                    if (cell.getFlag() != null) {
                        switch (cell.getFlag()) {
                            case FLAG:
                                paintImage(g, skin.getFlag(), rectangle);
                                break;
                            case QUESTION:
                                paintImage(g, skin.getQuestion(), rectangle);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            if (game.isLost()) {
                if (cell.isOpen() && cell.isMine()) {
                    paintImage(g, skin.getMined(), rectangle);
                } else if (!cell.isOpen() && cell.getFlag() != null && cell.isMine()) {
                    paintImage(g, skin.getMine(), rectangle);
                } else if (!cell.isOpen() && cell.getFlag() != null && !cell.isMine()) {
                    paintImage(g, skin.getNoMine(), rectangle);
                }
            }
        } else {
            if (!cell.isOpen()) {
                paintCellFlag(g, cell, rectangle);
            }
            if (cell.isOpen() && !cell.isMine()) {
                paintCellCount(g, cell, rectangle);
            }
            if (game.isLost()) {
                if (cell.isOpen() && cell.isMine()) {
                    paintCellBombed(g, rectangle);
                } else if (!cell.isOpen()) {
                    paintCellIfBomb(g, cell, rectangle);
                }
            }
        }
    }

    private void paintCellBombed(Graphics g, Rectangle rectangle) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        String mark = CHAR_MARK_BOMBED;
        FontMetrics fm = getFontMetrics(getFont());
        Rectangle bb = fm.getStringBounds(mark, g).getBounds();
        g.drawString(mark, rectangle.x + (rectangle.width - bb.width) / 2,
                rectangle.y + (rectangle.height - bb.height) / 2 + fm.getAscent());
        g.setColor(c);
    }

    private void paintCellBorder(Graphics g, int x, int y, Cell cell, Rectangle rectangle) {
        Color c = g.getColor();
        g.setColor(highlight != null && x == highlight.x && y == highlight.y ? Color.YELLOW : Color.BLACK);
        g.draw3DRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, !cell.isOpen());
        g.setColor(c);
    }

    private void paintCellCount(Graphics g, Cell cell, Rectangle rectangle) {
        if (cell.isOpen() && cell.getCount() > 0) {
            String count = String.valueOf(cell.getCount());
            FontMetrics fm = getFontMetrics(getFont());
            Rectangle bb = fm.getStringBounds(count, g).getBounds();
            g.drawString(count, rectangle.x + (rectangle.width - bb.width) / 2,
                    rectangle.y + (rectangle.height - bb.height) / 2 + fm.getAscent());
        }
    }

    private void paintCellFlag(Graphics g, Cell cell, Rectangle rectangle) {
        Color c = g.getColor();
        g.setColor(cell.getFlag() == null ? Color.BLACK : cell.getFlag() == Flag.FLAG ? Color.RED : Color.GREEN);
        String flag = cell.getFlag() == null ? "" : cell.getFlag() == Flag.FLAG ? CHAR_FLAG : CHAR_QUESTION;
        FontMetrics fm = getFontMetrics(getFont());
        Rectangle bb = fm.getStringBounds(flag, g).getBounds();
        g.drawString(flag, rectangle.x + (rectangle.width - bb.width) / 2,
                rectangle.y + (rectangle.height - bb.height) / 2 + fm.getAscent());
        g.setColor(c);
    }

    private void paintCellIfBomb(Graphics g, Cell cell, Rectangle rectangle) {
        Color c = g.getColor();
        g.setColor(cell.getFlag() == null ?
                cell.isMine() ? Color.GRAY : Color.WHITE :
                cell.isMine() ? Color.RED : Color.GREEN);
        String mark = "*";
        FontMetrics fm = getFontMetrics(getFont());
        Rectangle bb = fm.getStringBounds(mark, g).getBounds();
        if (cell.getFlag() != null || cell.isMine()) {
            g.drawString(mark, rectangle.x + (rectangle.width - bb.width) / 2,
                    rectangle.y + (rectangle.height - bb.height) / 2 + fm.getAscent());
        }
        g.setColor(c);
    }

    private void paintCellImage(Graphics g, int x, int y, Field field, Cell cell, Rectangle rectangle) {
        Image img = cell.isOpen() ? game.getImage() : game.getCover();
        if (img != null) {
            Rectangle rect = getCellRect(x, y, field.getWidth(), field.getHeight(),
                    new Rectangle(img.getWidth(this), img.getHeight(this)));
            g.drawImage(img, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height,
                    rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, this);
        } else {
            Color c = g.getColor();
            g.setColor(cell.isOpen() ? Color.GRAY : cell.getFlag() == null ? Color.YELLOW : Color.white);
            g.fill3DRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, !cell.isOpen());
            g.setColor(c);
        }
    }

    private void paintImage(Graphics g, Image img, Rectangle rectangle) {
        if (img != null) {
            g.drawImage(img, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, 0,
                    0, img.getWidth(this), img.getHeight(this), this);
        }
    }

    private void paintTitle(Graphics g) {
        if (bgTitle != null) {
            g.drawImage(bgTitle, 0, 0, getWidth(), getHeight(), 0, 0, bgTitle.getWidth(this), bgTitle.getHeight(this),
                    this);
        }
    }

    public void refine(Status status) {
        if (game == null) {
            return;
        }
        Field field = game.getField();
        if (field == null) {
            return;
        }
        Rectangle rectangle = getCellRect(0, 0, field.getWidth(), field.getHeight(), new Rectangle(getWidth(), getHeight()));
        setFont(getFont().deriveFont((float) rectangle.getHeight()));
        if (game.isResize()) {
            Image image = game.getImage();
            Image img = image == null ? this.bgTitle : image;
            Dimension size = new Dimension(img.getWidth(this), img.getHeight(this));
            setSizes(size);
        }
    }

    private void setSizes(Dimension size) {
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
    }
}
