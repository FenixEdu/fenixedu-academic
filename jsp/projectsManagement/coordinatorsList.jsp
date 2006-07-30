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
<logic:present name="coordinatorsList">
	<logic:notEmpty name="coordinatorsList">
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.mecanographicNumber" /></th>
				<th class="listClasses-header"><bean:message key="label.name" /></th>
			</tr>
			<logic:iterate id="coordinator" name="coordinatorsList">
				<bean:define id="coordinatorCode" name="coordinator" property="code" />
				<tr>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;coordinatorCode="+coordinatorCode%>">
						<bean:write name="coordinator" property="code" />
					</html:link></td>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;coordinatorCode="+coordinatorCode%>">
						<bean:write name="coordinator" property="description" />
					</html:link></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="coordinatorsList">
		<span class="error"><!-- Error messages go here --><bean:message key="message.noUserProjects" /></span>
	</logic:empty>
</logic:present>
