package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 */
public class SelectShifts implements IService {

    public Object run(InfoShift infoShift) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Shift shift = (Shift) sp.getITurnoPersistente().readByOID(Shift.class,
                infoShift.getIdInternal());

        final List<Shift> shifts = sp.getITurnoPersistente().readByExecutionCourse(
                shift.getDisciplinaExecucao().getIdInternal());

        List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        for (Shift taux : shifts) {
            infoShifts.add(InfoShift.newInfoFromDomain(taux));
        }

        return infoShifts;
    }

}
