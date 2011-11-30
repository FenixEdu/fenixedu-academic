<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<html:xhtml/>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="centralApplicationsUrl" type="java.lang.String"><bean:message key="centralApplications.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="supportLink" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="contextId" name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedTopLevelContainer.idInternal" />

<ul>
	<li class="institution">
		<a href="<%= institutionUrl %>" target="_blank"><bean:message key="label.institution" bundle="GLOBAL_RESOURCES"/></a>
	</li>
	<li class="centralApplications">
		<a href="<%= centralApplicationsUrl %>" target="_blank"><bean:message key="label.central.applications" bundle="GLOBAL_RESOURCES"/></a>
	</li>
	<li class="support">
		<a href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" + "&contextId=" + contextId %>" target="_blank">
			<bean:message key="link.suporte" bundle="GLOBAL_RESOURCES"/>
		</a>
	</li>
	<li class="suggestion">
		<a href="https://fears.ist.utl.pt/#Project3" target="_blank">
			<bean:message key="link.suggestions" bundle="GLOBAL_RESOURCES"/>
		</a>
	</li>
	<li class="logout">
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() %>/logoff.do"><bean:message key="link.logout" bundle="GLOBAL_RESOURCES"/></a>
	</li>
</ul>
