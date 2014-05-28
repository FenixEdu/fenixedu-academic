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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices.CreateStudent;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.OriginInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminStudentsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = AcademicAdminStudentsApp.class, path = "create-student",
        titleKey = "link.studentOperations.createStudent", accessGroup = "academic(CREATE_REGISTRATION)")
@Mapping(path = "/createStudent", module = "academicAdministration")
@Forwards({
        @Forward(name = "chooseNewStudentExecutionDegreeAndIdentification",
                path = "/academicAdminOffice/chooseNewStudentExecutionDegreeAndIdentification.jsp"),
        @Forward(name = "fillNewPersonData", path = "/academicAdminOffice/fillNewPersonData.jsp"),
        @Forward(name = "fillNewPersonDataForEmployee", path = "/academicAdminOffice/fillNewPersonDataForEmployee.jsp"),
        @Forward(name = "fillOriginInformation", path = "/academicAdminOffice/fillOriginInformation.jsp"),
        @Forward(name = "createStudentSuccess", path = "/academicAdminOffice/createStudentSuccess.jsp"),
        @Forward(name = "showCreateStudentConfirmation", path = "/academicAdminOffice/showCreateStudentConfirmation.jsp") })
public class StudentOperationsDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareCreateStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("executionDegreeBean", new ExecutionDegreeBean());
        return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseDegreePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState().getMetaObject().getObject();
        executionDegreeBean.setDegreeCurricularPlan(null);
        executionDegreeBean.setExecutionDegree(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("executionDegreeBean", executionDegreeBean);

        return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseDegreeCurricularPlanPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState().getMetaObject().getObject();

        ExecutionDegree executionDegree = null;
        if (executionDegreeBean.getDegreeCurricularPlan() != null) {
            executionDegree =
                    executionDegreeBean.getDegreeCurricularPlan()
                            .getExecutionDegreeByYear(executionDegreeBean.getExecutionYear());
        }

        executionDegreeBean.setExecutionDegree(executionDegree);
        RenderUtils.invalidateViewState();
        request.setAttribute("executionDegreeBean", executionDegreeBean);
        request.setAttribute("ingressionInformationBean", new IngressionInformationBean());

        return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseAgreementPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionDegreeBean executionDegreeBean =
                (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree").getMetaObject().getObject();
        IngressionInformationBean ingressionInformationBean =
                (IngressionInformationBean) RenderUtils.getViewState("chooseIngression").getMetaObject().getObject();
        ingressionInformationBean.clearIngressionAndEntryPhase();

        RenderUtils.invalidateViewState();

        request.setAttribute("executionDegreeBean", executionDegreeBean);
        request.setAttribute("ingressionInformationBean", ingressionInformationBean);

        if (ingressionInformationBean.getRegistrationAgreement() != null
                && !ingressionInformationBean.getRegistrationAgreement().isNormal()) {
            request.setAttribute("choosePersonBean", new ChoosePersonBean());
        }

        return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseIngressionPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionDegreeBean executionDegreeBean =
                (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree").getMetaObject().getObject();
        IngressionInformationBean ingressionInformationBean =
                (IngressionInformationBean) RenderUtils.getViewState("chooseIngression").getMetaObject().getObject();
        ingressionInformationBean.clearAgreement();

        RenderUtils.invalidateViewState();

        request.setAttribute("executionDegreeBean", executionDegreeBean);
        request.setAttribute("ingressionInformationBean", ingressionInformationBean);

        if (ingressionInformationBean.getIngression() != null && !ingressionInformationBean.getIngression().hasEntryPhase()) {
            request.setAttribute("choosePersonBean", new ChoosePersonBean());
        }

        return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseEntryPhasePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionDegreeBean executionDegreeBean =
                (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree").getMetaObject().getObject();
        IngressionInformationBean ingressionInformationBean =
                (IngressionInformationBean) RenderUtils.getViewState("chooseIngression").getMetaObject().getObject();
        ingressionInformationBean.clearAgreement();

        RenderUtils.invalidateViewState("executionDegree");
        RenderUtils.invalidateViewState("chooseIngression");

        request.setAttribute("executionDegreeBean", executionDegreeBean);
        request.setAttribute("ingressionInformationBean", ingressionInformationBean);
        request.setAttribute("choosePersonBean", new ChoosePersonBean());

        return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("executionDegreeBean", RenderUtils.getViewState().getMetaObject().getObject());

        return mapping.getInputForward();
    }

    public ActionForward choosePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionDegreeBean executionDegreeBean =
                (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree").getMetaObject().getObject();
        IngressionInformationBean ingressionInformationBean =
                (IngressionInformationBean) RenderUtils.getViewState("chooseIngression").getMetaObject().getObject();
        PrecedentDegreeInformationBean precedentDegreeInformationBean =
                RenderUtils.getViewState("precedentDegreeInformation") == null ? new PrecedentDegreeInformationBean() : (PrecedentDegreeInformationBean) RenderUtils
                        .getViewState("precedentDegreeInformation").getMetaObject().getObject();

        request.setAttribute("executionDegreeBean", executionDegreeBean);
        request.setAttribute("ingressionInformationBean", ingressionInformationBean);
        request.setAttribute("precedentDegreeInformationBean", precedentDegreeInformationBean);

        PersonBean personBean = null;
        Person person = null;

        if (RenderUtils.getViewState("person") != null) { // Postback
            request.setAttribute("personBean", RenderUtils.getViewState("person").getMetaObject().getObject());
            return mapping.findForward("fillNewPersonData");
        }

        ChoosePersonBean choosePersonBean =
                (ChoosePersonBean) RenderUtils.getViewState("choosePerson").getMetaObject().getObject();

        final String identificationNumber = choosePersonBean.getIdentificationNumber();
        final YearMonthDay dateOfBirth = choosePersonBean.getDateOfBirth();

        if (choosePersonBean.getPerson() == null) {

            Collection<Person> persons = Person.findPersonByDocumentID(identificationNumber);

            if (choosePersonBean.isFirstTimeSearch()) {
                choosePersonBean.setFirstTimeSearch(false);
                if (!persons.isEmpty()
                        || !Person.findByDateOfBirth(dateOfBirth,
                                Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty()
                        || (choosePersonBean.getStudentNumber() != null && Student.readStudentByNumber(choosePersonBean
                                .getStudentNumber()) != null)) {
                    // show similar persons
                    RenderUtils.invalidateViewState();
                    request.setAttribute("choosePersonBean", choosePersonBean);
                    return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
                }
            }

        } else {
            person = choosePersonBean.getPerson();
        }

        if (!checkIngression(request, executionDegreeBean, ingressionInformationBean, person, choosePersonBean)) {
            return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
        }

        if (person != null) {
            personBean = new PersonBean(person);

            personBean.setStudentNumber(person.hasStudent() ? person.getStudent().getNumber() : choosePersonBean
                    .getStudentNumber());

            if (isEmployeeAndHasCurrentWorkingContract(person)) {
                request.setAttribute("personBean", personBean);
                return mapping.findForward("fillNewPersonDataForEmployee");
            }
        } else {
            personBean =
                    new PersonBean(choosePersonBean.getName(), identificationNumber, choosePersonBean.getDocumentType(),
                            dateOfBirth, choosePersonBean.getStudentNumber());
        }

        request.setAttribute("personBean", personBean);
        return mapping.findForward("fillNewPersonData");
    }

    private boolean isEmployeeAndHasCurrentWorkingContract(Person person) {
        return person.getEmployee() != null && person.getEmployee().getCurrentWorkingContract() != null;
    }

    private boolean checkIngression(HttpServletRequest request, ExecutionDegreeBean executionDegreeBean,
            IngressionInformationBean ingressionInformationBean, Person person, ChoosePersonBean choosePersonBean) {

        try {
            Registration.checkIngression(ingressionInformationBean.getIngression(), person,
                    executionDegreeBean.getDegreeCurricularPlan());
        } catch (DomainException e) {
            RenderUtils.invalidateViewState();
            request.setAttribute("choosePersonBean", choosePersonBean);
            addActionMessage(request, e.getKey());
            return false;
        }

        return true;
    }

    public ActionForward prepareEditInstitutionPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PrecedentDegreeInformationBean pdiBean = getRenderedObject("precedentDegreeInformation");
        pdiBean.resetDegree();

        request.setAttribute("executionDegreeBean", getRenderedObject("executionDegree"));
        request.setAttribute("ingressionInformationBean", getRenderedObject("chooseIngression"));
        request.setAttribute("personBean", getRenderedObject("person"));
        request.setAttribute("precedentDegreeInformationBean", pdiBean);
        request.setAttribute("originInformationBean", getRenderedObject("originInformation"));
        RenderUtils.invalidateViewState("precedentDegreeInformation");
        RenderUtils.invalidateViewState("precedentDegreeInformationExternal");

        return mapping.findForward("fillNewPersonData");
    }

    public ActionForward fillNewPersonDataPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PrecedentDegreeInformationBean pdiBean = getRenderedObject("precedentDegreeInformation");
        pdiBean.updateCountryHighSchoolLevel();

        request.setAttribute("executionDegreeBean", getRenderedObject("executionDegree"));
        request.setAttribute("ingressionInformationBean", getRenderedObject("chooseIngression"));
        request.setAttribute("personBean", getRenderedObject("person"));
        request.setAttribute("precedentDegreeInformationBean", pdiBean);
        request.setAttribute("originInformationBean", getRenderedObject("originInformation"));
        RenderUtils.invalidateViewState("precedentDegreeInformation");
        RenderUtils.invalidateViewState("precedentDegreeInformationExternal");

        return mapping.findForward("fillNewPersonData");
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("executionDegreeBean", getRenderedObject("executionDegree"));
        request.setAttribute("ingressionInformationBean", getRenderedObject("chooseIngression"));
        request.setAttribute("personBean", getRenderedObject("person"));
        request.setAttribute("precedentDegreeInformationBean", getRenderedObject("precedentDegreeInformation"));
        request.setAttribute("originInformationBean", getRenderedObject("originInformation"));

        return mapping.findForward("fillNewPersonData");
    }

    public ActionForward prepareShowFillOriginInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("executionDegreeBean", getRenderedObject("executionDegree"));
        request.setAttribute("ingressionInformationBean", getRenderedObject("chooseIngression"));
        final Object personBean = getRenderedObject("person");
        request.setAttribute("personBean", personBean);

        Object originInformation = getRenderedObject("originInformation");
        request.setAttribute("originInformationBean", originInformation != null ? originInformation : new OriginInformationBean(
                (PersonBean) personBean));

        final PrecedentDegreeInformationBean precedentDegreeBean = getRenderedObject("precedentDegreeInformation");
        request.setAttribute("precedentDegreeInformationBean", precedentDegreeBean);

        try {
            precedentDegreeBean.validate();
        } catch (DomainException e) {
            RenderUtils.invalidateViewState();
            addActionMessage(request, e.getKey());
            return mapping.findForward("fillNewPersonData");
        }

        return mapping.findForward("fillOriginInformation");
    }

    public ActionForward prepareShowCreateStudentConfirmation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("executionDegreeBean", getRenderedObject("executionDegree"));
        request.setAttribute("ingressionInformationBean", getRenderedObject("chooseIngression"));
        request.setAttribute("personBean", getRenderedObject("person"));
        request.setAttribute("precedentDegreeInformationBean", getRenderedObject("precedentDegreeInformation"));
        request.setAttribute("originInformationBean", getRenderedObject("originInformation"));

        return mapping.findForward("showCreateStudentConfirmation");
    }

    public ActionForward prepareCreateStudentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("executionDegreeBean", getRenderedObject("executionDegree"));
        request.setAttribute("ingressionInformationBean", getRenderedObject("chooseIngression"));
        PersonBean personBean = getRenderedObject("person");
        request.setAttribute("personBean", personBean);
        request.setAttribute("precedentDegreeInformationBean", getRenderedObject("precedentDegreeInformation"));
        request.setAttribute("originInformationBean", getRenderedObject("originInformation"));

        return isEmployeeAndHasCurrentWorkingContract(personBean.getPerson()) ? mapping
                .findForward("fillNewPersonDataForEmployee") : mapping.findForward("fillNewPersonData");
    }

    public ActionForward createStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            Registration registration =
                    CreateStudent.run((PersonBean) getRenderedObject("person"),
                            (ExecutionDegreeBean) getRenderedObject("executionDegree"),
                            (PrecedentDegreeInformationBean) getRenderedObject("precedentDegreeInformation"),
                            (IngressionInformationBean) getRenderedObject("chooseIngression"),
                            (OriginInformationBean) getRenderedObject("originInformation"));
            request.setAttribute("registration", registration);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return prepareShowCreateStudentConfirmation(mapping, actionForm, request, response);
        }

        return mapping.findForward("createStudentSuccess");
    }

    /*
     * public ActionForward printRegistrationDeclarationTemplate(ActionMapping
     * mapping, ActionForm actionForm, HttpServletRequest request,
     * HttpServletResponse response) {
     * 
     * Integer registrationID =
     * Integer.valueOf(request.getParameter("registrationID"));
     * request.setAttribute("registration",
     * FenixFramework.getDomainObject(registrationID));
     * 
     * return mapping.findForward("printRegistrationDeclarationTemplate"); }
     */

}