<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<bean:define id="reportType" value="<%=request.getAttribute("reportType").toString()%>" />
<h2><bean:message key="title.projects" />&nbsp;-&nbsp;<bean:message key="<%="title."+reportType%>" /></h2>
<br />
<br />
<logic:present name="projectList">
	<logic:notEmpty name="projectList">
		<table>
			<tr>
				<td class="listClasses-header"><bean:message key="label.projectNumber" /></td>
				<td class="listClasses-header"><bean:message key="label.acronym" /></td>
			</tr>
			<logic:iterate id="project" name="projectList">
				<bean:define id="projectCode" name="project" property="projectCode" />
				<tr>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;projectCode="+projectCode%>">
						<bean:write name="project" property="projectIdentification" />
					</html:link></td>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;projectCode="+projectCode%>">
						<bean:write name="project" property="title" />
					</html:link></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="projectList">
		<span class="error"><bean:message key="message.noUserProjects" /></span>
	</logic:empty>
</logic:present>
