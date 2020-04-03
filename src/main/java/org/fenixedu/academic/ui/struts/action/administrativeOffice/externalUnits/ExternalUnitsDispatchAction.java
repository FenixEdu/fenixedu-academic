/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.externalUnits;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExternalCurricularCourse;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitName;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.CreateExternalCurricularCourseBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.CreateExternalUnitBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.EditExternalCurricularCourseBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.EditExternalEnrolmentBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.EditExternalUnitBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.ExternalUnitResultBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.ExternalUnitsSearchBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.CreateExternalCurricularCourse;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.CreateExternalUnit;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.DeleteExternalCurricularCourse;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.DeleteExternalUnit;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.EditExternalCurricularCourse;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.EditExternalEnrolment;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.EditExternalUnit;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminInstitutionsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AcademicAdminInstitutionsApp.class, path = "external-units", titleKey = "label.externalUnits",
        accessGroup = "academic(MANAGE_EXTERNAL_UNITS)")
@Mapping(path = "/externalUnits", module = "academicAdministration", formBean = "externalUnitsForm")
@Forwards({ @Forward(name = "searchExternalUnits", path = "/academicAdminOffice/externalUnits/searchExternalUnit.jsp"),
        @Forward(name = "viewCountryUnit", path = "/academicAdminOffice/externalUnits/viewCountryUnit.jsp"),
        @Forward(name = "viewUniversityUnit", path = "/academicAdminOffice/externalUnits/viewUniversityUnit.jsp"),
        @Forward(name = "viewSchoolUnit", path = "/academicAdminOffice/externalUnits/viewSchoolUnit.jsp"),
        @Forward(name = "viewDepartmentUnit", path = "/academicAdminOffice/externalUnits/viewDepartmentUnit.jsp"),
        @Forward(name = "viewExternalCurricularCourse",
                path = "/academicAdminOffice/externalUnits/viewExternalCurricularCourse.jsp"),
        @Forward(name = "prepareCreateUnit", path = "/academicAdminOffice/externalUnits/createExternalUnit.jsp"),
        @Forward(name = "prepareEditUnit", path = "/academicAdminOffice/externalUnits/editExternalUnit.jsp"),
        @Forward(name = "prepareDeleteUnit", path = "/academicAdminOffice/externalUnits/deleteExternalUnit.jsp"),
        @Forward(name = "prepareDeleteExternalCurricularCourse",
                path = "/academicAdminOffice/externalUnits/deleteExternalCurricularCourse.jsp"),
        @Forward(name = "prepareCreateExternalCurricularCourse",
                path = "/academicAdminOffice/externalUnits/createExternalCurricularCourse.jsp"),
        @Forward(name = "prepareEditExternalCurricularCourse",
                path = "/academicAdminOffice/externalUnits/editExternalCurricularCourse.jsp"),
        @Forward(name = "prepareEditExternalEnrolment", path = "/academicAdminOffice/externalUnits/editExternalEnrolment.jsp") })
public class ExternalUnitsDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("searchBean", new ExternalUnitsSearchBean());
        return mapping.findForward("searchExternalUnits");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final ExternalUnitsSearchBean searchBean = getRenderedObject();
        request.setAttribute("searchBean", searchBean);

        if (StringUtils.isEmpty(searchBean.getUnitName())) {
            addActionMessage("error", request, "error.externalUnits.invalid.unit.name");
        } else {
            searchBean.clearResults();
            searchUnits(searchBean);
            searchExternalCurricularCourses(searchBean);
            Collections.sort(searchBean.getResults(), new BeanComparator("fullName"));
            searchBean.setEarthUnit(UnitUtils.readEarthUnit());
        }

        return mapping.findForward("searchExternalUnits");
    }

    private String buildNameToSearch(final String name) {
        String result = name.replaceAll("[ ]", "%");
        result = !name.startsWith("%") ? "%" + result : result;
        result = !name.endsWith("%") ? result + "%" : result;
        return result;
    }

    private void searchUnits(final ExternalUnitsSearchBean searchBean) {
        for (final Unit unit : readExternalUnitsByNameAndTypesStartingAtEarth(buildNameToSearch(searchBean.getUnitName()),
                getUnitTypes(searchBean))) {
            searchBean.add(new ExternalUnitResultBean(unit));
        }
    }

    /**
     * moved from UnitUtils
     */
    private static List<Unit> readExternalUnitsByNameAndTypesStartingAtEarth(final String unitName,
            final List<PartyTypeEnum> types) {
        if (unitName == null) {
            return Collections.emptyList();
        }

        final String nameToSearch = Normalizer.normalize(unitName.replaceAll("%", ".*").toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\s", " ").replaceAll("[^\\p{ASCII}]", "");

        final List<Unit> result = new ArrayList<Unit>();
        for (final UnitName name : Bennu.getInstance().getUnitNameSet()) {

            final String current = Normalizer.normalize(name.getName().toLowerCase(), Normalizer.Form.NFD).replaceAll("\\s", " ")
                    .replaceAll("[^\\p{ASCII}]", "");

            if (current.matches(nameToSearch) && name.getIsExternalUnit() && types.contains(name.getUnit().getType())) {
                result.add(name.getUnit());
            }
        }

        return result;
    }

    private void searchExternalCurricularCourses(final ExternalUnitsSearchBean searchBean) {
        for (final ExternalCurricularCourse externalCurricularCourse : ExternalCurricularCourse
                .readByName(buildNameToSearch(searchBean.getUnitName()))) {
            searchBean.add(new ExternalCurricularCourseResultBean(externalCurricularCourse));
        }
    }

    private List<PartyTypeEnum> getUnitTypes(ExternalUnitsSearchBean searchBean) {
        return (searchBean.getUnitType() == null) ? searchBean.getValidPartyTypes() : Collections
                .singletonList(searchBean.getUnitType());
    }

    public ActionForward viewUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Unit unit = getUnit(request);
        return viewUnit(mapping, request, unit);
    }

    private ActionForward viewUnit(ActionMapping mapping, HttpServletRequest request, final Unit unit) {
        if (unit.isPlanetUnit()) {
            request.setAttribute("searchBean", new ExternalUnitsSearchBean());
            return mapping.findForward("searchExternalUnits");
        } else {
            request.setAttribute("unitResultBean", new ExternalUnitResultBean(unit));
            buildUnitBean(request, unit);
            return mapping.findForward(findForwardNameFor(unit));
        }
    }

    private void buildUnitBean(final HttpServletRequest request, final Unit unit) {
        switch (unit.getType()) {

        case COUNTRY:
            request.setAttribute("universities", ExternalUnitResultBean.buildFrom(unit, PartyTypeEnum.UNIVERSITY));
        case UNIVERSITY:
            request.setAttribute("schools", ExternalUnitResultBean.buildFrom(unit, PartyTypeEnum.SCHOOL));
        case SCHOOL:
            request.setAttribute("departments", ExternalUnitResultBean.buildFrom(unit, PartyTypeEnum.DEPARTMENT));
        case DEPARTMENT:
            request.setAttribute("externalCurricularCourses", ExternalCurricularCourseResultBean.buildFrom(unit));
        default:
            break;
        }
    }

    private String findForwardNameFor(Unit unit) {
        switch (unit.getType()) {
        case COUNTRY:
            return "viewCountryUnit";
        case UNIVERSITY:
            return "viewUniversityUnit";
        case SCHOOL:
            return "viewSchoolUnit";
        case DEPARTMENT:
            return "viewDepartmentUnit";
        default:
            return null;
        }
    }

    private Unit getUnit(final HttpServletRequest request) {
        return getDomainObject(request, "oid");
    }

    public ActionForward prepareCreateCountry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.COUNTRY);
    }

    public ActionForward prepareCreateUniversity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.UNIVERSITY);
    }

    public ActionForward prepareCreateSchool(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.SCHOOL);
    }

    public ActionForward prepareCreateDepartment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.DEPARTMENT);
    }

    private ActionForward prepareCreateUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, PartyTypeEnum type) {

        request.setAttribute("createUnitBean", new CreateExternalUnitBean(getUnit(request), type));
        return mapping.findForward("prepareCreateUnit");
    }

    public ActionForward prepareEditUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("editUnitBean", new EditExternalUnitBean(getUnit(request)));
        return mapping.findForward("prepareEditUnit");
    }

    public ActionForward prepareDeleteUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("unit", getUnit(request));
        return mapping.findForward("prepareDeleteUnit");
    }

    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final CreateExternalUnitBean externalUnitBean = getRenderedObject();

        try {
            final Unit unit = CreateExternalUnit.run(externalUnitBean);
            final String oid = (!externalUnitBean.getParentUnit().isPlanetUnit()) ? externalUnitBean.getParentUnit()
                    .getExternalId() : unit.getExternalId();
            request.setAttribute("oid", oid);
            return viewUnit(mapping, actionForm, request, response);

        } catch (final NotAuthorizedException e) {
            addActionMessage("error", request, "error.notAuthorized");
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        request.setAttribute("createUnitBean", externalUnitBean);
        return mapping.findForward("prepareCreateUnit");
    }

    public ActionForward editExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final EditExternalUnitBean externalUnitBean = getRenderedObject();

        try {
            EditExternalUnit.run(externalUnitBean);
            request.setAttribute("oid", externalUnitBean.getExternalUnit().getExternalId());
            return viewUnit(mapping, actionForm, request, response);

        } catch (final IllegalDataAccessException e) {
            addActionMessage("error", request, "error.notAuthorized");
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        request.setAttribute("editUnitBean", externalUnitBean);
        return mapping.findForward("prepareCreateUnit");
    }

    public ActionForward deleteExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Unit unit = getUnit(request);
        final Unit parent = getAnyParentUnit(unit);

        try {
            DeleteExternalUnit.run(unit);
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
            request.setAttribute("unit", unit);
            return mapping.findForward("prepareDeleteUnit");
        }

        return viewUnit(mapping, request, parent);
    }

    private Unit getAnyParentUnit(final Unit unit) {
        return unit.getParentUnits().iterator().next();
    }

    public ActionForward prepareCreateExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createExternalCurricularCourseBean", new CreateExternalCurricularCourseBean(getUnit(request)));
        return mapping.findForward("prepareCreateExternalCurricularCourse");
    }

    public ActionForward prepareEditExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("editExternalCurricularCourseBean",
                new EditExternalCurricularCourseBean(getExternalCurricularCourse(request)));
        return mapping.findForward("prepareEditExternalCurricularCourse");
    }

    public ActionForward prepareDeleteExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("externalCurricularCourse", getExternalCurricularCourse(request));
        return mapping.findForward("prepareDeleteExternalCurricularCourse");
    }

    public ActionForward createExternalCurricularCoursePostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createExternalCurricularCourseBean", getRenderedObject());
        RenderUtils.invalidateViewState();
        return mapping.findForward("prepareCreateExternalCurricularCourse");
    }

    public ActionForward createExternalCurricularCourseInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createExternalCurricularCourseBean", getRenderedObject());
        return mapping.findForward("prepareCreateExternalCurricularCourse");
    }

    public ActionForward createExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final CreateExternalCurricularCourseBean externalCurricularCourseBean = getRenderedObject();

        try {
            CreateExternalCurricularCourse.run(externalCurricularCourseBean);

            request.setAttribute("oid", externalCurricularCourseBean.getParentUnit().getExternalId());
            return viewUnit(mapping, actionForm, request, response);

        } catch (final NotAuthorizedException e) {
            addActionMessage("error", request, "error.notAuthorized");
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        request.setAttribute("createExternalCurricularCourseBean", externalCurricularCourseBean);
        return mapping.findForward("prepareCreateExternalCurricularCourse");
    }

    public ActionForward editExternalCurricularCoursePostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("editExternalCurricularCourseBean", getRenderedObject());
        RenderUtils.invalidateViewState();
        return mapping.findForward("prepareEditExternalCurricularCourse");
    }

    public ActionForward deleteExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final ExternalCurricularCourse externalCurricularCourse = getExternalCurricularCourse(request);
        final Unit parent = externalCurricularCourse.getUnit();

        try {
            DeleteExternalCurricularCourse.run(externalCurricularCourse);
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
            request.setAttribute("externalCurricularCourse", getExternalCurricularCourse(request));
            return mapping.findForward("prepareDeleteExternalCurricularCourse");
        }

        return viewUnit(mapping, request, parent);
    }

    public ActionForward editExternalCurricularCourseInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("editExternalCurricularCourseBean", getRenderedObject());
        return mapping.findForward("prepareEditExternalCurricularCourse");
    }

    public ActionForward editExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final EditExternalCurricularCourseBean externalCurricularCourseBean = getRenderedObject();

        try {
            EditExternalCurricularCourse.run(externalCurricularCourseBean);

            request.setAttribute("oid", externalCurricularCourseBean.getExternalCurricularCourse().getExternalId());
            return viewExternalCurricularCourse(mapping, actionForm, request, response);

        } catch (final IllegalDataAccessException e) {
            addActionMessage("error", request, "error.notAuthorized");
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        request.setAttribute("editExternalCurricularCourseBean", externalCurricularCourseBean);
        return mapping.findForward("prepareCreateExternalCurricularCourse");
    }

    public ActionForward viewExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final ExternalCurricularCourse externalCurricularCourse = getExternalCurricularCourse(request);
        request.setAttribute("externalCurricularCourseBean", new ExternalCurricularCourseResultBean(externalCurricularCourse));
        request.setAttribute("externalEnrolments", externalCurricularCourse.getExternalEnrolmentsSet());
        return mapping.findForward("viewExternalCurricularCourse");
    }

    private ExternalCurricularCourse getExternalCurricularCourse(final HttpServletRequest request) {
        return getDomainObject(request, "oid");
    }

    public ActionForward prepareEditExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("externalEnrolmentBean", new EditExternalEnrolmentBean(getExternalEnrolment(request)));
        return mapping.findForward("prepareEditExternalEnrolment");
    }

    private ExternalEnrolment getExternalEnrolment(final HttpServletRequest request) {
        return getDomainObject(request, "oid");
    }

    public ActionForward editExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final EditExternalEnrolmentBean externalEnrolmentBean = getRenderedObject();
        try {
            EditExternalEnrolment.run(externalEnrolmentBean, externalEnrolmentBean.getExternalEnrolment().getRegistration());

            request.setAttribute("oid", externalEnrolmentBean.getExternalCurricularCourse().getExternalId());
            return viewExternalCurricularCourse(mapping, actionForm, request, response);

        } catch (final IllegalDataAccessException e) {
            addActionMessage("error", request, "error.notAuthorized");
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        request.setAttribute("externalEnrolmentBean", externalEnrolmentBean);
        return mapping.findForward("prepareEditExternalEnrolment");
    }

}