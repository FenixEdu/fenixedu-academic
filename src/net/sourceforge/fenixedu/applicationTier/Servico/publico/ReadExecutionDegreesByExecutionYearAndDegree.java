/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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