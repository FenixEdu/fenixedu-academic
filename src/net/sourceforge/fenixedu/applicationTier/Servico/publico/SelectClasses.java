package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
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

        List classes = new ArrayList();
        List infoClasses = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ITurmaPersistente classDAO = sp.getITurmaPersistente();

        IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass
                .getInfoExecutionPeriod());
        IExecutionDegree executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass
                .getInfoExecutionDegree());

        classes = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(executionPeriod,
                infoClass.getAnoCurricular(), executionDegree);

        for (int i = 0; i < classes.size(); i++) {
            ISchoolClass taux = (ISchoolClass) classes.get(i);
            infoClasses.add(Cloner.copyClass2InfoClass(taux));
        }

        return infoClasses;

    }

}