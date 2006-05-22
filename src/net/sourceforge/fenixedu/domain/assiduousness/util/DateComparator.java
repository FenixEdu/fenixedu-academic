package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;

public class DateComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        AssiduousnessRecord assidRecord1 = (AssiduousnessRecord)o1;
        AssiduousnessRecord assidRecord2 = (AssiduousnessRecord)o2;
        if (assidRecord1.getDate().isEqual(assidRecord2.getDate())) {
            return 0;
        } else if (assidRecord1.getDate().isBefore(assidRecord2.getDate())) {
            return -1;
        } else {
            return 1;
        }
    }
    
    
}
