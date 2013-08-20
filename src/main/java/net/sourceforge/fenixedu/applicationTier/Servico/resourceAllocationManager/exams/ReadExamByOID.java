package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExamByOID {

    @Service
    public static InfoExam run(String examID) throws FenixServiceException {
        final Exam exam = (Exam) AbstractDomainObject.fromExternalId(examID);
        if (exam == null) {
            throw new FenixServiceException("error.noExam");
        }
        return InfoExam.newInfoFromDomain(exam);
    }
}