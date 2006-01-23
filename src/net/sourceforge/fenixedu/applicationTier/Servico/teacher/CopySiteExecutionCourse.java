/*
 * Created on 22/Out/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.ExecutionCourseUtils;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author tfs
 * 
 */
public class CopySiteExecutionCourse extends Service {

    public void run(Integer executionCourseFromID, Integer executionCourseToID)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        final ExecutionCourse executionCourseFrom = (ExecutionCourse) persistentObject
                .readByOID(ExecutionCourse.class, executionCourseFromID);
        if (executionCourseFrom == null)
            throw new InvalidArgumentsServiceException();

        final ExecutionCourse executionCourseTo = (ExecutionCourse) persistentObject
                .readByOID(ExecutionCourse.class, executionCourseToID);
        if (executionCourseTo == null)
            throw new InvalidArgumentsServiceException();

        Site siteFrom = executionCourseFrom.getSite();
        if (siteFrom == null) {
            throw new FenixServiceException();
        }

        Site siteTo = executionCourseTo.getSite();
        if (siteTo == null) {
            executionCourseTo.createSite();
            siteTo = executionCourseTo.getSite();
        }

        siteTo.edit(siteFrom.getInitialStatement(), siteFrom.getIntroduction(), siteFrom.getMail(),
                siteFrom.getAlternativeSite());

        ExecutionCourseUtils.deleteSectionsAndItemsIfExistFrom(siteTo);
        ExecutionCourseUtils.copySectionsAndItems(siteFrom, siteTo);
        ExecutionCourseUtils.copyBibliographicReference(executionCourseFrom, executionCourseTo);
        ExecutionCourseUtils.copyEvaluationMethod(executionCourseFrom, executionCourseTo);
    }
}