package net.sourceforge.fenixedu.presentationTier.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AllPredicate;

public class CollectionFilter<T extends DomainObject> implements Serializable {

    private final Comparator<T> comparator;
    private Set<Predicate> predicates;

    public Set<Predicate> getPredicates() {
        return predicates;
    }

    public CollectionFilter() {
        this.comparator = null;
    }

    public CollectionFilter(Set<Predicate> predicates) {
        this(predicates, null);
    }

    public CollectionFilter(Predicate predicate) {
        this(Collections.singleton(predicate), null);
    }

    public CollectionFilter(Set<Predicate> predicates, Comparator<T> comparator) {
        this.comparator = comparator;
        this.predicates = predicates;
    }

    public CollectionFilter(Predicate predicate, Comparator<T> comparator) {
        this(Collections.singleton(predicate), comparator);
    }

    public Set<T> filter(Set<T> elements) {
        Set<T> filtered;

        if (comparator == null) {
            filtered = new HashSet<T>();
        } else {
            filtered = new TreeSet<T>(comparator);
        }

        final Set<Predicate> predicates = getPredicates();

        if (predicates == null || predicates.isEmpty()) {
            filtered.addAll(elements);
            return Collections.unmodifiableSet(filtered);
        }
        Predicate predicate;
        if (predicates.size() == 1) {
            predicate = predicates.iterator().next();
        } else {
            predicate = AllPredicate.getInstance(predicates);
        }
        for (T element : elements) {
            if (predicate.evaluate(element)) {
                filtered.add(element);
            }
        }
        return Collections.unmodifiableSet(filtered);
    }

}
