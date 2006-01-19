/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class RemoveClasses extends Service {

    public Boolean run(InfoShift infoShift, List classOIDs) throws ExcepcaoPersistencia {

        boolean result = false;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Shift shift = (Shift) sp.getITurnoPersistente().readByOID(Shift.class,
                infoShift.getIdInternal());

        for (int i = 0; i < classOIDs.size(); i++) {
            SchoolClass schoolClass = (SchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                    (Integer) classOIDs.get(i));

            shift.getAssociatedClasses().remove(schoolClass);

            schoolClass.getAssociatedShifts().remove(shift);
        }

        result = true;

        return new Boolean(result);
    }

}