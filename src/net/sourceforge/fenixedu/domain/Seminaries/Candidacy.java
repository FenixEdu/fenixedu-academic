/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;


/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class Candidacy extends Candidacy_Base {

    public String toString() {
        String result = "[Candidacy:";
        result += "Theme=" + getThemeIdInternal() + ";";
        result += "Motivation=" + getMotivation() + ";";
        result += "Student=" + getStudentIdInternal() + ";";
        result += "CaseStudyChoices" + getCaseStudyChoices() + ";";
        result += "CurricularCourse=" + getCurricularCourseIdInternal() + ";";
        result += "Seminary:=" + getSeminaryIdInternal() + ";";
        result += "Modality=" + getModalityIdInternal() + "]";

        return result;
    }

}