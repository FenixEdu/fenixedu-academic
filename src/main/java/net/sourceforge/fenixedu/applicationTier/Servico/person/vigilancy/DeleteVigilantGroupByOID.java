package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteVigilantGroupByOID {

    @Service
    public static void run(Integer externalId) {

        VigilantGroup group = (VigilantGroup) AbstractDomainObject.fromExternalId(VigilantGroup.class, externalId);
        group.delete();

    }

}