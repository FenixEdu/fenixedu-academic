package ServidorAplicacao.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.List;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 5/Nov/2003
 */
public class ReadActiveDegreeCurricularPlanByExecutionDegreeCode extends
        ReadDegreeCurricularPlanBaseService {

    private static ReadActiveDegreeCurricularPlanByExecutionDegreeCode service = new ReadActiveDegreeCurricularPlanByExecutionDegreeCode();

    public static ReadActiveDegreeCurricularPlanByExecutionDegreeCode getService() {

        return service;
    }

    private ReadActiveDegreeCurricularPlanByExecutionDegreeCode() {

    }

    public final String getNome() {

        return "ReadActiveDegreeCurricularPlanByExecutionDegreeCode";
    }

    public List run(Integer executionDegreeCode) throws FenixServiceException {

        ISuportePersistente sp;
        ICursoExecucao executionDegree;
        try {
            sp = SuportePersistenteOJB.getInstance();

            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();
            if (executionDegreeCode == null) {
                throw new FenixServiceException("nullDegree");
            }

            executionDegree = (ICursoExecucao) persistentExecutionDegree
                    .readByOID(CursoExecucao.class, executionDegreeCode);

            if (executionDegree == null) {
                throw new NonExistingServiceException();
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException("Problems on database!");
        }
        IDegreeCurricularPlan degreeCurricularPlan = executionDegree
                .getCurricularPlan();
        return super.readActiveCurricularCourseScopes(degreeCurricularPlan, sp);

    }
}