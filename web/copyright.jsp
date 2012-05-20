<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<p>
	&copy; 
	<dt:format pattern="yyyy"><dt:currentTime/></dt:format>,
	<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>.
	<bean:message key="footer.copyright.alrightsreserved" bundle="GLOBAL_RESOURCES"/>.
	|
	<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
		<bean:define id="contextId" name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedTopLevelContainer.idInternal" />
		<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>
		<a href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" + "&contextId=" + contextId %>" target="_blank">
			<bean:message key="message.footer.help.support.form" bundle="GLOBAL_RESOURCES"/></a>.
	</logic:present>
	<logic:notPresent name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
		<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>
		<a href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" %>" target="_blank">
			<bean:message key="message.footer.help.support.form" bundle="GLOBAL_RESOURCES"/></a>.
	</logic:notPresent>
</p>