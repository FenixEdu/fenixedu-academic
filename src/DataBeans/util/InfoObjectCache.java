/*
 * Created on Jan 3, 2004
 *  
 */
package DataBeans.util;

import org.apache.commons.collections.FastHashMap;
import org.apache.ojb.broker.Identity;

import DataBeans.InfoObject;

/**
 * @author Luis Cruz
 *  
 */
public class InfoObjectCache
{

	private static FastHashMap infoObjectTable;

	static
	{
		infoObjectTable = new FastHashMap();
		infoObjectTable.setFast(true);
	}

	public static void cache(Identity key, InfoObject value)
	{
		remove(key);
		infoObjectTable.put(key, value);
	}

	public static InfoObject lookup(Identity key)
	{
		return (InfoObject) infoObjectTable.get(key);
	}

	public static void remove(Identity key)
	{
		infoObjectTable.remove(key);
	}

	public static void clear()
	{
		infoObjectTable.clear();
	}

}
