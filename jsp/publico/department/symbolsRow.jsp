<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ page import="net.sourceforge.fenixedu.domain.UnitSite"%>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>

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
    	String finalLanguage = language == null ? "pt" : String.valueOf(language);
        request.setAttribute("siteDefaultLogo", 
        	String.format("%s/images/departments/%s_%s.gif", request.getContextPath(), unit.getAcronym(), finalLanguage));
    }
%>

<jsp:include page="../customized/symbolsRow.jsp"/>
