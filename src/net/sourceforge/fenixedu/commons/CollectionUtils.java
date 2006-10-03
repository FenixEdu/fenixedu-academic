/*
 * Created on Oct 31, 2003
 *
 */
package net.sourceforge.fenixedu.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;

import org.apache.commons.collections.Predicate;

/**
 * @author Luis Cruz
 * 
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

	/**
	 * 
	 */
	public CollectionUtils() {
		super();
	}

	public static String[] toArrayOfString(Collection collection) {
		String[] strings = new String[collection.size()];

		int i = 0;
		Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			strings[i++] = (String) iterator.next();
		}

		return strings;
	}

	public static List toList(Object[] array) {
		List result = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			result.add(array[i]);
		}
		return result;
	}

	public static InfoObject getByInternalId(Collection infoObjectList, final Integer idInternal) {
		InfoObject infoObject = (InfoObject) CollectionUtils.find(infoObjectList, new Predicate() {

			public boolean evaluate(Object obj) {
				InfoObject infoObj = (InfoObject) obj;
				return infoObj.getIdInternal().equals(idInternal);
			}

		});

		return infoObject;
	}

	public static List getCombinations(List list, int count) {
		Combinations c = new Combinations(list, count);

		List results = new ArrayList();

		while (c.hasMoreElements()) {
			results.add(c.nextElement());
		}

		return results;
	}

	public static List getPermutations(List list, int count) {
		Permutations p = new Permutations(list, count);

		List results = new ArrayList();

		while (p.hasMoreElements()) {
			results.add(p.nextElement());
		}

		return results;
	}
	
	public static <T extends DomainObject> List<T> toObjects(List<DomainReference<T>> references) {
		List<T> objects = new ArrayList<T>();
		
		for(DomainReference<T> reference : references) {
			objects.add(reference.getObject());
		}
		
		return objects;
	}
	
	public static <T extends DomainObject> List<DomainReference<T>> toReferences(List<T> objects) {
		List<DomainReference<T>> references = new ArrayList<DomainReference<T>>();
		
		for(T object : objects) {
			references.add(new DomainReference<T>(object));
		}
		
		return references;
	}

}