/*
 * Created on 16/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByCurricularCourse implements IService {

    /**
     * The constructor of this class.
     */
    public ReadExecutionCoursesByCurricularCourse() {
    }

    /**
     * Executes the service. Returns the current collection of
     * infoExecutionCourses.
     */
    public List run(Integer curricularCourseId) throws FenixServiceException {
        ISuportePersistente sp;
        List allExecutionCourses = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse()
                    .readByOID(CurricularCourse.class, curricularCourseId);

            if (curricularCourse == null) {

                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
            }

            allExecutionCourses = curricularCourse.getAssociatedExecutionCourses();

            if (allExecutionCourses == null || allExecutionCourses.isEmpty()) {
                return allExecutionCourses;
            }

            // build the result of this service
            Iterator iterator = allExecutionCourses.iterator();
            List result = new ArrayList(allExecutionCourses.size());

            Boolean hasSite;
            while (iterator.hasNext()) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
                        .get((IExecutionCourse) iterator.next());
                try {
                    hasSite = sp.getIPersistentExecutionCourse().readSite(
                            infoExecutionCourse.getIdInternal());
                } catch (ExcepcaoPersistencia ex) {
                    throw new FenixServiceException(ex);
                }
                infoExecutionCourse.setHasSite(hasSite);
                result.add(infoExecutionCourse);
            }
            return result;
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}