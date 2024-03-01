package attmayMBBot.util;

public class Pair<K, V>{
    private K key;
    private V value;

    public Pair(K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pair){
            Pair<K,V> pair = (Pair<K,V>) obj;
            return pair.getKey().equals(this.getKey()) && pair.getValue().equals(this.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}
