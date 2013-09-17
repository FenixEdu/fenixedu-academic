<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
	<bean:write name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>"
		property="selectedTopLevelContainer.name"/> -
	<logic:present name="<%= net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext.CONTEXT_KEY %>">
        <bean:define id="funcContext" name="<%= net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext.CONTEXT_KEY %>" property="selectedContent" type="net.sourceforge.fenixedu.domain.contents.Content"/>
        <bean:write name="funcContext" property="name" />
    </logic:present>
</logic:present>

<logic:notPresent name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <jsp:include page="/commons/blank.jsp"/>
</logic:notPresent>