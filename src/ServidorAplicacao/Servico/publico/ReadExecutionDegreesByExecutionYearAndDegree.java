/*
 * Created on 2004/04/04
 *  
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IDegree;
import Dominio.IExecutionDegree;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class ReadExecutionDegreesByExecutionYearAndDegree implements IService {
    private static ReadExecutionDegreesByExecutionYearAndDegree service = new ReadExecutionDegreesByExecutionYearAndDegree();

    public ReadExecutionDegreesByExecutionYearAndDegree() {
    }

    public String getNome() {
        return "ReadExecutionDegreesByExecutionYearAndDegree";
    }

    public static ReadExecutionDegreesByExecutionYearAndDegree getService() {
        return service;
    }

    //    public List run(Integer executionYearOID, TipoCurso typeOfCourse) throws
    // FenixServiceException {
    //        List infoExecutionDegrees = new ArrayList();
    //        try {
    //		
    //            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
    //            IPersistentExecutionDegree executionDegreeDAO =
    // sp.getIPersistentExecutionDegree();
    //
    //            List executionDegrees =
    // executionDegreeDAO.readByExecutionYearOIDAndDegreeType(
    //                    executionYearOID, typeOfCourse);
    //            if (executionDegrees != null && !executionDegrees.isEmpty()) {
    //                for (int i = 0; i < executionDegrees.size(); i++) {
    //                    infoExecutionDegrees.add(Cloner.get((IExecutionDegree)
    // executionDegrees.get(i)));
    //                }
    //            }
    //        } catch (ExcepcaoPersistencia ex) {
    //            throw new FenixServiceException(ex);
    //        }
    //        return infoExecutionDegrees;
    //    }

    public Object run(IDegree curso, IExecutionYear year) throws FenixServiceException {
        List infoExecutionDegrees = new ArrayList();

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            List executionDegrees = executionDegreeDAO.readByDegreeAndExecutionYear(curso, year);
            if (executionDegrees != null && !executionDegrees.isEmpty()) {
                for (int i = 0; i < executionDegrees.size(); i++) {
                    infoExecutionDegrees.add(Cloner.get((IExecutionDegree) executionDegrees.get(i)));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegrees;
    }

}