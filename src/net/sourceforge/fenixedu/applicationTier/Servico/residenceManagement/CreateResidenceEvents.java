package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceEventBean;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;

public class CreateResidenceEvents extends Service {

    public void run(List<ResidenceEventBean> beans, ResidenceMonth month) {
	for (ResidenceEventBean bean : beans) {
	    new ResidenceEvent(month,bean.getStudent().getPerson(), bean.getRoomValue());
	}
    }
}
