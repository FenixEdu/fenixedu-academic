package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.presentationTier.Action.person.ModifiedContentBean;
import pt.ist.fenixframework.Atomic;

public class ApplyStructureModifications {

    @Atomic
    public static void run(List<ModifiedContentBean> modifications) {
        ServiceMonitoring.logService(ApplyStructureModifications.class, modifications);
        for (ModifiedContentBean bean : modifications) {
            bean.getContent().applyStructureModifications(bean.getNewParent(), bean.getOrder());
        }
    }

}
