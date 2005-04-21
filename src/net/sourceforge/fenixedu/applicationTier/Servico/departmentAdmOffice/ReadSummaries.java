package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * Manuel Pinto e João Figueiredo
 */
public class ReadSummaries implements IService {

    public SiteView run(Integer teacherNumber, Integer executionCourseId, Integer summaryType,
            Integer shiftId) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
        final IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();

        final ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new FenixServiceException("no.shift");
        }

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executionCourse");
        }

        final Collection summaries = new ArrayList();
        summaries.addAll(persistentSummary.readByExecutionCourse(executionCourse));
        summaries.addAll(persistentSummary.readByExecutionCourseShifts(executionCourse));

        final List infoSummaries = new ArrayList(summaries.size());
        for (final Iterator iterator = summaries.iterator(); iterator.hasNext(); ) {
            final ISummary summary = (ISummary) iterator.next();

            if ((summaryType == null || (summary.getSummaryType() != null && summaryType.equals(summary.getSummaryType().getTipo())))

                    && (shiftId == null || (summary.getShift() != null && shiftId.equals(summary.getShift().getIdInternal())))

                    && (teacherNumber == null
                            || (summary.getProfessorship() != null && teacherNumber.equals(summary.getProfessorship().getTeacher().getTeacherNumber()))
                            || (summary.getTeacher() != null && teacherNumber.equals(summary.getTeacher().getTeacherNumber())))) {

                final InfoSummary infoSummary = InfoSummary.newInfoFromDomain(summary);
                infoSummaries.add(infoSummary);
            }
        }

        // execution courses's lesson types for display to filter summary
        List lessonTypes = findLessonTypesExecutionCourse(executionCourse);

        IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
        ISite site = persistentSite.readByExecutionCourse(executionCourse);

        if (site == null) {
            throw new FenixServiceException("no.site");
        }

        // execution courses's shifts for display to filter summary
        ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
        List shifts = persistentShift.readByExecutionCourse(executionCourse);
        List infoShifts = new ArrayList();
        if (shifts != null && shifts.size() > 0) {
            infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {

                public Object transform(Object arg0) {
                    final IShift turno = (IShift) arg0;
                    final InfoShift infoShift = InfoShift.newInfoFromDomain(turno);
                    infoShift.setInfoLessons(new ArrayList(turno.getAssociatedLessons().size()));
                    for (final Iterator iterator = turno.getAssociatedLessons().iterator(); iterator
                            .hasNext();) {
                        final ILesson lesson = (ILesson) iterator.next();
                        final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                        final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(lesson.getSala());
                        infoLesson.setInfoSala(infoRoom);
                        infoShift.getInfoLessons().add(infoLesson);
                    }
                    return infoShift;
                }
            });
        }

        InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
        bodyComponent.setInfoSummaries(infoSummaries);
        bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        bodyComponent.setLessonTypes(lessonTypes);
        bodyComponent.setInfoShifts(infoShifts);
        bodyComponent.setInfoProfessorships(new ArrayList());

        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);
        SiteView siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

        return siteView;
    }

    private List findLessonTypesExecutionCourse(IExecutionCourse executionCourse) {
        List lessonTypes = new ArrayList();

        if (executionCourse.getTheoreticalHours() != null
                && executionCourse.getTheoreticalHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(1));
        }
        if (executionCourse.getTheoPratHours() != null
                && executionCourse.getTheoPratHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(3));
        }
        if (executionCourse.getPraticalHours() != null
                && executionCourse.getPraticalHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(2));
        }
        if (executionCourse.getLabHours() != null && executionCourse.getLabHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(4));
        }

        return lessonTypes;
    }
}