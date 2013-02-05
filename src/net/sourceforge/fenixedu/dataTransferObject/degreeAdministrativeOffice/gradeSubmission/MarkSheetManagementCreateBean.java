package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.services.Service;

public class MarkSheetManagementCreateBean extends MarkSheetManagementBaseBean {

    private String teacherId;
    private Date evaluationDate;
    private MarkSheetType markSheetType;
    private Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans =
            new HashSet<MarkSheetEnrolmentEvaluationBean>();
    private Collection<MarkSheetEnrolmentEvaluationBean> impossibleEnrolmentEvaluationBeans =
            new HashSet<MarkSheetEnrolmentEvaluationBean>();

    public MarkSheetType getMarkSheetType() {
        return markSheetType;
    }

    public void setMarkSheetType(MarkSheetType markSheetType) {
        this.markSheetType = markSheetType;
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

    public Collection<MarkSheetEnrolmentEvaluationBean> getAllEnrolmentEvalutionBeans() {
        final Collection<MarkSheetEnrolmentEvaluationBean> result = new ArrayList<MarkSheetEnrolmentEvaluationBean>();
        result.addAll(getEnrolmentEvaluationBeans());
        result.addAll(getImpossibleEnrolmentEvaluationBeans());
        return result;
    }

    @Service
    public MarkSheet createMarkSheet(Person person) {
        final Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeanList =
                CollectionUtils.select(getAllEnrolmentEvalutionBeans(), new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        return ((MarkSheetEnrolmentEvaluationBean) arg0).hasAnyGradeValue();
                    }

                });

        return getCurricularCourse().createNormalMarkSheet(getExecutionPeriod(), getTeacher(), getEvaluationDate(),
                getMarkSheetType(), Boolean.FALSE, enrolmentEvaluationBeanList, person);
    }

    @Service
    public MarkSheet createOldMarkSheet(Person person) {
        final Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeanList =
                CollectionUtils.select(getAllEnrolmentEvalutionBeans(), new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        return ((MarkSheetEnrolmentEvaluationBean) arg0).hasAnyGradeValue();
                    }

                });

        return getCurricularCourse().createOldNormalMarkSheet(getExecutionPeriod(), getTeacher(), getEvaluationDate(),
                getMarkSheetType(), enrolmentEvaluationBeanList, person);
    }
}
