package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.externalUnits;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalCurricularCourseBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalUnitBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalUnitResultBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalUnitsSearchBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
	return "%" + name.replaceAll("[ ]", "%") + "%";
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
    
    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateExternalUnitBean externalUnitBean = (CreateExternalUnitBean) getRenderedObject();
	
	try {
	    final Unit unit = (Unit) executeService("CreateExternalUnit", new Object[] {externalUnitBean});
	    final Integer oid = (externalUnitBean.getParentUnit().getType() != PartyTypeEnum.PLANET) ? 
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
    
    public ActionForward prepareCreateExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createExternalCurricularCourseBean", new CreateExternalCurricularCourseBean(getUnit(request)));
	return mapping.findForward("prepareCreateExternalCurricularCourse");
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
    
    public ActionForward prepareCreateExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("externalEnrolmentBean", new CreateExternalEnrolmentBean(getExternalCurricularCourse(request)));
	return mapping.findForward("prepareCreateExternalEnrolment");
    }
    
    public ActionForward createExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateExternalEnrolmentBean externalEnrolmentBean = (CreateExternalEnrolmentBean) getRenderedObject();
	final Student student = Student.readStudentByNumber(externalEnrolmentBean.getStudentNumber());	
	try {
	    executeService("CreateExternalEnrolment", new Object[] {externalEnrolmentBean, student});
	    
	    request.setAttribute("oid", externalEnrolmentBean.getExternalCurricularCourse().getIdInternal());
	    return viewExternalCurricularCourse(mapping, actionForm, request, response);
	    
	} catch (final NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	}

	request.setAttribute("externalEnrolmentBean", externalEnrolmentBean);
	return mapping.findForward("prepareCreateExternalEnrolment");	
    }

}
