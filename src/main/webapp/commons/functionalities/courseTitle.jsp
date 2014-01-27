<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<logic:present name="<%= FunctionalityContext.CONTEXT_KEY %>">
	<bean:define id="funcContext" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContent" type="net.sourceforge.fenixedu.domain.contents.Content"/>
	<bean:write name="funcContext" property="name" /> -
</logic:present>
<logic:notPresent name="<%= FunctionalityContext.CONTEXT_KEY %>">
	<tiles:getAsString name="title" ignore="true" />
</logic:notPresent>
<logic:present name="executionCourse">
	<bean:write name="executionCourse" property="name"/> -
</logic:present>
