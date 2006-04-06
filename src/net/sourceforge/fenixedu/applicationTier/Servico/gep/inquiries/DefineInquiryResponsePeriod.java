package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DefineInquiryResponsePeriod extends Service {

    public void run(final Integer executionPeriodID, final Date inquiryResponseBegin, final Date inquiryResponseEnd) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        executionPeriod.setInquiryResponseBegin(inquiryResponseBegin);
        executionPeriod.setInquiryResponseEnd(inquiryResponseEnd);
    }

}
