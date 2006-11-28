package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.person.InvitedPersonBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;

public class CreateNewPersonInvitation extends Service {

    public void run(InvitedPersonBean bean) {
	new Invitation(bean.getInvitedPerson(), bean.getUnit(), bean.getResponsible(), bean.getBegin(), bean.getEnd());
    }    
}
