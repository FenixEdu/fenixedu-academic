/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.TeacherSearchForExecutionCourseAssociation;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentAdmOffice", path = "/showTeacherProfessorshipsForManagement",
        formBean = "teacherExecutionCourseResponsabilities", functionality = TeacherSearchForExecutionCourseAssociation.class)
@Forwards({ @Forward(name = "list-professorships",
        path = "/departmentAdmOffice/teacher/showTeacherProfessorshipsForManagement.jsp") })
@Exceptions(value = { @ExceptionHandling(type = NotAuthorizedException.class, key = "message.teacher-not-belong-to-department",
        handler = ExceptionHandler.class, path = "/teacherSearchForExecutionCourseAssociation.do", scope = "request") })
public class ReadPersonProfessorshipsByExecutionYearAction extends Action {
    private final class Professorships2DetailProfessorship implements Transformer {
        private Professorships2DetailProfessorship() {
            super();
        }

        @Override
        public Object transform(Object input) {
            Professorship professorship = (Professorship) input;
            InfoProfessorship infoProfessorShip = InfoProfessorship.newInfoFromDomain(professorship);

            final DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

            ExecutionCourse executionCourse = professorship.getExecutionCourse();
            List executionCourseCurricularCoursesList = getInfoCurricularCourses(detailedProfessorship, executionCourse);

            detailedProfessorship.setResponsibleFor(professorship.getResponsibleFor());

            detailedProfessorship.setInfoProfessorship(infoProfessorShip);
            detailedProfessorship.setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

            return detailedProfessorship;
        }

        private List getInfoCurricularCourses(final DetailedProfessorship detailedProfessorship, ExecutionCourse executionCourse) {

            List infoCurricularCourses =
                    (List) CollectionUtils.collect(executionCourse.getAssociatedCurricularCourses(), new Transformer() {

                        @Override
                        public Object transform(Object input) {
                            CurricularCourse curricularCourse = (CurricularCourse) input;
                            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
                            if (degreeType.equals(DegreeType.DEGREE)) {
                                detailedProfessorship.setMasterDegreeOnly(Boolean.FALSE);
                            }
                            return infoCurricularCourse;
                        }
                    });
            return infoCurricularCourses;
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoPerson infoPerson = getInfoPerson(request, dynaForm);

        List detailedInfoProfessorshipList = getDetailedProfessorships(userView, infoPerson.getExternalId(), dynaForm, request);

        ComparatorChain chain = new ComparatorChain();

        Comparator executionPeriodComparator =
                new BeanComparator("infoProfessorship.infoExecutionCourse.infoExecutionPeriod.semester");
        Comparator nameComparator = new BeanComparator("infoProfessorship.infoExecutionCourse.nome");

        chain.addComparator(executionPeriodComparator);
        chain.addComparator(nameComparator);
        Collections.sort(detailedInfoProfessorshipList, chain);

        request.setAttribute("detailedProfessorshipList", detailedInfoProfessorshipList);

        extraPreparation(userView, infoPerson, request, dynaForm);
        return mapping.findForward("list-professorships");
    }

    protected InfoPerson getInfoPerson(HttpServletRequest request, DynaActionForm dynaForm) throws Exception {
        InfoPerson infoPerson = (InfoPerson) request.getAttribute("infoPerson");
        if (infoPerson == null) {
            final User userView = Authenticate.getUser();
            infoPerson = ReadPersonByID.run((String) dynaForm.get("externalId"));
            request.setAttribute("infoPerson", infoPerson);

        }
        return infoPerson;
    }

    List getDetailedProfessorships(User userView, String personId, DynaActionForm actionForm, HttpServletRequest request)
            throws FenixServiceException {

        Collection<Professorship> professorshipList = ((Person) FenixFramework.getDomainObject(personId)).getProfessorships();

        String executionYearID = (String) actionForm.get("executionYearId");
        ExecutionYear executionYear =
                (!StringUtils.isEmpty(executionYearID) ? (ExecutionYear) FenixFramework.getDomainObject(executionYearID) : null);
        if (executionYear == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }
        final List<Professorship> responsibleFors = new ArrayList();
        for (final Professorship professorship : professorshipList) {
            if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear() == executionYear) {
                responsibleFors.add(professorship);
            }
        }

        List detailedProfessorshipList =
                (List) CollectionUtils.collect(responsibleFors, new Professorships2DetailProfessorship());

        request.setAttribute("args", new TreeMap());
        return detailedProfessorshipList;
    }

    protected void extraPreparation(User userView, InfoPerson infoPerson, HttpServletRequest request, DynaActionForm dynaForm)
            throws FenixServiceException {

        prepareConstants(userView, infoPerson, request);
        prepareForm(dynaForm, request);
    }

    private void prepareForm(DynaActionForm dynaForm, HttpServletRequest request) {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) request.getAttribute("executionYear");
        InfoPerson infoPerson = (InfoPerson) request.getAttribute("infoPerson");
        dynaForm.set("externalId", infoPerson.getExternalId());
        dynaForm.set("teacherId", infoPerson.getIstUsername());
        dynaForm.set("teacherName", infoPerson.getIstUsername());
        if (dynaForm.get("executionYearId") == null || dynaForm.getString("executionYearId").isEmpty()) {
            dynaForm.set("executionYearId", infoExecutionYear.getExternalId());
        }

        List detailedProfessorshipList = (List) request.getAttribute("detailedProfessorshipList");

        List executionCourseIds = new ArrayList();
        Map hours = new HashMap();
        for (int i = 0; i < detailedProfessorshipList.size(); i++) {
            DetailedProfessorship dps = (DetailedProfessorship) detailedProfessorshipList.get(i);

            String executionCourseId = dps.getInfoProfessorship().getInfoExecutionCourse().getExternalId();
            if (dps.getResponsibleFor().booleanValue()) {
                executionCourseIds.add(executionCourseId);
            }
            if (dps.getMasterDegreeOnly().booleanValue()) {
                if (dps.getInfoProfessorship().getHours() != null) {
                    hours.put(executionCourseId.toString(), dps.getInfoProfessorship().getHours().toString());
                }
            }
        }

        dynaForm.set("executionCourseResponsability", executionCourseIds.toArray(new String[] {}));
        dynaForm.set("hours", hours);

    }

    private void prepareConstants(User userView, InfoPerson infoPerson, HttpServletRequest request) throws FenixServiceException {

        List executionYears = ReadNotClosedExecutionYears.run();

        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) CollectionUtils.find(executionYears, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                InfoExecutionYear infoExecutionYearElem = (InfoExecutionYear) arg0;
                if (infoExecutionYearElem.getState().equals(PeriodState.CURRENT)) {
                    return true;
                }
                return false;
            }
        });
        Person person = (Person) FenixFramework.getDomainObject(infoPerson.getExternalId());
        InfoDepartment teacherDepartment = null;
        if (person.getTeacher() != null) {
            Department department = person.getTeacher().getCurrentWorkingDepartment();
            teacherDepartment = InfoDepartment.newInfoFromDomain(department);
            if (userView == null || !userView.getPerson().hasRole(RoleType.CREDITS_MANAGER)) {
                final Collection<Department> departmentList = userView.getPerson().getManageableDepartmentCredits();
                request.setAttribute("isDepartmentManager", (departmentList.contains(department) || department == null));
            } else {
                request.setAttribute("isDepartmentManager", Boolean.FALSE);
            }
        } else {
            request.setAttribute("isDepartmentManager", Boolean.TRUE);
        }

        request.setAttribute("teacherDepartment", teacherDepartment);
        request.setAttribute("executionYear", infoExecutionYear);
        request.setAttribute("executionYears", executionYears);
    }

}
