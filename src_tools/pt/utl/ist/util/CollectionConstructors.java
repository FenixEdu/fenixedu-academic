package pt.utl.ist.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionConstructors
{

    public static HashSet newHashSet(final Object element) {
        final HashSet hashSet = new HashSet(1);
        hashSet.add(element);
        return hashSet;
    }

}
