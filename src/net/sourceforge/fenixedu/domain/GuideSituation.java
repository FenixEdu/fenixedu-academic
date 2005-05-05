package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends GuideSituation_Base {

    protected GuideState situation;

    protected Date date;

    protected State state;

    protected IGuide guide;

    public GuideSituation() {
    }

    public GuideSituation(GuideState situation, String remarks, Date date, IGuide guide,
            State state) {
        setRemarks(remarks);
        this.guide = guide;
        this.situation = situation;
        this.date = date;
        this.state = state;

    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof GuideSituation) {
            GuideSituation guideSituation = (GuideSituation) obj;

            if (((getGuide() == null && guideSituation.getGuide() == null) || (getGuide()
                    .equals(guideSituation.getGuide())))
                    && ((getSituation() == null && guideSituation.getSituation() == null) || (getSituation()
                            .equals(guideSituation.getSituation())))) {
                resultado = true;
            }
        }

        return resultado;
    }

    public String toString() {
        String result = "[GUIDE SITUATION";

        result += ", remarks=" + getRemarks();
        result += ", guide=" + guide;
        result += ", guide Situtation=" + situation;
        result += ", date=" + date;
        result += ", state=" + state;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return
     */
    public IGuide getGuide() {
        return guide;
    }

    /**
     * @return
     */
    public GuideState getSituation() {
        return situation;
    }

    /**
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param guide
     */
    public void setGuide(IGuide guide) {
        this.guide = guide;
    }

    /**
     * @param guide
     */
    public void setSituation(GuideState guide) {
        situation = guide;
    }

    /**
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

}