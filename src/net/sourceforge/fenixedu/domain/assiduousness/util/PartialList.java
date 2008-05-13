package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.Partial;

public class PartialList implements Serializable {

    private List<Partial> partials;

    public PartialList() {
    }

    public PartialList(List<Partial> partials) {
	setPartials(partials);
    }

    public List<Partial> getPartials() {
	if (partials == null) {
	    partials = new ArrayList<Partial>();
	}
	return partials;
    }

    public void setPartials(List<Partial> partials) {
	this.partials = partials;
    }

    public List<Partial> getSortedPartials() {
	List<Partial> result = new ArrayList<Partial>(getPartials());
	Collections.sort(result);
	return result;
    }

}
