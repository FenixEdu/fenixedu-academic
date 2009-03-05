package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.fenixframework.pstm.PartialExternalization;
import org.joda.time.Partial;

public class PartialList implements Serializable {

    private final List<Partial> partials;

    public PartialList() {
        // The following use of an emptyList (immutable) is the
        // correct way of doing this, but I'm not sure whether the
        // current code is relying on being able to mutate the result
        // of the getPartials() method or not.  If it is, it is not
        // transactional, but it may work most of the time, whereas if
        // I change this to an immutable collection, then things may
        // break abruptly.  This will need to be checked later...
        //
        //this.partials = Collections.emptyList();
        this.partials = new ArrayList<Partial>();
    }

    public PartialList(List<Partial> partials) {
        this.partials = new ArrayList<Partial>(partials);
    }

    public List<Partial> getPartials() {
	return partials;
    }

    public List<Partial> getSortedPartials() {
	List<Partial> result = new ArrayList<Partial>(this.partials);
	Collections.sort(result);
	return result;
    }

    // The two following methods (exportAsString and importFromString)
    // came (with the necessary changes) from the
    // net.sourceforge.fenixedu.persistenceTier.Conversores.PartialList2SqlVarcharConverter
    // class.  So, go see there for history if you need to.

    public String exportAsString() {
        final StringBuilder result = new StringBuilder();
        for (Partial partial : getPartials()) {
            if (result.length() != 0) {
                result.append(";");
            }
            result.append(PartialExternalization.partialToString(partial));
        }
        return result.toString();
    }

    public static PartialList importFromString(String source) {
        // Should this be really here?
        if (source == null) {
            return null;
        }

        ArrayList<Partial> partials = new ArrayList<Partial>();
        for (final String partialString : source.split(";")) {
            partials.add(PartialExternalization.partialFromString(partialString));
        }
        return new PartialList(partials);
    }
}
