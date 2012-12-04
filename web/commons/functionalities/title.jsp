<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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