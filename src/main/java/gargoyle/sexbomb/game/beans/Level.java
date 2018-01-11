package gargoyle.sexbomb.game.beans;

import gargoyle.sexbomb.util.ini.PropertyMap;
import gargoyle.sexbomb.util.log.Log;
import gargoyle.sexbomb.util.res.Res;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class Level {
    private static final int DEFAULT_HEIGHT = 10;
    private static final int DEFAULT_MINES = 10;
    private static final int DEFAULT_WIDTH = 10;
    private static final String PARAM_COVER = "cover";
    private static final String PARAM_HEIGHT = "height";
    private static final String PARAM_IMAGE = "image";
    private static final String PARAM_MINES = "mines";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_WIDTH = "width";
    private URL cover;
    private int height;
    private URL image;
    private int mines;
    private String name;
    private int width;

    public Level(URL url) {
        try (Reader reader = new InputStreamReader(url.openStream(), Charset.defaultCharset())) {
            PropertyMap file = new PropertyMap(reader);
            name = file.get(PARAM_NAME);
            width = file.get(PARAM_WIDTH, DEFAULT_WIDTH);
            height = file.get(PARAM_HEIGHT, DEFAULT_HEIGHT);
            mines = file.get(PARAM_MINES, DEFAULT_MINES);
            cover = Res.url(url, file.get(PARAM_COVER));
            image = Res.url(url, file.get(PARAM_IMAGE));
        } catch (IOException e) {
            Log.error(String.format("cannot load campaign from %s", url), e);
        }
    }

    public URL getCover() {
        return cover;
    }

    public int getHeight() {
        return height;
    }

    public URL getImage() {
        return image;
    }

    public int getMines() {
        return mines;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }
}
