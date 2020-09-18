/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.Person;

import pt.ist.fenixframework.Atomic;

public class MarkSheetManagementCreateBean extends MarkSheetManagementBaseBean {

    private String teacherId;
    private Date evaluationDate;
    private EvaluationSeason evaluationSeason;
    private Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans =
            new HashSet<MarkSheetEnrolmentEvaluationBean>();
    private Collection<MarkSheetEnrolmentEvaluationBean> impossibleEnrolmentEvaluationBeans =
            new HashSet<MarkSheetEnrolmentEvaluationBean>();
    
    private transient InputStream inputStream;

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationBeans() {
        return enrolmentEvaluationBeans;
    }

    public void setEnrolmentEvaluationBeans(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans) {
        this.enrolmentEvaluationBeans = enrolmentEvaluationBeans;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getImpossibleEnrolmentEvaluationBeans() {
        return impossibleEnrolmentEvaluationBeans;
    }

    public void setImpossibleEnrolmentEvaluationBeans(
            Collection<MarkSheetEnrolmentEvaluationBean> impossibleEnrolmentEvaluationBeans) {
        this.impossibleEnrolmentEvaluationBeans = impossibleEnrolmentEvaluationBeans;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getAllEnrolmentEvalutionBeans() {
        final Collection<MarkSheetEnrolmentEvaluationBean> result = new ArrayList<MarkSheetEnrolmentEvaluationBean>();
        result.addAll(getEnrolmentEvaluationBeans());
        result.addAll(getImpossibleEnrolmentEvaluationBeans());
        return result;
    }

    @Atomic
    public MarkSheet createMarkSheet(Person person) {
        final Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeanList =
                CollectionUtils.select(getAllEnrolmentEvalutionBeans(), new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        return ((MarkSheetEnrolmentEvaluationBean) arg0).hasAnyGradeValue();
                    }

                });

        return getCurricularCourse().createNormalMarkSheet(getExecutionPeriod(), getTeacher(), getEvaluationDate(),
                getEvaluationSeason(), Boolean.FALSE, enrolmentEvaluationBeanList, person);
    }

    @Atomic
    public MarkSheet createOldMarkSheet(Person person) {
        final Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeanList =
                CollectionUtils.select(getAllEnrolmentEvalutionBeans(), new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        return ((MarkSheetEnrolmentEvaluationBean) arg0).hasAnyGradeValue();
                    }

                });

        return getCurricularCourse().createOldNormalMarkSheet(getExecutionPeriod(), getTeacher(), getEvaluationDate(),
                getEvaluationSeason(), enrolmentEvaluationBeanList, person);
    }
}
