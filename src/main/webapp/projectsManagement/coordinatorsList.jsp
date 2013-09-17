<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<bean:define id="reportType" value="<%=request.getAttribute("reportType").toString()%>" />

<bean:define id="backendInstance" name="backendInstance" type="net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance"/>
<bean:define id="backendInstanceUrl" type="java.lang.String">&amp;backendInstance=<%= backendInstance.name() %></bean:define>


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
	<bean:define id="code" value="" />
		<logic:present name="infoCostCenter" scope="request">
			<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
			<bean:define id="code" value="<%="&amp;costCenter="+cc.toString()%>" />
		</logic:present>
		<logic:present name="it" scope="request">
			<logic:equal name="it" value="true">
				<bean:define id="code" value="<%=code+"&amp;backendInstance=IST"%>" />
			</logic:equal>
		</logic:present>
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.mecanographicNumber" /></th>
				<th class="listClasses-header"><bean:message key="label.name" /></th>
			</tr>
			<logic:iterate id="coordinator" name="coordinatorsList">
				<bean:define id="coordinatorCode" name="coordinator" property="code" />
				<tr>
					<td class="listClasses"><html:link page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;coordinatorCode="+coordinatorCode+code+backendInstanceUrl%>">
						<bean:write name="coordinator" property="code" />
					</html:link></td>
					<td class="listClasses"><html:link page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;coordinatorCode="+coordinatorCode+code+backendInstanceUrl%>">
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
