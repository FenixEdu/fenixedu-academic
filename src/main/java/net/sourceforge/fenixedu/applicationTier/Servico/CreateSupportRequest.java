package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class CreateSupportRequest {

    @Atomic
    public static void run(Person person, SupportRequestBean bean) {

        SupportRequestFactory.createSupportRequest(bean.getRequestType(), bean.getRequestContext(), bean.getRequestPriority(),
                person, bean.getResponseEmail(), bean.getSubject(), bean.getMessage(), bean.getErrorLog());
    }

}