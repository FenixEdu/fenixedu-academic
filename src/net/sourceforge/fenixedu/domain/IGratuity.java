/*
 * IGratuity
 *
 * Created on 28 of December 2002, 10:02
 */

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.GratuityState;
import net.sourceforge.fenixedu.util.State;

public interface IGratuity extends IDomainObject {

    public void setState(State state);

    public void setGratuityState(GratuityState gratuityState);

    public void setDate(Date date);

    public void setRemarks(String remarks);

    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);

    public State getState();

    public GratuityState getGratuityState();

    public Date getDate();

    public String getRemarks();

    public IStudentCurricularPlan getStudentCurricularPlan();

}