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
        result += "Theme=" + getKeyTheme() + ";";
        result += "Motivation=" + getMotivation() + ";";
        result += "Student=" + getKeyStudent() + ";";
        result += "CaseStudyChoices" + getCaseStudyChoices() + ";";
        result += "CurricularCourse=" + getKeyCurricularCourse() + ";";
        result += "Seminary:=" + getKeySeminary() + ";";
        result += "Modality=" + getKeyModality() + "]";

        return result;
    }

}
