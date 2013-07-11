package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteVigilantGroupByOID {

    @Service
    public static void run(String externalId) {

        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(externalId);
        group.delete();

    }
}