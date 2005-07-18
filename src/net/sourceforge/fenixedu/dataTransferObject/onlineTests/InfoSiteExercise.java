/*
 * Created on 23/Jul/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;

/**
 * @author Susana Fernandes
 */

public class InfoSiteExercise extends DataTranferObject implements ISiteComponent {
    private InfoMetadata infoMetadata;

    private List questionNames;

    private InfoExecutionCourse executionCourse;

    public InfoSiteExercise() {
    }

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public InfoMetadata getInfoMetadata() {
        return infoMetadata;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public void setInfoMetadata(InfoMetadata metadata) {
        infoMetadata = metadata;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteExercise) {
            InfoSiteExercise infoSiteMetadata = (InfoSiteExercise) obj;
            result = getExecutionCourse().equals(infoSiteMetadata.getExecutionCourse())
                    && getInfoMetadata().equals(infoSiteMetadata.getInfoMetadata())
                    && getQuestionNames().contains(infoSiteMetadata.getQuestionNames())
                    && infoSiteMetadata.getQuestionNames().containsAll(getQuestionNames());
        }
        return result;
    }

    public List getQuestionNames() {
        return questionNames;
    }

    public void setQuestionNames(List questionNames) {
        this.questionNames = questionNames;
    }
}