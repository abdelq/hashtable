/**
 * @author Abdelhakim Qbaich
 */

public abstract class AbstractHash {
    public abstract void insert(String key, Object value);

    public abstract void delete(String key);

    public abstract Object find(String key);
}
