<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>

<tiles:useAttribute name="titleString" id="titleK" ignore="true" />
<tiles:useAttribute name="bundle" id="bundleT" ignore="true" />
<logic:present name="bundleT">
	<logic:present name="titleK">
		<bean:message name="titleK" bundle="<%= bundleT.toString() %>" /> -
		<bean:message key="institution.name" bundle="GLOBAL_RESOURCES" />
	</logic:present>
</logic:present>
<logic:notPresent name="bundleT">
	<logic:present name="<%= FilterFunctionalityContext.CONTEXT_KEY %>">
		<bean:define id="funcContext" name="<%= FilterFunctionalityContext.CONTEXT_KEY %>" property="selectedContent" type="net.sourceforge.fenixedu.domain.contents.Content"/>
		<bean:define id="contentContext" name="<%= FilterFunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" type="net.sourceforge.fenixedu.domain.contents.Content"/>
		<logic:equal name="contentContext" property="unitSite" value="true">
			<bean:write name="funcContext" property="name" /> -
			<bean:write name="contentContext" property="unit.partyName"/> -
		</logic:equal>
	</logic:present>
</logic:notPresent>

<logic:notPresent name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <jsp:include page="/commons/blank.jsp"/>
</logic:notPresent>