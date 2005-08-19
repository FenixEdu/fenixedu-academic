/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain.degree.finalProject;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudent extends TeacherDegreeFinalProjectStudent_Base implements
        ICreditsEventOriginator {

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

}
