package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceEventBean;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import pt.ist.fenixWebFramework.services.Service;

public class CreateResidenceEvents extends FenixService {

    @Service
    public static void run(List<ResidenceEventBean> beans, ResidenceMonth month) {
        for (ResidenceEventBean bean : beans) {
            if (!month.isEventPresent(bean.getStudent().getPerson())) {
                new ResidenceEvent(month, bean.getStudent().getPerson(), bean.getRoomValue(), bean.getRoom());
            }
        }
    }
}