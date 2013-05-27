package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DefineInquiryResponsePeriod {

    @Checked("RolePredicates.GEP_PREDICATE")
    @Service
    public static void run(final Integer executionPeriodID, final Date inquiryResponseBegin, final Date inquiryResponseEnd) {
        final ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodID);
        final InquiryResponsePeriod inquiryResponsePeriod = executionSemester.getInquiryResponsePeriod();
        if (inquiryResponsePeriod == null) {
            new InquiryResponsePeriod(executionSemester, inquiryResponseBegin, inquiryResponseEnd);
        } else {
            inquiryResponsePeriod.setPeriod(inquiryResponseBegin, inquiryResponseEnd);
        }
    }

}