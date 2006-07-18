<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<bean:define id="reportType" value="<%=request.getAttribute("reportType").toString()%>" />
<logic:present name="infoCostCenter" scope="request">
	<table class="viewHeader">
		<tr>
			<td>
			<h3><bean:message key="title.projects" />&nbsp;-&nbsp;<bean:write name="infoCostCenter" property="description" /></h3>
			</td>
		</tr>
	</table>
	<br />
	<blockquote>
	<h2>
</logic:present>
<logic:notPresent name="infoCostCenter" scope="request">
	<blockquote>
	<h2><bean:message key="title.projects" />&nbsp;-&nbsp;
</logic:notPresent>
<bean:message key="<%="title."+reportType%>" />
</h2>
</blockquote>
<br />
<br />
<logic:present name="projectList">
	<logic:notEmpty name="projectList">
		<bean:define id="code" value="" />
		<logic:present name="infoCostCenter" scope="request">
			<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
			<bean:define id="code" value="<%="&amp;costCenter="+cc.toString()%>" />
		</logic:present>
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.projectNumber" /></th>
				<th class="listClasses-header"><bean:message key="label.acronym" /></th>
			</tr>
			<logic:iterate id="project" name="projectList">
				<bean:define id="projectCode" name="project" property="projectCode" />
				<tr>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;projectCode="+projectCode+code%>">
						<bean:write name="project" property="projectIdentification" />
					</html:link></td>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;projectCode="+projectCode+code%>">
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
