package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.IExecutionDegree;
import Dominio.IExecutionPeriod;
import Dominio.ISchoolClass;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class SelectClasses implements IService {

    private static SelectClasses _servico = new SelectClasses();

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        List classes = new ArrayList();
        List infoClasses = new ArrayList();

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

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