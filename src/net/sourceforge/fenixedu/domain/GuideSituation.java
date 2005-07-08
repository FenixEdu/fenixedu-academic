package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends GuideSituation_Base {

    public GuideSituation() {
    }

    public GuideSituation(GuideState situation, String remarks, Date date, IGuide guide, State state) {
        this.setRemarks(remarks);
        this.setGuide(guide);
        this.setSituation(situation);
        this.setDate(date);
        this.setState(state);
    }

    public String toString() {
        String result = "[GUIDE SITUATION";
        result += ", remarks=" + getRemarks();
        result += ", guide=" + getGuide();
        result += ", guide Situtation=" + getSituation();
        result += ", date=" + getDate();
        result += ", state=" + getState();
        result += "]";
        return result;
    }

}
