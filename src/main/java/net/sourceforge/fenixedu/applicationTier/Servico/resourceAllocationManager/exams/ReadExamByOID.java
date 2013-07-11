package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExamByOID {

    @Atomic
    public static InfoExam run(String examID) throws FenixServiceException {
        final Exam exam = (Exam) FenixFramework.getDomainObject(examID);
        if (exam == null) {
            throw new FenixServiceException("error.noExam");
        }
        return InfoExam.newInfoFromDomain(exam);
    }
}