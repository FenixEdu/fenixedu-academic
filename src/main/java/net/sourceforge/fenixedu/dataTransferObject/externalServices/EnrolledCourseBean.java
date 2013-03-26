/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class EnrolledCourseBean {

    private String name;
    private String acronym;
    private String pageURL;
    private List<EnrolledGroupBean> enrolledGroups = new ArrayList<EnrolledGroupBean>();
    private List<EvaluationBean> evaluations = new ArrayList<EvaluationBean>();

    public EnrolledCourseBean(final Attends attend) {
        setName(attend.getExecutionCourse().getName());
        setAcronym(attend.getExecutionCourse().getSigla());
        setPageURL("http://fenix.ist.utl.pt" + attend.getExecutionCourse().getSite().getReversePath());
        //grupos
        for (StudentGroup studentGroup : attend.getAllStudentGroups()) {
            getEnrolledGroups().add(new EnrolledGroupBean(studentGroup, attend));
        }
        //exames e testes
        for (WrittenEvaluation writtenEvaluation : attend.getExecutionCourse().getWrittenEvaluations()) {
            getEvaluations().add(new EvaluationBean(writtenEvaluation));
        }
        //projectos
        for (Project project : attend.getExecutionCourse().getAssociatedProjects()) {
            getEvaluations().add(new EvaluationBean(project));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public void setEnrolledGroups(List<EnrolledGroupBean> enrolledGroups) {
        this.enrolledGroups = enrolledGroups;
    }

    public List<EnrolledGroupBean> getEnrolledGroups() {
        return enrolledGroups;
    }

    public void setEvaluations(List<EvaluationBean> evaluations) {
        this.evaluations = evaluations;
    }

    public List<EvaluationBean> getEvaluations() {
        return evaluations;
    }
}
