/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.domain.log;

import java.util.Date;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentLog extends EnrolmentLog_Base {

    public EnrolmentLog(EnrolmentAction action, Registration registration, CurricularCourse curricularCourse, ExecutionPeriod executionPeriod, String who) {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
        this.setDate(new Date());
        this.setAction(action);
        this.setStudent(registration);        
        this.setCurricularCourse(curricularCourse);
        this.setExecutionPeriod(executionPeriod);
        this.setWho(who);
    }

}
