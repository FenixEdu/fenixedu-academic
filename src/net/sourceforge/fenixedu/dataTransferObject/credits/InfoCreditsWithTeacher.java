package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCreditsWithTeacher extends InfoCreditsWrapper {

    /**
     * @param infoCredits
     */
    public InfoCreditsWithTeacher(InfoCredits infoCredits) {
        super(infoCredits);
    }

    private InfoTeacher infoTeacher;

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }
}