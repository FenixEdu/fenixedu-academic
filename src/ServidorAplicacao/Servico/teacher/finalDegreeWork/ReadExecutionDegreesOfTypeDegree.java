package ServidorAplicacao.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * Created on Mar 8, 2004
 *
 */

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ReadExecutionDegreesOfTypeDegree implements IService {

    /**
     *  
     */
    public ReadExecutionDegreesOfTypeDegree() {
        super();
    }

    public List run() throws FenixServiceException {
        List executionDegrees = null;
        List infoExecutionDegrees = new ArrayList();
        try {
            SuportePersistenteOJB suportePersistenteOJB = SuportePersistenteOJB.getInstance();
            executionDegrees = suportePersistenteOJB.getIPersistentExecutionDegree()
                    .readExecutionDegreesOfTypeDegree();

            if (executionDegrees != null) {
                Iterator iterator = executionDegrees.iterator();
                while (iterator.hasNext()) {
                    IExecutionDegree cursoExecucao = (IExecutionDegree) iterator.next();
                    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner
                            .get(cursoExecucao);
                    infoExecutionDegrees.add(infoExecutionDegree);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoExecutionDegrees;
    }
}