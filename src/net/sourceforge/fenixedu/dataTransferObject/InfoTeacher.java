/*
 * Created on 12/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author João Mota
 */
public class InfoTeacher extends InfoObject implements ISmsDTO {

    private Integer teacherNumber;

    private InfoPerson infoPerson;

    private InfoCategory infoCategory;

    private List professorShipsExecutionCourses;

    private List responsibleForExecutionCourses;

    /**
     *  
     */
    public InfoTeacher() {

    }

    public InfoTeacher(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public InfoTeacher(Integer teacherNumber, InfoPerson infoPerson) {
        setTeacherNumber(teacherNumber);
        setInfoPerson(infoPerson);
    }

    public InfoTeacher(Integer teacherNumber, List professorShipsExecutionCourses,
            List responsibleForExecutionCourses, InfoPerson infoPerson) {
        setTeacherNumber(teacherNumber);
        setProfessorShipsExecutionCourses(professorShipsExecutionCourses);
        setResponsibleForExecutionCourses(responsibleForExecutionCourses);
        setInfoPerson(infoPerson);
    }

    /**
     * @return List
     */
    public List getProfessorShipsExecutionCourses() {
        return professorShipsExecutionCourses;
    }

    /**
     * @return List
     */
    public List getResponsibleForExecutionCourses() {
        return responsibleForExecutionCourses;
    }

    /**
     * @return Integer
     */
    public Integer getTeacherNumber() {
        return teacherNumber;
    }

    /**
     * Sets the professorShipsExecutionCourses.
     * 
     * @param professorShipsExecutionCourses
     *            The professorShipsExecutionCourses to set
     */
    public void setProfessorShipsExecutionCourses(List professorShipsExecutionCourses) {
        this.professorShipsExecutionCourses = professorShipsExecutionCourses;
    }

    /**
     * Sets the responsibleForExecutionCourses.
     * 
     * @param responsibleForExecutionCourses
     *            The responsibleForExecutionCourses to set
     */
    public void setResponsibleForExecutionCourses(List responsibleForExecutionCourses) {
        this.responsibleForExecutionCourses = responsibleForExecutionCourses;
    }

    /**
     * Sets the teacherNumber.
     * 
     * @param teacherNumber
     *            The teacherNumber to set
     */
    public void setTeacherNumber(Integer teacherNumber) {
        this.teacherNumber = teacherNumber;
    }

    /**
     * @return InfoPerson
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @return InfoCategory
     */
    public InfoCategory getInfoCategory() {
        return infoCategory;
    }

    /**
     * Sets the person.
     * 
     * @param person
     *            The person to set
     */
    public void setInfoPerson(InfoPerson person) {
        this.infoPerson = person;
    }

    /**
     * Sets the category.
     * 
     * @param category
     */
    public void setInfoCategory(InfoCategory category) {
        this.infoCategory = category;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoTeacher) {
            InfoTeacher infoTeacher = (InfoTeacher) obj;

            result = ((infoTeacher != null)
                    && (getTeacherNumber().equals(infoTeacher.getTeacherNumber())) && (getInfoPerson()
                    .equals(infoTeacher.getInfoPerson())));
        }
        return result;
    }

    public String toString() {
        String result = "[INFOTEACHER";
        result += ", number=" + this.teacherNumber;
        // This throws a null poiter exception when infoPerson is not set!
        //result += ", nome=" + this.infoPerson.getNome();
        result += "]";
        return result;
    }

    public void copyFromDomain(ITeacher teacher) {
        if (teacher != null) {
            super.copyFromDomain(teacher);
            setTeacherNumber(teacher.getTeacherNumber());
            setInfoPerson(InfoPerson.newInfoFromDomain(teacher.getPerson()));
            setInfoCategory(InfoCategory.newInfoFromDomain(teacher.getCategory()));
        }
    }

    /**
     * @param teacher
     * @return
     */
    public static InfoTeacher newInfoFromDomain(ITeacher teacher) {
        InfoTeacher infoTeacher = null;
        if (teacher != null) {
            infoTeacher = new InfoTeacher();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }

    public void copyToDomain(InfoTeacher infoTeacher, ITeacher teacher) {
        super.copyToDomain(infoTeacher, teacher);
        teacher.setTeacherNumber(infoTeacher.getTeacherNumber());
    }

    public static ITeacher newDomainFromInfo(InfoTeacher infoTeacher) {
        ITeacher teacher = null;
        if (infoTeacher != null) {
            teacher = new Teacher();
            infoTeacher.copyToDomain(infoTeacher, teacher);
        }
        return teacher;
    }

    public String toSmsText() {

        return "Nome: " + this.infoPerson.getNome() + " Numero: " + this.teacherNumber;
    }
}