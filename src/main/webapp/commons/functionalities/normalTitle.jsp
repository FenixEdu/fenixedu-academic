<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>

<tiles:useAttribute name="titleString" id="titleK" ignore="true" />
<tiles:useAttribute name="bundle" id="bundleT" ignore="true" />
<logic:present name="bundleT">
	<logic:present name="titleK">
		<bean:message name="titleK" bundle="<%= bundleT.toString() %>" /> -
		<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
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