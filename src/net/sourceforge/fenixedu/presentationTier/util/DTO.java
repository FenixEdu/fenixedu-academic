/*
 * Created on Mar 21, 2004
 */
package net.sourceforge.fenixedu.presentationTier.util;

import java.util.Hashtable;



/**
 * This class is used to communicate and pass data between different layers.
 * It's based on a hash table, so you may put any number of items inside. Flexible, hum?
 */

public class DTO {
	
	private Hashtable<String, Object> table;
	
	public DTO() {
		super();
		this.table = new Hashtable<String, Object>();
	}
	
	public Object get(String key) {
		return this.table.get(key);
	}
	
	public void set(String key, Object obj) {
		this.table.put(key, obj);
	}

	public void set(String key, int i) {
		this.table.put(key, new Integer(i));		
	}
}
