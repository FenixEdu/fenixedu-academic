package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteVigilantGroupByOID {

    @Service
    public static void run(Integer idInternal) {

        VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, idInternal);
        group.delete();

    }

}