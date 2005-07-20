package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 */
public class SelectClasses implements IService {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurmaPersistente classDAO = sp.getITurmaPersistente();

        List<ISchoolClass> classes = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(
                infoClass.getInfoExecutionPeriod().getIdInternal(), infoClass.getAnoCurricular(),
                infoClass.getInfoExecutionDegree().getIdInternal());

        List<InfoClass> infoClasses = new ArrayList<InfoClass>();
        for (ISchoolClass taux : classes) {
            infoClasses.add(InfoClass.newInfoFromDomain(taux));
        }

        return infoClasses;
    }

}
