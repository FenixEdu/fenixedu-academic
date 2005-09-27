package net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment;

import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class DisplayEvaluationsForStudentToEnrol extends FenixBackingBean {

    private IStudent student = null;

    private static final ComparatorChain comparator = new ComparatorChain();
    static {
        comparator.addComparator(new BeanComparator("dayDate"));
        comparator.addComparator(new BeanComparator("beginningDate"));
    }

    public List<IExam> getEnroledExams() throws FenixFilterException, FenixServiceException {
        List<IExam> enroledExams = getStudent().getEnroledExams();
        Collections.sort(enroledExams, comparator);
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
        return enroledWrittenTests;
    }

    public List<IWrittenTest> getUnenroledWrittenTests() throws FenixFilterException,
            FenixServiceException {

        List<IWrittenTest> unenroledWrittenTests = getStudent().getUnenroledWrittenTests();
        Collections.sort(unenroledWrittenTests, comparator);
        return unenroledWrittenTests;
    }

    public String enrolStudent() throws FenixFilterException, FenixServiceException {
        final Integer evaluationID = Integer.valueOf(getRequestParameter("evaluationID"));
        // Use another service that uses student ID ?!?!??!
        final Object args[] = { getUserView().getUtilizador(), evaluationID };
        ServiceUtils.executeService(getUserView(), "EnrolStudentInWrittenEvaluation", args);
        return "success";
    }
    
    public String unenrolStudent() throws FenixFilterException, FenixServiceException {
        final Integer evaluationID = Integer.valueOf(getRequestParameter("evaluationID"));
        final Object args[] = { getUserView().getUtilizador(), evaluationID };
        ServiceUtils.executeService(getUserView(), "UnEnrollStudentInWrittenEvaluation", args);
        return "success";
    }

    private IStudent getStudent() throws FenixFilterException, FenixServiceException {
        if (student == null) {
            final Object args[] = { getUserView().getUtilizador() };
            student = (IStudent) ServiceUtils.executeService(getUserView(),
                    "ReadStudentByUsernameForEvaluationEnrolment", args);
        }
        return student;
    }
}
