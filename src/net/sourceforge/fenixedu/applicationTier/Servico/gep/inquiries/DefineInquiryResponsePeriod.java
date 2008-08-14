package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DefineInquiryResponsePeriod extends Service {

    public void run(final Integer executionPeriodID, final Date inquiryResponseBegin, final Date inquiryResponseEnd) {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	final InquiryResponsePeriod inquiryResponsePeriod = executionSemester.getInquiryResponsePeriod();
	if (inquiryResponsePeriod == null) {
	    new InquiryResponsePeriod(executionSemester, inquiryResponseBegin, inquiryResponseEnd);
	} else {
	    inquiryResponsePeriod.setPeriod(inquiryResponseBegin, inquiryResponseEnd);
	}
    }

}
