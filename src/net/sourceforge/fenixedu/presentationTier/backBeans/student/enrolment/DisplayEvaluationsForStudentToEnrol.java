package net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class DisplayEvaluationsForStudentToEnrol extends FenixBackingBean {

    private IStudent student = null;
    private Map<Integer, Boolean> renderUnenrolLinks = new HashMap<Integer, Boolean>();
    private Map<Integer, String> enroledRooms = new HashMap<Integer, String>();

    private static final ComparatorChain comparator = new ComparatorChain();
    static {
        comparator.addComparator(new BeanComparator("dayDate"));
        comparator.addComparator(new BeanComparator("beginningDate"));
    }

    public List<IExam> getEnroledExams() throws FenixFilterException, FenixServiceException {
        List<IExam> enroledExams = getStudent().getEnroledExams();
        Collections.sort(enroledExams, comparator);
        renderUnenrolLinks.clear();
        enroledRooms.clear();
        for (final IExam exam : enroledExams) {
            final IWrittenEvaluationEnrolment writtenEvaluationEnrolment = exam
                    .getWrittenEvaluationEnrolmentFor(getStudent());
            if (writtenEvaluationEnrolment != null && writtenEvaluationEnrolment.getRoom() != null) {
                renderUnenrolLinks.put(exam.getIdInternal(), false);
                enroledRooms.put(exam.getIdInternal(), writtenEvaluationEnrolment.getRoom().getNome());
            } else {
                renderUnenrolLinks.put(exam.getIdInternal(), exam.isInEnrolmentPeriod());
                enroledRooms.put(exam.getIdInternal(), " ");
            }
        }
        return enroledExams;
    }

    public List<IExam> getUnenroledExams() throws FenixFilterException, FenixServiceException {
        List<IExam> unenroledExams = getStudent().getUnenroledExams();
        Collections.sort(unenroledExams, comparator);
        return unenroledExams;
    }

    public List<IWrittenTest> getEnroledWrittenTests() throws FenixFilterException,
            FenixServiceException {
        List<IWrittenTest> enroledWrittenTests = getStudent().getEnroledWrittenTests();
        Collections.sort(enroledWrittenTests, comparator);
        renderUnenrolLinks.clear();
        enroledRooms.clear();
        for (final IWrittenTest writtenTest : enroledWrittenTests) {
            final IWrittenEvaluationEnrolment writtenEvaluationEnrolment = writtenTest
                    .getWrittenEvaluationEnrolmentFor(getStudent());
            if (writtenEvaluationEnrolment != null && writtenEvaluationEnrolment.getRoom() != null) {
                renderUnenrolLinks.put(writtenTest.getIdInternal(), false);
                enroledRooms.put(writtenTest.getIdInternal(), writtenEvaluationEnrolment.getRoom().getNome());
            } else {
                renderUnenrolLinks.put(writtenTest.getIdInternal(), writtenTest.isInEnrolmentPeriod());
                enroledRooms.put(writtenTest.getIdInternal(), " ");
            }
        }
        return enroledWrittenTests;
    }

    public List<IWrittenTest> getUnenroledWrittenTests() throws FenixFilterException,
            FenixServiceException {

        List<IWrittenTest> unenroledWrittenTests = getStudent().getUnenroledWrittenTests();
        Collections.sort(unenroledWrittenTests, comparator);
        return unenroledWrittenTests;
    }

    public void enrolStudent(ActionEvent actionEvent) throws FenixFilterException, FenixServiceException {
        final UIParameter parameter = (UIParameter) actionEvent.getComponent().findComponent(
                "evaluationID");
        final Integer evaluationID = Integer.valueOf(parameter.getValue().toString());
        final Object args[] = { getUserView().getUtilizador(), evaluationID };
        ServiceUtils.executeService(getUserView(), "EnrolStudentInWrittenEvaluation", args);
    }

    public void unenrolStudent(ActionEvent actionEvent) throws FenixFilterException,
            FenixServiceException {
        final UIParameter parameter = (UIParameter) actionEvent.getComponent().findComponent(
                "evaluationID");
        final Integer evaluationID = Integer.valueOf(parameter.getValue().toString());
        final Object args[] = { getUserView().getUtilizador(), evaluationID };
        ServiceUtils.executeService(getUserView(), "UnEnrollStudentInWrittenEvaluation", args);
    }

    private IStudent getStudent() throws FenixFilterException, FenixServiceException {
        if (student == null) {
            final Object args[] = { getUserView().getUtilizador() };
            student = (IStudent) ServiceUtils.executeService(getUserView(),
                    "ReadStudentByUsernameForEvaluationEnrolment", args);
        }
        return student;
    }

    public Map<Integer, Boolean> getRenderUnenrolLinks() {
        return renderUnenrolLinks;
    }

    public void setRenderUnenrolLinks(Map<Integer, Boolean> renderUnenrolLinks) {
        this.renderUnenrolLinks = renderUnenrolLinks;
    }

    public Map<Integer, String> getEnroledRooms() {
        return enroledRooms;
    }

    public void setEnroledRooms(Map<Integer, String> enroledRooms) {
        this.enroledRooms = enroledRooms;
    }
}
