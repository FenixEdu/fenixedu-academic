package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaTurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadClassesWithShiftService implements IService {

    public Object run(InfoShift infoShift) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurmaTurnoPersistente classShiftDAO = sp.getITurmaTurnoPersistente();

        final List<ISchoolClassShift> shiftClasses = classShiftDAO.readClassesWithShift(infoShift
                .getIdInternal());

        List<InfoClass> infoClasses = new ArrayList<InfoClass>();
        for (ISchoolClassShift element : shiftClasses) {
            infoClasses.add(InfoClass.newInfoFromDomain(element.getTurma()));
        }
        return infoClasses;
    }

}
