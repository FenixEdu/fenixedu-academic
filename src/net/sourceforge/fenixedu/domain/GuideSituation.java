package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends GuideSituation_Base {

    public GuideSituation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public GuideSituation(GuideState situation, String remarks, Date date, Guide guide, State state) {
	this();
	this.setRemarks(remarks);
	this.setGuide(guide);
	this.setSituation(situation);
	this.setDate(date);
	this.setState(state);
    }

    public void delete() {
	removeGuide();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
