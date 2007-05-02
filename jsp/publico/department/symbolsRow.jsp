<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ page import="net.sourceforge.fenixedu.domain.UnitSite"%>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%
    Integer departmentUnitId;
    Unit unit = (Unit) request.getAttribute("unit");

    if (unit == null) {
        departmentUnitId = new Integer(request.getParameter("selectedDepartmentUnitID"));
        unit = (Unit) RootDomainObject.getInstance().readPartyByOID(departmentUnitId);
    }

    UnitSite site = unit.getSite();
    request.setAttribute("unit", unit);
    request.setAttribute("site", site);
	
    if (site != null && site.isDefaultLogoUsed()) {
        request.setAttribute("siteDefaultLogo", 
        	String.format("%s/images/departments/%s.gif", request.getContextPath(), unit.getAcronym()));
    }
%>

<jsp:include page="../customized/symbolsRow.jsp"/>
