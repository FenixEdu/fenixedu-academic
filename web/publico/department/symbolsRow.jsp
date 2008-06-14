<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ page import="net.sourceforge.fenixedu.domain.UnitSite"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>

<%
	FilterFunctionalityContext context = (FilterFunctionalityContext) AbstractFunctionalityContext.getCurrentContext(request);
	UnitSite site = (UnitSite) context.getSelectedContainer();
	Unit unit = site.getUnit();
		
    request.setAttribute("unit", unit);
    request.setAttribute("site", site);
	
    if (site != null && site.isDefaultLogoUsed()) {
    	String finalLanguage = language == null ? "pt" : String.valueOf(language);
        request.setAttribute("siteDefaultLogo", 
        	String.format("%s/images/departments/%s_%s.gif", request.getContextPath(), unit.getAcronym(), finalLanguage));
    }
%>

<jsp:include page="../customized/symbolsRow.jsp"/>
