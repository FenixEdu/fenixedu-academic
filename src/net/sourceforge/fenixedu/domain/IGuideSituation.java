package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.SituationOfGuide;
import net.sourceforge.fenixedu.util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IGuideSituation extends IDomainObject {

    public SituationOfGuide getSituation();

    public Date getDate();

    public String getRemarks();

    public IGuide getGuide();

    public State getState();

    public void setSituation(SituationOfGuide situation);

    public void setDate(Date date);

    public void setRemarks(String remarks);

    public void setGuide(IGuide guide);

    public void setState(State state);
}