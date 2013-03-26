package net.sourceforge.fenixedu.domain.inquiries;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;

public class InquiryStudentCycleAnswer extends InquiryStudentCycleAnswer_Base {

    public InquiryStudentCycleAnswer(Registration registration) {
        super();
        setRegistration(registration);
    }

    public InquiryStudentCycleAnswer(PhdIndividualProgramProcess phdProcess) {
        super();
        setPhdProcess(phdProcess);
    }

    @ConsistencyPredicate
    public boolean checkHasRegistrationOrHasPhd() {
        return hasRegistration() || hasPhdProcess();
    }
}
