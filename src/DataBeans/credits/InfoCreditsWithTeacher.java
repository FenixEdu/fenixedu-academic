package DataBeans.credits;

import DataBeans.InfoTeacherWithPerson;


/**
 * @author Tânia Pousão
 *  
 */
public class InfoCreditsWithTeacher extends InfoCreditsWrapper
{

    /**
     * @param infoCredits
     */
    public InfoCreditsWithTeacher(InfoCredits infoCredits) {
        super(infoCredits);
    }

    private InfoTeacherWithPerson infoTeacher;
    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacherWithPerson getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacherWithPerson infoTeacher) {
        this.infoTeacher = infoTeacher;
    }
}
