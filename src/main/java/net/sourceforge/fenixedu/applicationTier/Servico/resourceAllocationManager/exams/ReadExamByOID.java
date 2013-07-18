package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExamByOID {

    @Service
    public static InfoExam run(Integer examID) throws FenixServiceException {
        final Exam exam = (Exam) RootDomainObject.getInstance().readEvaluationByOID(examID);
        if (exam == null) {
            throw new FenixServiceException("error.noExam");
        }
        return InfoExam.newInfoFromDomain(exam);
    }
}