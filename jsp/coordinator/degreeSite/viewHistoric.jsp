<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<p><span class="error"><html:errors/></span></p>

<h2>
<bean:message key="label.historic" />&nbsp;<bean:message key="label.the" />&nbsp;<bean:message key="label.site" />&nbsp;<bean:message key="label.the" />&nbsp;<bean:message key="label.degree" />&nbsp;
</h2>

<logic:present name="infoExecutionDegrees">
<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees">
	<bean:define id="executionDegreeId" name="infoExecutionDegree" property="idInternal" />
	<p>
		<html:link href="<%= request.getContextPath()+"/publico/showDegreeSite.do?method=showDescription&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") %>" target="_blank">
			<bean:message key="link.coordinator.degreeSite.viewSite" />&nbsp;<bean:message key="label.the" />&nbsp;<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>
		</html:link>
	</p>
</logic:iterate>
</logic:present>

<logic:notPresent name="infoExecutionDegrees">
	<h1><bean:message key="error.coordinator.noHistoric" /></h1>
</logic:notPresent>