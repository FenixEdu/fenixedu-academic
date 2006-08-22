/*
 * Created on 7/Abr/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.OldRoom;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Manuel Pinto e João Figueiredo
 */
public class PrepareInsertSummary extends Service {

    public SiteView run(Integer teacherNumber, Integer executionCourseId) throws FenixServiceException {
	
	//SiteView siteView;

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	if (executionCourse == null) {
	    throw new FenixServiceException("no.executionCourse");
	}

	final Site site = executionCourse.getSite();
	if (site == null) {
	    throw new FenixServiceException("no.site");
	}

	final List<InfoShift> infoShifts = (List) CollectionUtils.collect(executionCourse
		.getAssociatedShifts(), new Transformer() {
	    
	    public Object transform(Object arg0) {
		final Shift turno = (Shift) arg0;
		final InfoShift infoShift = InfoShift.newInfoFromDomain(turno);
		infoShift.setInfoLessons(new ArrayList(turno.getAssociatedLessons().size()));
		for (final Iterator<Lesson> iterator = turno.getAssociatedLessons().iterator(); iterator
			.hasNext();) {
		    infoShift.getInfoLessons().add(InfoLesson.newInfoFromDomain(iterator.next()));
		}
		return infoShift;
	    }
	});

	List infoProfessorships = new ArrayList();
	Collection<OldRoom> rooms = OldRoom.getOldRooms();
	List infoRooms = new ArrayList();
	if (rooms != null && rooms.size() > 0) {
	    infoRooms = (List) CollectionUtils.collect(rooms, new Transformer() {

		public Object transform(Object arg0) {
		    OldRoom room = (OldRoom) arg0;
		    return InfoRoom.newInfoFromDomain(room);
		}
	    });
	}
	Collections.sort(infoRooms, new BeanComparator("nome"));

	// teacher logged
	Teacher teacher = Teacher.readByNumber(teacherNumber);
	Integer professorshipSelect = null;
	if (teacher != null) {
	    final Professorship professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
	    if (professorship != null) {
		professorshipSelect = professorship.getIdInternal();
		infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
	    }
	}

	InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
	bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
	bodyComponent.setInfoShifts(infoShifts);
	bodyComponent.setInfoProfessorships(infoProfessorships);
	bodyComponent.setInfoRooms(infoRooms);
	bodyComponent.setTeacherId(professorshipSelect);

	TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
		.getInstance();
	ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
		null, null);

	return new ExecutionCourseSiteView(commonComponent, bodyComponent);
    }
}