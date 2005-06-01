/*
 * Created on 7/Abr/2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Manuel Pinto e João Figueiredo
 */
public class PrepareInsertSummary implements IService {

    public SiteView run(Integer teacherNumber, Integer executionCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {
        SiteView siteView;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executionCourse");
        }

        IPersistentSite persistentSite = sp.getIPersistentSite();
        ISite site = persistentSite.readByExecutionCourse(executionCourse.getIdInternal());
        if (site == null) {
            throw new FenixServiceException("no.site");
        }

        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        List shifts = persistentShift.readByExecutionCourse(executionCourse.getIdInternal());
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

        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        List infoProfessorships = new ArrayList();

        ISalaPersistente persistentRoom = sp.getISalaPersistente();
        List rooms = persistentRoom.readAll();
        List infoRooms = new ArrayList();
        if (rooms != null && rooms.size() > 0) {
            infoRooms = (List) CollectionUtils.collect(rooms, new Transformer() {

                public Object transform(Object arg0) {
                    IRoom room = (IRoom) arg0;
                    return Cloner.copyRoom2InfoRoom(room);
                }
            });
        }
        Collections.sort(infoRooms, new BeanComparator("nome"));

        //teacher logged
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
        Integer professorshipSelect = null;
        if (teacher != null) {
            IProfessorship professorship = persistentProfessorship.readByTeacherAndExecutionCourse(
                    teacher.getIdInternal(), executionCourse.getIdInternal());
            if (professorship != null) {
                professorshipSelect = professorship.getIdInternal();
                infoProfessorships.add(InfoProfessorshipWithAll.newInfoFromDomain(professorship));
            }
        }

        InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
        bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));
        bodyComponent.setInfoShifts(infoShifts);
        bodyComponent.setInfoProfessorships(infoProfessorships);
        bodyComponent.setInfoRooms(infoRooms);
        bodyComponent.setTeacherId(professorshipSelect);

        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

        return siteView;
    }
}