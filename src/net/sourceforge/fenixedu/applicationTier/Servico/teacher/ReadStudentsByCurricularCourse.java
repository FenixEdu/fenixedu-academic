package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadStudentsByCurricularCourse extends Service {

    public Object run(Integer executionCourseCode, Integer courseCode) throws FenixServiceException {

        CurricularCourse curricularCourse = null;

    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( executionCourseCode);

        final List<InfoStudent> infoStudentList;
        if (executionCourse == null) {
            infoStudentList = getAllAttendingStudents(executionCourse.getSite());
        } else {
            curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(courseCode);
            infoStudentList = getCurricularCourseStudents(curricularCourse);
        }
        return createSiteView(infoStudentList, executionCourse.getSite(), curricularCourse);
    }

    private List<InfoStudent> getCurricularCourseStudents(CurricularCourse curricularCourse) {
        return (List) CollectionUtils.collect(curricularCourse.getEnrolments(), new Transformer() {
            public Object transform(Object input) {
                Enrolment enrolment = (Enrolment) input;
                Registration registration = enrolment.getStudentCurricularPlan().getRegistration();
                return InfoStudent.newInfoFromDomain(registration);
            }
        });
    }

    private List<InfoStudent> getAllAttendingStudents(final ExecutionCourseSite site) {
        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (final Attends attends : site.getExecutionCourse().getAttends()) {
            infoStudentList.add(InfoStudent.newInfoFromDomain(attends.getRegistration()));
        }
        return infoStudentList;
    }

    private TeacherAdministrationSiteView createSiteView(List infoStudentList, ExecutionCourseSite site,
            CurricularCourse curricularCourse) throws FenixServiceException {
	
        final InfoSiteStudents infoSiteStudents = new InfoSiteStudents();
        infoSiteStudents.setStudents(infoStudentList);

        if (curricularCourse != null) {
            infoSiteStudents.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        final TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        final ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

        return new TeacherAdministrationSiteView(commonComponent, infoSiteStudents);
    }
}