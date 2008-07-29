<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<p>
	&copy; 
	<dt:format pattern="yyyy"><dt:currentTime/></dt:format>,
	<bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/>.
	<bean:message key="footer.copyright.alrightsreserved" bundle="GLOBAL_RESOURCES"/>.
	|
	<bean:define id="contextId" name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedTopLevelContainer.idInternal" />
	<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>
	<a href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" + "&contextId=" + contextId %>" target="_blank">
		<bean:message key="message.footer.help.support.form" bundle="GLOBAL_RESOURCES"/></a>.
	
	
</p>