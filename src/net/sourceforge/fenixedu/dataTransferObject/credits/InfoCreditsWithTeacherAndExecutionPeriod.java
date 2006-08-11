package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;

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

    private InfoExecutionPeriod infoExecutionPeriod;

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
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }
}