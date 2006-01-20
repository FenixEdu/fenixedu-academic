package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Mota
 * 
 */
public class SelectShifts extends Service {

    public Object run(InfoShift infoShift) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Shift shift = (Shift) persistentSupport.getITurnoPersistente().readByOID(Shift.class,
                infoShift.getIdInternal());

        final List<Shift> shifts = persistentSupport.getITurnoPersistente().readByExecutionCourse(
                shift.getDisciplinaExecucao().getIdInternal());

        List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        for (Shift taux : shifts) {
            infoShifts.add(InfoShift.newInfoFromDomain(taux));
        }

        return infoShifts;
    }

}
