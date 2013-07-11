package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteVigilantGroupByOID {

    @Atomic
    public static void run(String externalId) {

        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(externalId);
        group.delete();

    }
}