package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.Person;

public class CreateSupportRequest extends FenixService {

    public void run(Person person, SupportRequestBean bean) {

	SupportRequestFactory.createSupportRequest(bean.getRequestType(), bean.getRequestContext(), bean.getRequestPriority(),
		person, bean.getResponseEmail(), bean.getSubject(), bean.getMessage(), bean.getErrorLog());
    }

}
