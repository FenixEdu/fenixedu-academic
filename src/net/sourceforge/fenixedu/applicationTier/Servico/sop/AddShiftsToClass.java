/*
 *
 * Created on 2003/08/13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AddShiftsToClass implements IService {

    public Boolean run(InfoClass infoClass, List shiftOIDs) throws FenixServiceException,
            ExcepcaoPersistencia {

        boolean result = false;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                infoClass.getIdInternal());
        sp.getITurmaPersistente().simpleLockWrite(schoolClass);

        for (int i = 0; i < shiftOIDs.size(); i++) {
            IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                    (Integer) shiftOIDs.get(i));

            schoolClass.associateShift(shift);
        }

        result = true;

        return new Boolean(result);
    }

}