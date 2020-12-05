package pcmember.utility;

public class Pair <K,V> implements java.io.Serializable {
    private K key;
    private V value;
    public K getKey() {return this.key;}
    public V getValue() {return this.value;}
}
