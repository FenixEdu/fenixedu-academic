package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.externalUnits;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalCurricularCourseBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalUnitBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalCurricularCourseBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalUnitBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalUnitResultBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalUnitsSearchBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ExternalUnitsDispatchAction extends FenixDispatchAction {
    
    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	final ExternalUnitsSearchBean searchBean = new ExternalUnitsSearchBean();
	request.setAttribute("searchBean", searchBean);
	return mapping.findForward("searchExternalUnits");
    }
    
    public ActionForward search(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	final ExternalUnitsSearchBean searchBean = (ExternalUnitsSearchBean) getRenderedObject();
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
	for (final Unit unit : UnitUtils.readExternalUnitsByNameAndTypesStartingAtEarth(buildNameToSearch(searchBean.getUnitName()), getUnitTypes(searchBean))) {
	    searchBean.add(new ExternalUnitResultBean(unit));
	}
    }
    
    private void searchExternalCurricularCourses(final ExternalUnitsSearchBean searchBean) {
	for (final ExternalCurricularCourse externalCurricularCourse : ExternalCurricularCourse.readByName(buildNameToSearch(searchBean.getUnitName()))) {
	    searchBean.add(new ExternalCurricularCourseResultBean(externalCurricularCourse));
	}
    }

    private List<PartyTypeEnum> getUnitTypes(ExternalUnitsSearchBean searchBean) {
	return (searchBean.getUnitType() == null) ? searchBean.getValidPartyTypes() : Collections.singletonList(searchBean.getUnitType());   
    }
    
    public ActionForward viewUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final Unit unit = getUnit(request);
	return viewUnit(mapping, request, unit);
    }

    private ActionForward viewUnit(ActionMapping mapping, HttpServletRequest request, final Unit unit) {
	request.setAttribute("unitResultBean", new ExternalUnitResultBean(unit));
	buildUnitBean(request, unit);
	return mapping.findForward(findForwardNameFor(unit));
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
	return (Unit) readDomainObject(request, Unit.class, getIntegerFromRequest(request, "oid"));
    }
    
    public ActionForward prepareCreateCountry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.COUNTRY);
    }
    
    public ActionForward prepareCreateUniversity(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.UNIVERSITY);
    }
    
    public ActionForward prepareCreateSchool(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.SCHOOL);
    }
    
    public ActionForward prepareCreateDepartment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreateUnit(mapping, actionForm, request, response, PartyTypeEnum.DEPARTMENT);
    }
    
    private ActionForward prepareCreateUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response, PartyTypeEnum type) {
	
	request.setAttribute("createUnitBean", new CreateExternalUnitBean(getUnit(request), type));
	return mapping.findForward("prepareCreateUnit");
    }
    
    public ActionForward prepareEditUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	request.setAttribute("editUnitBean", new EditExternalUnitBean(getUnit(request)));
	return mapping.findForward("prepareEditUnit");
    }
    
    public ActionForward prepareDeleteUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	
	request.setAttribute("unit", getUnit(request));
	return mapping.findForward("prepareDeleteUnit");
    }
    
    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateExternalUnitBean externalUnitBean = (CreateExternalUnitBean) getRenderedObject();
	
	try {
	    final Unit unit = (Unit) executeService("CreateExternalUnit", new Object[] {externalUnitBean});
	    final Integer oid = (!externalUnitBean.getParentUnit().isPlanetUnit()) ? 
		    externalUnitBean.getParentUnit().getIdInternal() : unit.getIdInternal(); 
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
    
    public ActionForward editExternalUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final EditExternalUnitBean externalUnitBean = (EditExternalUnitBean) getRenderedObject();
	
	try {
	    executeService("EditExternalUnit", new Object[] {externalUnitBean});
            request.setAttribute("oid", externalUnitBean.getExternalUnit().getIdInternal());
	    return viewUnit(mapping, actionForm, request, response);
	    
	} catch (final NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	}
	
	request.setAttribute("editUnitBean", externalUnitBean);
	return mapping.findForward("prepareCreateUnit");
    }
    
    public ActionForward deleteExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Unit unit = getUnit(request);
	final Unit parent = getAnyParentUnit(unit);
	
	try {
	    executeService("DeleteExternalUnit", unit);
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

	request.setAttribute("editExternalCurricularCourseBean", new EditExternalCurricularCourseBean(getExternalCurricularCourse(request)));
	return mapping.findForward("prepareEditExternalCurricularCourse");
    }
    
    public ActionForward prepareDeleteExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	
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
    
    public ActionForward createExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateExternalCurricularCourseBean externalCurricularCourseBean = (CreateExternalCurricularCourseBean) getRenderedObject();
	
	try {
	    executeService("CreateExternalCurricularCourse", new Object[] {externalCurricularCourseBean});
	    
	    request.setAttribute("oid", externalCurricularCourseBean.getParentUnit().getIdInternal());
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
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
	final ExternalCurricularCourse externalCurricularCourse = getExternalCurricularCourse(request);
	final Unit parent = externalCurricularCourse.getUnit();
	
	try {
	    executeService("DeleteExternalCurricularCourse", externalCurricularCourse);
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
    
    public ActionForward editExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final EditExternalCurricularCourseBean externalCurricularCourseBean = (EditExternalCurricularCourseBean) getRenderedObject();
	
	try {
	    executeService("EditExternalCurricularCourse", new Object[] {externalCurricularCourseBean});
	    
	    request.setAttribute("oid", externalCurricularCourseBean.getExternalCurricularCourse().getIdInternal());
	    return viewExternalCurricularCourse(mapping, actionForm, request, response);
	    
	} catch (final NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	}
	
	request.setAttribute("editExternalCurricularCourseBean", externalCurricularCourseBean);
	return mapping.findForward("prepareCreateExternalCurricularCourse");
    }
    
    public ActionForward viewExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final ExternalCurricularCourse externalCurricularCourse = getExternalCurricularCourse(request);
	request.setAttribute("externalCurricularCourseBean", new ExternalCurricularCourseResultBean(externalCurricularCourse));
	request.setAttribute("externalEnrolments", externalCurricularCourse.getExternalEnrolments());
	return mapping.findForward("viewExternalCurricularCourse");
    }
    
    private ExternalCurricularCourse getExternalCurricularCourse(final HttpServletRequest request) {
	return (ExternalCurricularCourse) readDomainObject(request, ExternalCurricularCourse.class, getIntegerFromRequest(request, "oid"));
    }
        
    public ActionForward prepareEditExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("externalEnrolmentBean", new EditExternalEnrolmentBean(getExternalEnrolment(request)));
	return mapping.findForward("prepareEditExternalEnrolment");
    }
    
    private ExternalEnrolment getExternalEnrolment(final HttpServletRequest request) {
	return (ExternalEnrolment) readDomainObject(request, ExternalEnrolment.class, getIntegerFromRequest(request, "oid"));
    }

    public ActionForward editExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final EditExternalEnrolmentBean externalEnrolmentBean = (EditExternalEnrolmentBean) getRenderedObject();
	try {
	    executeService("EditExternalEnrolment", new Object[] { externalEnrolmentBean,
		    externalEnrolmentBean.getExternalEnrolment().getRegistration() });
	    
	    request.setAttribute("oid", externalEnrolmentBean.getExternalCurricularCourse().getIdInternal());
	    return viewExternalCurricularCourse(mapping, actionForm, request, response);
	    
	} catch (final NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	}

	request.setAttribute("externalEnrolmentBean", externalEnrolmentBean);
	return mapping.findForward("prepareEditExternalEnrolment");	
    }

}
