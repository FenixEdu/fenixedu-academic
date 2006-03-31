/**
 * 
 */


package pt.ist.utl.fenix.utils;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:27:42,31/Mar/2006
 * @version $Id$
 */
public class Pair<K, V> {

	private K key;

	private V value;

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
}
