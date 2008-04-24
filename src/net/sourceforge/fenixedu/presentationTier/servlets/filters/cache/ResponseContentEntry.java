package net.sourceforge.fenixedu.presentationTier.servlets.filters.cache;

import java.lang.ref.SoftReference;

import eu.ist.fenixframework.pstm.ReadSet;

import com.opensymphony.oscache.web.filter.ResponseContent;


public class ResponseContentEntry {
    private SoftReference<ReadSet> readSet = null;
    private ResponseContent content;

    public ResponseContentEntry() {
    }

    public void addReadSet(ReadSet newReadSet) {
	ReadSet currReadSet = (this.readSet == null) ? null : this.readSet.get();
	if (currReadSet == null) {
	    this.readSet = new SoftReference<ReadSet>(newReadSet);
	} else {
	    currReadSet.merge(newReadSet);
	}
    }

    public boolean isValid() {
	if (readSet == null) {
	    return false;
	} else {
	    ReadSet currReadSet = readSet.get();
	    return (currReadSet != null) && currReadSet.isStillCurrent();
	}
    }

    public void setContent(ResponseContent content) {
	this.content = content;
    }

    public ResponseContent getContent() {
	return content;
    }
}
