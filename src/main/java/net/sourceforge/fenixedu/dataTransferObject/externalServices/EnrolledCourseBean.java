/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
