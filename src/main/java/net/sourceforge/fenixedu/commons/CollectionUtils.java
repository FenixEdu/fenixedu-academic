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
import pt.utl.ist.fenix.tools.predicates.Predicate;

/**
 * @author Luis Cruz
 * 
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

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
        for (Object element : array) {
            result.add(element);
        }
        return result;
    }

    public static InfoObject getByInternalId(Collection<InfoObject> infoObjectList, final Integer externalId) {
        InfoObject infoObject = find(infoObjectList, new Predicate<InfoObject>() {

            @Override
            public boolean eval(InfoObject infoObj) {
                return infoObj.getExternalId().equals(externalId);
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

    /**
     * Selects all elements from input collection which match the given
     * predicate into an output collection.
     * 
     * @param inputCollection
     *            the collection to get the input from, may not be null
     * @param predicate
     *            the predicate to use, may be null
     * @return the elements matching the predicate (new list)
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {

        final List<T> result = new ArrayList<T>();

        for (final T each : collection) {
            if (predicate.eval(each)) {
                result.add(each);
            }
        }

        return result;
    }

    /**
     * Finds the first element in the given collection which matches the given
     * predicate.
     * <p>
     * If the input collection or predicate is null, or no element of the collection matches the predicate, null is returned.
     * 
     */
    public static <T> T find(Collection<T> collection, Predicate<T> predicate) {
        if (collection != null && predicate != null) {
            for (T item : collection) {
                if (predicate.eval(item)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Returns a new Collection consisting of the elements of inputCollection
     * transformed by the given transformer.
     * <p>
     * If the input transformer is null, the result is an empty list.
     * 
     * @param inputCollection
     *            the collection to get the input from, may not be null
     * @param transformer
     *            the transformer to use, may be null
     * @return the transformed result (new list)
     */
    public static <T, E> List<T> collect(Collection<E> collection, Transformer<E, T> transformer) {

        List<T> list = new ArrayList<T>(collection.size());
        if (transformer != null && collection != null) {
            Iterator<E> iterator = collection.iterator();

            while (iterator.hasNext()) {
                list.add(transformer.transform(iterator.next()));
            }
        }
        return list;

    }

    /**
     * Executes the given closure on each element in the collection.
     * <p>
     * If the input collection or closure is null, there is no change made.
     * 
     * @param collection
     *            the collection to get the input from, may be null
     * @param closure
     *            the closure to perform, may be null
     */
    public static <T> void forAllDo(Collection<T> collection, Closure<T> closure) {
        if (collection != null && closure != null) {
            for (T t : collection) {
                closure.execute(t);
            }
        }
    }

    /**
     * Filter the collection by applying a Predicate to each element. If the
     * predicate returns false, remove the element.
     * <p>
     * If the input collection or predicate is null, there is no change made.
     * 
     * @param collection
     *            the collection to get the input from, may be null
     * @param predicate
     *            the predicate to use as a filter, may be null
     */
    public static <T> void filterMatching(Collection<T> collection, Predicate<T> predicate) {
        if (collection != null && predicate != null) {
            for (Iterator<T> it = collection.iterator(); it.hasNext();) {
                if (predicate.eval(it.next()) == false) {
                    it.remove();
                }
            }
        }
    }

    public static <T> boolean anyMatches(Collection<T> collection, Predicate<T> predicate) {
        if (collection != null && predicate != null) {
            for (T t : collection) {
                if (predicate.eval(t)) {
                    return true;
                }
            }
        }
        return false;
    }

}