/*
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarTurma
 * 
 * @author tfc130
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ITurma;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CriarTurma implements IService {

    /**
     * The actor of this class.
     */
    public CriarTurma() {
    }

    public Object run(InfoClass infoTurma) throws FenixServiceException {

        ITurma turma = null;
        InfoClass infoClass = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            turma = Cloner.copyInfoClass2Class(infoTurma);

            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            try {
                sp.getITurmaPersistente().simpleLockWrite(turma);
                turma.setExecutionDegree(executionDegreeDAO.readByDegreeCurricularPlanAndExecutionYear(
                        turma.getExecutionDegree().getCurricularPlan(), turma.getExecutionDegree()
                                .getExecutionYear()));

                turma.setExecutionPeriod(executionPeriodDAO.readByNameAndExecutionYear(turma
                        .getExecutionPeriod().getName(), turma.getExecutionPeriod().getExecutionYear()));

                infoClass = Cloner.copyClass2InfoClass(turma);
            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return infoClass;
    }

}