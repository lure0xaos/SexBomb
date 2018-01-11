package gargoyle.sexbomb.game.beans;

import gargoyle.sexbomb.util.ini.PropertyMap;
import gargoyle.sexbomb.util.log.Log;
import gargoyle.sexbomb.util.res.Res;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Campaign {
    private static final String PROP_NAME = "name";
    private final List<Level> levels = new ArrayList<>();
    private String name;

    public Campaign(URL url) {
        try (Reader reader = new InputStreamReader(url.openStream(), Charset.defaultCharset())) {
            PropertyMap file = new PropertyMap(reader);
            this.name = file.get(PROP_NAME);
            Set<Map.Entry<String, String>> entries = file.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if (!PROP_NAME.equals(entry.getKey())) {
                    URL levelUrl = Res.url(url, entry.getValue());
                    if (levelUrl != null) {
                        levels.add(new Level(levelUrl));
                    }
                }
            }
        } catch (IOException e) {
            Log.error(String.format("cannot load campaign from %s", url), e);
        }
    }

    public List<Level> getLevels() {
        return Collections.unmodifiableList(levels);
    }

    public String getName() {
        return name;
    }

    public Level getNextLevel(Level current) {
        if (levels.isEmpty()) {
            return null;
        }
        if (current == null) {
            return levels.get(0);
        }
        for (int i = 0; i < levels.size(); i++) {
            Level level = levels.get(i);
            if (Objects.equals(level, current)) {
                int next = i + 1;
                return next == levels.size() ? null : levels.get(next);
            }
        }
        return null;
    }
}
