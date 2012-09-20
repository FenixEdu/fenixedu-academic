/*
 * Created on 20/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Mota
 * 
 * 
 */
public class ReadTeacherExecutionCoursesSitesService extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static List run(InfoTeacher infoTeacher) throws FenixServiceException {

	final List<InfoSite> infoSites = new ArrayList<InfoSite>();

	final Teacher teacher = rootDomainObject.readTeacherByOID(infoTeacher.getIdInternal());
	final List<Professorship> professorships = teacher.getProfessorships();
	for (final Professorship professorship : professorships) {
	    final ExecutionCourse executionCourse = professorship.getExecutionCourse();
	    final ExecutionCourseSite site = executionCourse.getSite();
	    final InfoSite infoSite = InfoSite.newInfoFromDomain(site);
	    infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
	    infoSites.add(infoSite);
	}

	return infoSites;
    }
}