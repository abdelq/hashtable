/**
 * @author Abdelhakim Qbaich
 */

public class HashEntry {
    private String key;
    private Object value;

    HashEntry(String key, Object value) {
        this.key = key;
        this.value = value;
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
