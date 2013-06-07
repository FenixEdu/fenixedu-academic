<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>

<logic:present name="<%= FilterFunctionalityContext.CONTEXT_KEY %>">
	<bean:define id="funcContext" name="<%= FilterFunctionalityContext.CONTEXT_KEY %>" property="selectedContent" type="net.sourceforge.fenixedu.domain.contents.Content"/>
	<bean:write name="funcContext" property="name" /> -
</logic:present>
<logic:notPresent name="<%= FilterFunctionalityContext.CONTEXT_KEY %>">
	<tiles:getAsString name="title" ignore="true" />
</logic:notPresent>
<logic:present name="executionCourse">
	<bean:write name="executionCourse" property="name"/> -
</logic:present>
