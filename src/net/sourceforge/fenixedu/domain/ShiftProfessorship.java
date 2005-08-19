package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

/**
 * @author Tânia & Alexandra
 *  
 */
public class ShiftProfessorship extends ShiftProfessorship_Base implements ICreditsEventOriginator {

    public String toString() {
        String result = "[CREDITS_TEACHER";
        result += ", codInt=" + getIdInternal();
        result += ", shift=" + getShift();
        result += "]";
        return result;
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

}
