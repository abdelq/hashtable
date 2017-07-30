/**
 * @author Abdelhakim Qbaich
 */

public class HashEntry {
    private int hash;
    private String key;
    private Object value;

    HashEntry() {}

    HashEntry(int hash, String key, Object value) {
        this.hash = hash;
        this.key = key;
        this.value = value;
    }

    public int getHash() {
        return hash;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
