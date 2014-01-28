<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<p>
	&copy; 
	<dt:format pattern="yyyy"><dt:currentTime/></dt:format>,
	<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>.
	<bean:message key="footer.copyright.alrightsreserved" bundle="GLOBAL_RESOURCES"/>.
	|
	<logic:present name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY  %>">
		<bean:define id="contextId" name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>" property="externalId" />
		<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>
		<a href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" + "&contextId=" + contextId %>" target="_blank">
			<bean:message key="message.footer.help.support.form" bundle="GLOBAL_RESOURCES"/></a>.
	</logic:present>
	<logic:notPresent name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>">
		<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>
		<a href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" %>" target="_blank">
			<bean:message key="message.footer.help.support.form" bundle="GLOBAL_RESOURCES"/></a>.
	</logic:notPresent>
</p>