/*
 * Created on 7/Abr/2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
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
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 * @author modified by Gonçalo Luiz, 18/October/2004
 */
public class PrepareInsertSummary implements IService {

    public SiteView run(Integer executionCourseId, String userLogged) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        final ISalaPersistente persistentRoom = sp.getISalaPersistente();
        final IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        final IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executionCourse");
        }

        final ISite site = executionCourse.getSite();
        if (site == null) {
            throw new FenixServiceException("no.site");
        }

        final TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        final ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site,
                null, null, null);
        final InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
        final SiteView siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

        final List<IShift> shifts = executionCourse.getAssociatedShifts();
        final List<InfoShift> infoShifts = new ArrayList<InfoShift>(shifts.size());
        for (final IShift shift : shifts) {
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoShifts.add(infoShift);

            final List<ILesson> lessons = shift.getAssociatedLessons();
            final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(lessons.size());
            infoShift.setInfoLessons(infoLessons);
            for (final ILesson lesson : lessons) {
                final InfoLesson infoLesson = InfoLessonWithInfoRoom.newInfoFromDomain(lesson);
                infoLessons.add(infoLesson);
            }
        }

        final List<IRoom> rooms = persistentRoom.readAll();
        final List<InfoRoom> infoRooms = new ArrayList<InfoRoom>(rooms.size());

        for (final IRoom room : rooms) {
            final InfoRoom infoRoom = new InfoRoom();
            infoRooms.add(infoRoom);

            infoRoom.setIdInternal(room.getIdInternal());
            infoRoom.setNome(room.getNome());
        }

        Collections.sort(infoRooms, new BeanComparator("nome"));

        final List<IProfessorship> professorships = executionCourse.getProfessorships();
        final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>(1);
        Integer professorshipSelect = null;
        for (final IProfessorship professorship : professorships) {
            if (professorship.getTeacher().getPerson().getUsername().equalsIgnoreCase(userLogged)) {
                infoProfessorships.add(InfoProfessorshipWithAll.newInfoFromDomain(professorship));
                professorshipSelect = professorship.getIdInternal();
                break;
            }
        }

        bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));
        bodyComponent.setInfoShifts(infoShifts);
        bodyComponent.setInfoProfessorships(infoProfessorships);
        bodyComponent.setInfoRooms(infoRooms);
        bodyComponent.setTeacherId(professorshipSelect);

        return siteView;
    }
}