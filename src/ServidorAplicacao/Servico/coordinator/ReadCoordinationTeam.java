/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCoordinator;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * fenix-head ServidorAplicacao.Servico.fileManager
 * 
 * @author João Mota 17/Set/2003
 *  
 */
public class ReadCoordinationTeam implements IServico {

    private static ReadCoordinationTeam service = new ReadCoordinationTeam();

    public static ReadCoordinationTeam getService() {

        return service;
    }

    private ReadCoordinationTeam() {

    }

    public final String getNome() {

        return "ReadCoordinationTeam";
    }

    public List run(Integer executionDegreeId) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCoordinator persistentCoordinator = sp
                    .getIPersistentCoordinator();
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();

            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                    .readByOID(CursoExecucao.class, executionDegreeId);
            if (executionDegree == null) {
                throw new InvalidArgumentsServiceException(
                        "execution Degree unexisting");
            }
            List coordinators = persistentCoordinator
                    .readCoordinatorsByExecutionDegree(executionDegree);
            Iterator iterator = coordinators.iterator();
            List infoCoordinators = new ArrayList();
            while (iterator.hasNext()) {
                ICoordinator coordinator = (ICoordinator) iterator.next();
                InfoCoordinator infoCoordinator = Cloner
                        .copyICoordinator2InfoCoordenator(coordinator);
                infoCoordinators.add(infoCoordinator);
            }

            return infoCoordinators;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}