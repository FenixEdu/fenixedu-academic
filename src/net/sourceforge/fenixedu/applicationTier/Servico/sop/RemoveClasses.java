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
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemoveClasses implements IService {

    public Boolean run(InfoShift infoShift, List classOIDs) throws ExcepcaoPersistencia {

        boolean result = false;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                infoShift.getIdInternal());

        sp.getITurnoPersistente().simpleLockWrite(shift);

        for (int i = 0; i < classOIDs.size(); i++) {
            ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                    (Integer) classOIDs.get(i));

            shift.getAssociatedClasses().remove(schoolClass);

            sp.getITurmaPersistente().simpleLockWrite(schoolClass);
            schoolClass.getAssociatedShifts().remove(shift);
        }

        result = true;

        return new Boolean(result);
    }

}