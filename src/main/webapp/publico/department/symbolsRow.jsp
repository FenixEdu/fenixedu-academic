<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ page import="net.sourceforge.fenixedu.domain.UnitSite"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
		final String finalLanguage = Language.getLanguage().name();
    	//String finalLanguage = language == null ? "pt" : String.valueOf(language);
        request.setAttribute("siteDefaultLogo", 
        	String.format("%s/images/newImage2012/%s_%s.png", request.getContextPath(), unit.getAcronym(), finalLanguage));
    }
%>
<jsp:include page="../customized/symbolsRow.jsp"/>
