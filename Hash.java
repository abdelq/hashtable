/**
 * @author Abdelhakim Qbaich
 */

public class Hash extends AbstractHash {
    HashEntry[] table;
    private HashEntry tombstone;

    // Sizes
    public static final int defaultSize = 32;
    public static final float threshold = 0.75f;
    private int currentSize, maxSize;

    public Hash() {
        table = new HashEntry[defaultSize];
        tombstone = new HashEntry();

        currentSize = 0;
        maxSize = (int) (defaultSize * threshold);
    }

    /**
     * String hashing using the Fowler–Noll–Vo hash function.
     *
     * @param  str String to hash.
     * @return a <i>positive</i> hash code value.
     */
    private int hashCode(String str) {
        int hashVal = 0x811c9dc5;

        if (str != null) {
            for (byte b : str.getBytes()) {
                hashVal ^= b;
                hashVal *= 0x01000193;
            }
        }

        return hashVal >>> 1;
    }

    public void resize(boolean up) {
        int newSize = (int) (table.length * (up ? 2 : .5));

        maxSize = (int) (newSize * threshold);
        currentSize = 0;

        HashEntry[] oldTable = table;
        table = new HashEntry[newSize];

        for (HashEntry entry : oldTable) {
            if (entry != null && entry != tombstone)
                insert(entry.getKey(), entry.getValue());
        }
    }

    public void insert(String key, Object value) {
        int hashCode = hashCode(key);
        int initialHash = hashCode % table.length;
        int hash = initialHash;
        int tombstoneIndex = -1;

        do {
            // End of probing: null found
            if (table[hash] == null) {
                if (tombstoneIndex == -1)
                    table[hash] = new HashEntry(hashCode, key, value);
                else
                    table[tombstoneIndex] = new HashEntry(hashCode, key, value);

                currentSize++;
                if (currentSize >= maxSize)
                    resize(true);

                return;
            }

            // Key exists
            if (hashCode == table[hash].getHash() &&
                    (key == table[hash].getKey() || key.equals(table[hash].getKey()))) {
                table[hash].setValue(value);
                return;
            }

            // Tombstone
            if (tombstoneIndex == -1 && table[hash] == tombstone)
                tombstoneIndex = hash;

            hash = (hash + 1) % table.length;
        } while (hash != initialHash);

        // End of probing: back to square one
        table[tombstoneIndex] = new HashEntry(hashCode, key, value);
        currentSize++;
        if (currentSize >= maxSize)
            resize(true);
    }

    public void delete(String key) {
        int hashCode = hashCode(key);
        int initialHash = hashCode % table.length;
        int hash = initialHash;

        do {
            if (table[hash] == null)
                return;

            if (hashCode == table[hash].getHash() &&
                    (key == table[hash].getKey() || key.equals(table[hash].getKey()))) {
                table[hash] = tombstone;
                currentSize--;

                // Not so sure about this
                if (currentSize <= maxSize && currentSize > defaultSize)
                    resize(false);

                return;
            }

            hash = (hash + 1) % table.length;
        } while (hash != initialHash);
    }

    public Object find(String key) {
        int hashCode = hashCode(key);
        int initialHash = hashCode % table.length;
        int hash = initialHash;

        do {
            if (table[hash] == null)
                return null;

            if (hashCode == table[hash].getHash() &&
                    (key == table[hash].getKey() || key.equals(table[hash].getKey())))
                return table[hash].getValue();

            hash = (hash + 1) % table.length;
        } while (hash != initialHash);

        return null;
    }
}
