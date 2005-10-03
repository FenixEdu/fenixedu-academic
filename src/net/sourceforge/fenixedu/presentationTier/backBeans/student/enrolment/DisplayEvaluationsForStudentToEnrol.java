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
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class DisplayEvaluationsForStudentToEnrol extends FenixBackingBean {

    private IStudent student = null;
    private List<IExam> unenroledExams = null;;
    private List<IExam> enroledExams = null;
    private List<IWrittenTest> enroledWrittenTests = null;
    private List<IWrittenTest> unenroledWrittenTests = null;

    private Map<Integer, Boolean> renderUnenrolLinks = new HashMap<Integer, Boolean>();
    private Map<Integer, String> enroledRooms = new HashMap<Integer, String>();
    private Map<Integer, List<IExecutionCourse>> attendingExecutionCourses = new HashMap<Integer, List<IExecutionCourse>>();

    private static final ComparatorChain comparator = new ComparatorChain();
    static {
        comparator.addComparator(new BeanComparator("dayDate"));
        comparator.addComparator(new BeanComparator("beginningDate"));
    }

    public List<IExam> getEnroledExams() throws FenixFilterException, FenixServiceException {
        if (enroledExams == null) {
            enroledExams = getStudent().getEnroledExams();
            Collections.sort(enroledExams, comparator);
            cleanMaps();
            for (final IExam exam : enroledExams) {
                final IWrittenEvaluationEnrolment writtenEvaluationEnrolment = exam
                        .getWrittenEvaluationEnrolmentFor(getStudent());
                renderUnenrolLinks.put(exam.getIdInternal(), exam.isInEnrolmentPeriod());
                enroledRooms.put(exam.getIdInternal(),
                                (writtenEvaluationEnrolment != null && 
                                 writtenEvaluationEnrolment.getRoom() != null) 
                                 ? writtenEvaluationEnrolment.getRoom().getNome() : " ");
                attendingExecutionCourses.put(exam.getIdInternal(), 
                        exam.getAttendingExecutionCoursesFor(getStudent()));
            }
        }
        return enroledExams;
    }

    public List<IExam> getUnenroledExams() throws FenixFilterException, FenixServiceException {
        if (unenroledExams == null) {
            unenroledExams = getStudent().getUnenroledExams();
            Collections.sort(unenroledExams, comparator);
            attendingExecutionCourses.clear();
            for (final IExam exam : unenroledExams) {            
                attendingExecutionCourses.put(exam.getIdInternal(), 
                        exam.getAttendingExecutionCoursesFor(getStudent()));
            }
        }
        return unenroledExams;
    }

    public List<IWrittenTest> getEnroledWrittenTests() throws FenixFilterException,
            FenixServiceException {
        if (enroledWrittenTests == null) {
            enroledWrittenTests = getStudent().getEnroledWrittenTests();
            Collections.sort(enroledWrittenTests, comparator);
            cleanMaps();
            for (final IWrittenTest writtenTest : enroledWrittenTests) {
                final IWrittenEvaluationEnrolment writtenEvaluationEnrolment = writtenTest
                        .getWrittenEvaluationEnrolmentFor(getStudent());
                renderUnenrolLinks.put(writtenTest.getIdInternal(), writtenTest.isInEnrolmentPeriod());
                enroledRooms.put(writtenTest.getIdInternal(),
                                (writtenEvaluationEnrolment != null && 
                                 writtenEvaluationEnrolment.getRoom() != null) 
                                 ? writtenEvaluationEnrolment.getRoom().getNome() : " ");
                attendingExecutionCourses.put(writtenTest.getIdInternal(), 
                        writtenTest.getAttendingExecutionCoursesFor(getStudent()));
            }
        }
        return enroledWrittenTests;
    }

    public List<IWrittenTest> getUnenroledWrittenTests() throws FenixFilterException,
            FenixServiceException {
        if (unenroledWrittenTests == null) {
            unenroledWrittenTests = getStudent().getUnenroledWrittenTests();
            Collections.sort(unenroledWrittenTests, comparator);
            attendingExecutionCourses.clear();
            for (final IWrittenTest writtenTest : unenroledWrittenTests) {            
                attendingExecutionCourses.put(writtenTest.getIdInternal(), 
                        writtenTest.getAttendingExecutionCoursesFor(getStudent()));
            }
        }       
        return unenroledWrittenTests;
    }

    public void enrolStudent(ActionEvent actionEvent) throws FenixFilterException, FenixServiceException {
        final UIParameter parameter = (UIParameter) actionEvent.getComponent().findComponent(
                "evaluationID");
        final Integer evaluationID = Integer.valueOf(parameter.getValue().toString());
        final Object args[] = { getUserView().getUtilizador(), evaluationID };
        ServiceUtils.executeService(getUserView(), "EnrolStudentInWrittenEvaluation", args);
        cleanBeanProperties();
    }

    public void unenrolStudent(ActionEvent actionEvent) throws FenixFilterException,
            FenixServiceException {
        final UIParameter parameter = (UIParameter) actionEvent.getComponent().findComponent(
                "evaluationID");
        final Integer evaluationID = Integer.valueOf(parameter.getValue().toString());
        final Object args[] = { getUserView().getUtilizador(), evaluationID };
        ServiceUtils.executeService(getUserView(), "UnEnrollStudentInWrittenEvaluation", args);
        cleanBeanProperties();
    }

    private IStudent getStudent() throws FenixFilterException, FenixServiceException {
        if (student == null) {
            final Object args[] = { getUserView().getUtilizador() };
            student = (IStudent) ServiceUtils.executeService(getUserView(),
                    "ReadStudentByUsernameForEvaluationEnrolment", args);
        }
        return student;
    }
    
    private void cleanBeanProperties() {
        this.unenroledExams = null;
        this.enroledExams = null;
        this.enroledWrittenTests = null;
        this.unenroledWrittenTests = null;
    }
    
    private void cleanMaps() {
        this.renderUnenrolLinks.clear();
        this.enroledRooms.clear();
        this.attendingExecutionCourses.clear();
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

    public Map<Integer, List<IExecutionCourse>> getAttendingExecutionCourses() {
        return attendingExecutionCourses;
    }

    public void setAttendingExecutionCourses(Map<Integer, List<IExecutionCourse>> attendingExecutionCourses) {
        this.attendingExecutionCourses = attendingExecutionCourses;
    }
}
