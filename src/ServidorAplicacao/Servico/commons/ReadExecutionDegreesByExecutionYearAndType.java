/*
 * Created on 2004/04/04
 *  
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Luis Cruz
 *  
 */
public class ReadExecutionDegreesByExecutionYearAndType implements IService {

    public ReadExecutionDegreesByExecutionYearAndType() {
        super();
    }

    public List run(Integer executionYearOID, TipoCurso typeOfCourse) throws FenixServiceException {
        List infoExecutionDegrees = new ArrayList();
        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            List executionDegrees = executionDegreeDAO.readByExecutionYearOIDAndDegreeType(
                    executionYearOID, typeOfCourse);
            if (executionDegrees != null && !executionDegrees.isEmpty()) {
                for (int i = 0; i < executionDegrees.size(); i++) {
                    infoExecutionDegrees.add(Cloner.get((ICursoExecucao) executionDegrees.get(i)));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegrees;
    }

    public List run(ICurso degree, IExecutionYear executionYear, String tmp)
            throws FenixServiceException {
        List infoExecutionDegrees = new ArrayList();

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            List executionDegrees = executionDegreeDAO.readByDegreeAndExecutionYear(degree,
                    executionYear);
            if (executionDegrees != null && !executionDegrees.isEmpty()) {
                for (int i = 0; i < executionDegrees.size(); i++) {
                    infoExecutionDegrees.add(Cloner.get((ICursoExecucao) executionDegrees.get(i)));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegrees;
    }

}