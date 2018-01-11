package gargoyle.sexbomb.util.ini;

import gargoyle.sexbomb.util.log.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public final class PropertyMap {
    private final Map<String, String> entries = new HashMap<>();

    public PropertyMap(InputStream stream, Charset charset) {
        this(new InputStreamReader(stream, charset));
    }

    public PropertyMap(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            load(bufferedReader);
        } catch (IOException e) {
            Log.error(e.getLocalizedMessage(), e);
        }
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return entries.entrySet();
    }

    public String get(String key, String defaultValue) {
        return entries.getOrDefault(key, defaultValue);
    }

    public String get(String key) {
        return get(key, null);
    }

    public int get(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    public double get(String key, double defaultValue) {
        return Double.parseDouble(get(key, String.valueOf(defaultValue)));
    }

    public <T> T get(String key, T defaultValue, Function<String, T> parser) {
        return parser.apply(get(key, String.valueOf(defaultValue)));
    }

    private void load(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("#") || line.startsWith("//")) {
                continue;
            }
            int i = line.indexOf('=');
            if (i > 0) {
                String key = line.substring(0, i).trim();
                String value = line.substring(i + 1).trim();
                put(key, value);
            }
        }
    }

    public void put(String key, String value) {
        entries.put(key, value);
    }
}
