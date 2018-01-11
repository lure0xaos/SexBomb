package gargoyle.sexbomb.util.res.load;

import java.io.IOException;
import java.net.URL;

@FunctionalInterface
public interface Loader<T> {
    T load(URL url) throws IOException;
}
