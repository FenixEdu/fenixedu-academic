/*
 *
 * Created on 2003/08/22
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExecutionCourse implements IService {

    public InfoExecutionCourse run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, infoExecutionCourse.getIdInternal());
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        executionCourse.edit(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(),
                infoExecutionCourse.getTheoreticalHours(), infoExecutionCourse.getTheoPratHours(),
                infoExecutionCourse.getPraticalHours(), infoExecutionCourse.getLabHours(),
                infoExecutionCourse.getComment());
        
        final InfoExecutionCourse result = InfoExecutionCourse.newInfoFromDomain(executionCourse);
        final List<InfoCurricularCourse> infoCurricularCourses = getInfoCurricularCoursesFrom(executionCourse);
        result.setAssociatedInfoCurricularCourses(infoCurricularCourses);

        return result;
    }

    private List<InfoCurricularCourse> getInfoCurricularCoursesFrom(
            final IExecutionCourse executionCourse) {
        final List<InfoCurricularCourse> result = new ArrayList();
        final Iterator associatedCurricularCourses = executionCourse
                .getAssociatedCurricularCoursesIterator();
        while (associatedCurricularCourses.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) associatedCurricularCourses.next();
            result.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }
        return result;
    }
}