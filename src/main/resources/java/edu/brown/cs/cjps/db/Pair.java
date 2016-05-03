package edu.brown.cs.cjps.db;

import java.util.Objects;

//Class to represent a pair.
public class Pair<K, V> {
  
  //The key associated with a Pair.
  private K key;
  
  //The value associated with a Pair.
  private V value;
  
  public Pair(K k, V v) {
    this.key = k;
    this.value = v;
  }
  
  public K getKey() {
    return this.key;
  }
  
  public V getValue() {
    return this.value;
  }
  
  @Override 
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) {
      return false;
    } else {
      Pair<? , ?> other = (Pair<?, ?>) o;
      return this.getKey().equals(other.getKey()) 
          && this.getValue().equals(other.getValue());
    }
    
  }
    
@Override
 public String toString() {
   StringBuilder sb = new StringBuilder();
   sb.append("Key: ");
   sb.append(this.getKey().toString());
   sb.append(" , ");
   sb.append("Value: ");
   sb.append(this.getValue().toString());
   return sb.toString();
   
 }

@Override
public int hashCode() {
  return Objects.hash(this.getKey(), this.getValue());
}
}
