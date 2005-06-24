/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ProfessionalCareer extends ProfessionalCareer_Base {

    public ProfessionalCareer() {
        super();
        setOjbConcreteClass(ProfessionalCareer.class.getName());
    }
    
    public String toString() {
        String result = "[Dominio.teacher.ProfessionalCareer ";
        result += ", beginYear=" + getBeginYear();
        result += ", endYear=" + getEndYear();
        result += ", entity=" + getEntity();
        result += ", function=" + getFunction();
        result += ", teacher=" + getTeacher();
        result += "]";
        return result;
    }

}
