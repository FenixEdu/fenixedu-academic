/*
 * Created on 28/Jan/2004
 */
package DataBeans.teacher.credits;

import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherCreditsDetailsDTO extends InfoTeacher
{
    private InfoCredits infoCredits;
    
    /**
     * @param infoTeacher
     */
    public TeacherCreditsDetailsDTO(InfoTeacher infoTeacher)
    {
        super.setIdInternal(infoTeacher.getIdInternal());
        super.setInfoCategory(infoTeacher.getInfoCategory());
        super.setInfoPerson(infoTeacher.getInfoPerson());
        super.setProfessorShipsExecutionCourses(infoTeacher.getProfessorShipsExecutionCourses());
        super.setResponsibleForExecutionCourses(infoTeacher.getResponsibleForExecutionCourses());
        super.setTeacherNumber(infoTeacher.getTeacherNumber());
    }


    /**
     * @return Returns the infoCredits.
     */
    public InfoCredits getInfoCredits()
    {
        return infoCredits;
    }

    /**
     * @param infoCredits The infoCredits to set.
     */
    public void setInfoCredits(InfoCredits infoCredits)
    {
        this.infoCredits = infoCredits;
    }

}
