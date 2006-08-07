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

    public void run(Integer executionCourseFromID, Integer executionCourseToID,
            Boolean sectionsAndItems, Boolean bibliographicReference, Boolean evaluationMethod)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        final ExecutionCourse executionCourseFrom = rootDomainObject
                .readExecutionCourseByOID(executionCourseFromID);
        if (executionCourseFrom == null)
            throw new InvalidArgumentsServiceException();

        final ExecutionCourse executionCourseTo = rootDomainObject
                .readExecutionCourseByOID(executionCourseToID);
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

        if (sectionsAndItems != null && sectionsAndItems) {
            ExecutionCourseUtils.deleteSectionsAndItemsIfExistFrom(siteTo);
            ExecutionCourseUtils.copySectionsAndItems(siteFrom, siteTo);
        }

        if (bibliographicReference != null && bibliographicReference) {
            ExecutionCourseUtils.copyBibliographicReference(executionCourseFrom, executionCourseTo);
        }

        if (evaluationMethod != null && evaluationMethod) {
            ExecutionCourseUtils.copyEvaluationMethod(executionCourseFrom, executionCourseTo);
        }
    }
}