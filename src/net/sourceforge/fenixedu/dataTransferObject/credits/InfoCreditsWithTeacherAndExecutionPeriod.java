package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCreditsWithTeacherAndExecutionPeriod extends InfoCreditsWithTeacher {

    /**
     * @param infoCredits
     */
    public InfoCreditsWithTeacherAndExecutionPeriod(InfoCredits infoCredits) {
        super(infoCredits);
    }

    private InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod;

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }
}