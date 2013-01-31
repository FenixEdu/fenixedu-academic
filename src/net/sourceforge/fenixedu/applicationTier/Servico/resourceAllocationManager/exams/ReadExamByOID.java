package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExamByOID extends FenixService {

	@Service
	public static InfoExam run(Integer examID) throws FenixServiceException {
		final Exam exam = (Exam) rootDomainObject.readEvaluationByOID(examID);
		if (exam == null) {
			throw new FenixServiceException("error.noExam");
		}
		return InfoExam.newInfoFromDomain(exam);
	}
}