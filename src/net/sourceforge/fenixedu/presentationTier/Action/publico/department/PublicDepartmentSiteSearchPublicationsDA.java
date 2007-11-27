package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean.SearchElement;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.publico.SearchPublicationsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;

public class PublicDepartmentSiteSearchPublicationsDA extends SearchPublicationsDA {

    @Override
    public ActionForward prepareSearchPublication(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	SearchDSpaceBean bean = createNewBean();
	bean.addSearchElement();

	String siteID = request.getParameter("siteID");
	String name = rootDomainObject.readUnitNameByOID(new Integer(siteID)).getName();

	SearchElement searchElement = bean.getSearchElements().get(0);
	searchElement.setSearchField(SearchField.UNIT);
	searchElement.setQueryValue(name);

	request.setAttribute("bean", bean);
	
	return mapping.findForward("SearchPublication");
    }

    @Override
    protected void setRequestDomainObject(HttpServletRequest request) {
	Integer unitId = getIntegerFromRequest(request, "selectedDepartmentUnitID");
	Unit unit = (Unit) rootDomainObject.readPartyByOID(unitId);
	
	Department department = unit.getDepartment();
	request.setAttribute("department", department);
    }

}
