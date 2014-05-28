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
/*
 * Created on Dec 10, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.degree.execution.ReadExecutionCoursesByExecutionDegreeService;
import net.sourceforge.fenixedu.applicationTier.Servico.degree.execution.ReadExecutionDegreesByExecutionYearAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.department.professorship.ReadExecutionCoursesByTeacherResponsibility;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.TeacherSearchForExecutionCourseAssociation;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship.exception.handler.MaxResponsibleForExceedHandler;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author jpvl
 */
@Mapping(path = "/createProfessorship", module = "departmentAdmOffice", formBean = "teacherExecutionCourseForm",
        functionality = TeacherSearchForExecutionCourseAssociation.class,
        input = "/createProfessorship.do?method=showExecutionYearExecutionPeriods")
@Forwards({ @Forward(name = "second-step", path = "/departmentAdmOffice/teacher/professorship/createProfessorship.jsp"),
        @Forward(name = "third-step", path = "/departmentAdmOffice/teacher/professorship/createProfessorship.jsp"),
        @Forward(name = "final-step", path = "/departmentAdmOffice/teacherSearchForExecutionCourseAssociation.do") })
@Exceptions({
        @ExceptionHandling(handler = ExceptionHandler.class, type = InvalidCategory.class,
                key = "message.professorship.invalidCategory", scope = "request",
                path = "/createProfessorship.do?method=showExecutionDegreeExecutionCourses"),
        @ExceptionHandling(handler = MaxResponsibleForExceedHandler.class, type = MaxResponsibleForExceed.class,
                key = "message.professorship.numberOfResponsibleForExceeded", scope = "request",
                path = "/createProfessorship.do?method=showExecutionDegreeExecutionCourses") })
public class CreateProfessorshipDispatchAction extends FenixDispatchAction {

    public ActionForward createProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm personExecutionCourseForm = (DynaActionForm) form;

        final Boolean responsibleFor = (Boolean) personExecutionCourseForm.get("responsibleFor");

        Professorship.create(responsibleFor,
                this.<ExecutionCourse> getDomainObject(personExecutionCourseForm, "executionCourseId"),
                getPerson(personExecutionCourseForm), 0.0);

        return mapping.findForward("final-step");
    }

    private List getExecutionDegrees(HttpServletRequest request) throws FenixServiceException {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute("infoExecutionPeriod");

        List<InfoExecutionDegree> executionDegrees =
                ReadExecutionDegreesByExecutionYearAndDegreeType.run(infoExecutionPeriod.getInfoExecutionYear().getYear(), null);

        ComparatorChain comparatorChain = new ComparatorChain();

        comparatorChain.addComparator(new BeanComparator("infoDegreeCurricularPlan.infoDegree.tipoCurso"));
        comparatorChain.addComparator(new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));

        Collections.sort(executionDegrees, comparatorChain);

        MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
        executionDegrees = InfoExecutionDegree.buildLabelValueBeansForList(executionDegrees, messageResources);

        return executionDegrees;
    }

    public Person getPerson(DynaActionForm personExecutionCourseForm) {
        String id = (String) personExecutionCourseForm.get("teacherId");
        Person person = Person.readPersonByUsername(id);
        return person;
    }

    private void prepareConstants(DynaActionForm personExecutionCourseForm, HttpServletRequest request)
            throws FenixServiceException {

        request.setAttribute("infoPerson", InfoPerson.newInfoFromDomain(getPerson(personExecutionCourseForm)));
    }

    private void prepareFirstStep(DynaValidatorForm personExecutionCourseForm, HttpServletRequest request)
            throws FenixServiceException {
        prepareConstants(personExecutionCourseForm, request);

        List executionPeriodsNotClosed = ReadNotClosedExecutionPeriods.run();

        setChoosedExecutionPeriod(request, executionPeriodsNotClosed, personExecutionCourseForm);

        BeanComparator initialDateComparator = new BeanComparator("beginDate");
        Collections.sort(executionPeriodsNotClosed, new ReverseComparator(initialDateComparator));

        request.setAttribute("executionPeriods", executionPeriodsNotClosed);
    }

    private void prepareSecondStep(DynaValidatorForm personExecutionCourseForm, HttpServletRequest request)
            throws FenixServiceException {
        prepareFirstStep(personExecutionCourseForm, request);
        List executionDegrees = getExecutionDegrees(request);
        request.setAttribute("executionDegrees", executionDegrees);
    }

    private void prepareThirdStep(DynaValidatorForm personExecutionCourseForm, HttpServletRequest request)
            throws FenixServiceException {
        prepareSecondStep(personExecutionCourseForm, request);
        String executionDegreeId = (String) personExecutionCourseForm.get("executionDegreeId");
        String executionPeriodId = (String) personExecutionCourseForm.get("executionPeriodId");

        List executionCourses = ReadExecutionCoursesByExecutionDegreeService.run(executionDegreeId, executionPeriodId);
        String personId = (String) personExecutionCourseForm.get("teacherId");

        List executionCoursesToRemove = ReadExecutionCoursesByTeacherResponsibility.run(personId);
        executionCourses.removeAll(executionCoursesToRemove);
        Collections.sort(executionCourses, new BeanComparator("nome"));

        request.setAttribute("executionCourses", executionCourses);

    }

    private void setChoosedExecutionPeriod(HttpServletRequest request, List executionPeriodsNotClosed,
            DynaValidatorForm personExecutionCourseForm) {
        String executionPeriodIdValue = null;
        try {
            executionPeriodIdValue = (String) personExecutionCourseForm.get("executionPeriodId");
        } catch (Exception e) {
            // do nothing
        }
        final String executionPeriodId = executionPeriodIdValue;
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (executionPeriodId == null) {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed, new Predicate() {

                @Override
                public boolean evaluate(Object input) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                    return infoExecutionPeriod.getState().equals(PeriodState.CURRENT);
                }
            });
        } else {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed, new Predicate() {

                @Override
                public boolean evaluate(Object input) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                    return infoExecutionPeriod.getExternalId().equals(executionPeriodId);
                }
            });

        }
        request.setAttribute("infoExecutionPeriod", infoExecutionPeriod);
    }

    public ActionForward showExecutionDegreeExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaValidatorForm personExecutionCourseForm = (DynaValidatorForm) form;
        prepareFirstStep(personExecutionCourseForm, request);

        prepareThirdStep(personExecutionCourseForm, request);
        personExecutionCourseForm.set("page", new Integer(3));
        return mapping.findForward("third-step");
    }

    public ActionForward showExecutionDegrees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaValidatorForm personExecutionCourseForm = (DynaValidatorForm) form;

        String personId = (String) personExecutionCourseForm.get("teacherId");
        Person person = Person.readPersonByUsername(personId);
        setChoosedExecutionPeriod(request, ReadNotClosedExecutionPeriods.run(), personExecutionCourseForm);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute("infoExecutionPeriod");
        final ExecutionSemester executionPeriod = infoExecutionPeriod.getExecutionPeriod();
        if (executionPeriod.getSemester().intValue() == 2 && executionPeriod.getExecutionYear().getYear().equals("2010/2011")) {
        } else {
            if (person.getTeacher() == null
                    || (person.getTeacher().getTeacherAuthorization(executionPeriod) == null && !person.hasRole(RoleType.TEACHER))) {
                request.setAttribute("notAuth", true);
                return showExecutionYearExecutionPeriods(mapping, personExecutionCourseForm, request, response);
            }
        }

        prepareSecondStep(personExecutionCourseForm, request);
        personExecutionCourseForm.set("page", new Integer(2));
        return mapping.findForward("second-step");
    }

    public ActionForward showExecutionYearExecutionPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaValidatorForm personExecutionCourseForm = (DynaValidatorForm) form;

        prepareFirstStep(personExecutionCourseForm, request);
        personExecutionCourseForm.set("page", new Integer(1));
        return mapping.findForward("second-step");
    }
}