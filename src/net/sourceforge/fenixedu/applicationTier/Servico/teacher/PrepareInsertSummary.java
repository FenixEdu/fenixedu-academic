package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

public class PrepareInsertSummary extends Service {

    public SiteView run(Integer executionCourseID, String userLogged) throws FenixServiceException,
            ExcepcaoPersistencia {
       
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executionCourse");
        }
        
        final Site site = executionCourse.getSite();
        if (site == null) {
            throw new FenixServiceException("no.site");
        }

        final TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        final ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site,
                null, null, null);
        final InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
        final SiteView siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

        final List<Shift> shifts = executionCourse.getAssociatedShifts();
        final List<InfoShift> infoShifts = new ArrayList<InfoShift>(shifts.size());
        for (final Shift shift : shifts) {
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoShifts.add(infoShift);
        }

        final Collection<OldRoom> rooms = OldRoom.getOldRooms();
        final List<InfoRoom> infoRooms = new ArrayList<InfoRoom>(rooms.size());

        for (final OldRoom room : rooms) {
            final InfoRoom infoRoom = new InfoRoom();
            infoRooms.add(infoRoom);

            infoRoom.setIdInternal(room.getIdInternal());
            infoRoom.setNome(room.getName());
        }

        Collections.sort(infoRooms, new BeanComparator("nome"));

        Integer professorshipSelect = null;
        final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>(1);
        for (final Professorship professorship : executionCourse.getProfessorships()) {
            if (professorship.getTeacher().getPerson().getUsername().equalsIgnoreCase(userLogged)) {
                infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
                professorshipSelect = professorship.getIdInternal();
                break;
            }
        }

        bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        bodyComponent.setInfoShifts(infoShifts);
        bodyComponent.setInfoProfessorships(infoProfessorships);
        bodyComponent.setInfoRooms(infoRooms);
        bodyComponent.setTeacherId(professorshipSelect);

        return siteView;
    }
}