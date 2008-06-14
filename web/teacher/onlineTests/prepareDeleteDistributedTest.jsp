<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="Javascript" type="text/javascript">
<!--
function changeMethod(){
	document.forms[0].method.value="showDistributedTests";
}
// -->
</script>

<logic:present name="canDelete">
	<h2><bean:message key="link.removeTest"/></h2>
	<html:form action="/testDistribution">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteDistributedTest"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
		<br/>
		<logic:equal name="canDelete" value="true">
			<bean:message key="message.confirm.deleteDistributedTest"/>
			<br/>
			<br/>
			<table align="center">
				<tr>
					<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.confirm"/></html:submit></td>
					<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="changeMethod()"><bean:message key="label.back"/></html:submit></td>
				</tr>
			</table>
		</logic:equal>

		<logic:notEqual name="canDelete" value="true">
			<bean:message key="message.cantDeleteDistributedTest"/>
			<br/>
			<br/>
			<table align="center">
				<tr>
					<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="changeMethod()"><bean:message key="label.back"/></html:submit></td>
				</tr>
			</table>
		</logic:notEqual>
	</html:form>
</logic:present>