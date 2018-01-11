package gargoyle.sexbomb.game.beans;

import gargoyle.sexbomb.util.ini.PropertyMap;
import gargoyle.sexbomb.util.res.Res;
import gargoyle.sexbomb.util.res.load.Loaders;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;

public class Skin implements Serializable {
    private static final String PROP_CLOSED = "closed";
    private static final String PROP_FLAG = "flag";
    private static final String PROP_MINE = "mine";
    private static final String PROP_MINED = "mined";
    private static final String PROP_NAME = "name";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String PROP_NOMINE = "nomine";
    private static final String PROP_OPEN_0 = "open0";
    private static final String PROP_OPEN_1 = "open1";
    private static final String PROP_OPEN_2 = "open2";
    private static final String PROP_OPEN_3 = "open3";
    private static final String PROP_OPEN_4 = "open4";
    private static final String PROP_OPEN_5 = "open5";
    private static final String PROP_OPEN_6 = "open6";
    private static final String PROP_OPEN_7 = "open7";
    private static final String PROP_OPEN_8 = "open8";
    private static final String PROP_QUESTION = "question";
    private final Image closed;
    private final Image flag;
    private final Image mine;
    private final Image mined;
    private final String name;
    private final Image noMine;
    private final Image open0;
    private final Image open1;
    private final Image open2;
    private final Image open3;
    private final Image open4;
    private final Image open5;
    private final Image open6;
    private final Image open7;
    private final Image open8;
    private final Image question;

    public Skin(URL url, Charset charset) throws IOException {
        try (Reader reader = new InputStreamReader(url.openStream(), charset)) {
            PropertyMap file = new PropertyMap(reader);
            name = file.get(PROP_NAME);
            closed = image(url, file.get(PROP_CLOSED));
            flag = image(url, file.get(PROP_FLAG));
            mine = image(url, file.get(PROP_MINE));
            mined = image(url, file.get(PROP_MINED));
            noMine = image(url, file.get(PROP_NOMINE));
            open0 = image(url, file.get(PROP_OPEN_0));
            open1 = image(url, file.get(PROP_OPEN_1));
            open2 = image(url, file.get(PROP_OPEN_2));
            open3 = image(url, file.get(PROP_OPEN_3));
            open4 = image(url, file.get(PROP_OPEN_4));
            open5 = image(url, file.get(PROP_OPEN_5));
            open6 = image(url, file.get(PROP_OPEN_6));
            open7 = image(url, file.get(PROP_OPEN_7));
            open8 = image(url, file.get(PROP_OPEN_8));
            question = image(url, file.get(PROP_QUESTION));
        }
    }

    private static Image image(URL url, String name) throws IOException {
        return Loaders.load(Image.class, Res.url(url, name));
    }

    public Image getClosed() {
        return closed;
    }

    public Image getFlag() {
        return flag;
    }

    public Image getMine() {
        return mine;
    }

    public Image getMined() {
        return mined;
    }

    public String getName() {
        return name;
    }

    public Image getNoMine() {
        return noMine;
    }

    public Image getOpen0() {
        return open0;
    }

    public Image getOpen1() {
        return open1;
    }

    public Image getOpen2() {
        return open2;
    }

    public Image getOpen3() {
        return open3;
    }

    public Image getOpen4() {
        return open4;
    }

    public Image getOpen5() {
        return open5;
    }

    public Image getOpen6() {
        return open6;
    }

    public Image getOpen7() {
        return open7;
    }

    public Image getOpen8() {
        return open8;
    }

    public Image getQuestion() {
        return question;
    }
}
