package org.fenixedu.academic.ui.spring.controller.student;

import org.fenixedu.academic.domain.RegistrationProtocolLog;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.bennu.core.domain.Bennu;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.Atomic;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationProtocolsService {

    public List<RegistrationProtocol> getAllRegistrationProtocols() {
        return Bennu.getInstance().getRegistrationProtocolsSet().stream().sorted(RegistrationProtocol.AGREEMENT_COMPARATOR).
                collect(Collectors.toList());
    }

    public List<RegistrationProtocolLog> getAllRegistrationProtocolLogs() {
        return Bennu.getInstance().getRegistrationProtocolLogSet().stream().sorted(RegistrationProtocolLog.COMPARATOR_BY_WHEN_DATETIME).
                collect(Collectors.toList());
    }

    @Atomic
    public void createRegistrationProtocol(RegistrationProtocolBean bean) {
        RegistrationProtocol.create(bean.getCode(), bean.getDescription(), bean.getEnrolmentByStudentAllowed(),
                bean.getPayGratuity(), bean.getAllowsIDCard(), bean.getOnlyAllowedDegreeEnrolment(), bean.getAlien(),
                bean.getExempted(), bean.getMobility(), bean.getMilitary(),
                bean.getForOfficialMobilityReporting(), bean.getAttemptAlmaMatterFromPrecedent());
    }

    @Atomic
    public void editRegistrationProtocol(RegistrationProtocol rp, RegistrationProtocolBean bean) {
        rp.edit(bean.getCode(), bean.getDescription(), bean.getEnrolmentByStudentAllowed(),
                bean.getPayGratuity(), bean.getAllowsIDCard(), bean.getOnlyAllowedDegreeEnrolment(), bean.getAlien(),
                bean.getExempted(), bean.getMobility(), bean.getMilitary(), bean.getForOfficialMobilityReporting(),
                bean.getAttemptAlmaMatterFromPrecedent());
    }

}
