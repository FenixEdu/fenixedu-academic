package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteVigilantGroupByOID extends Service {

    public void run(Integer idInternal) throws ExcepcaoPersistencia {

        VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(
                VigilantGroup.class, idInternal);
        group.delete();

    }

}
