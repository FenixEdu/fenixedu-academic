<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="deleteDistributedTest"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
<br/>
<logic:equal name="canDelete" value="true">
	<bean:message key="message.confirm.deleteDistributedTest"/>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton"><bean:message key="button.confirm"/></html:submit></td></html:form>
</logic:equal>

<logic:notEqual name="canDelete" value="true">
	<bean:message key="message.cantDeleteDistributedTest"/>
	<br/>
	<br/>
	<table align="center">
	<tr>
</logic:notEqual>
	<td><html:submit styleClass="inputbutton" onclick="changeMethod()"><bean:message key="label.back"/></html:submit></td>
	</tr>
	</table>
</html:form>
</logic:present>