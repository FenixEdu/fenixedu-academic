/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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