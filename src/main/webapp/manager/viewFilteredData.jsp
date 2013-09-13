<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.searchFor" /></h2>

<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.filterby" /></b>: <bean:write name="filterBy"/></p>
<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.param" /></b>: <bean:write name="param"/></p> 
<p><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.message1" /> <b><bean:write name="size"/></b> <bean:message bundle="MANAGER_RESOURCES" key="label.errorList.message2" /></p>

<table class="tstyle1 thleft thlight mtop05">
<tr><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.date" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.exception" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.user" /></th><th></th></tr>
<logic:iterate id="error" name="requestLogs" type="net.sourceforge.fenixedu.domain.log.requests.RequestLog">
<tr>
<bean:define id="errorLog" name="error" property="errorLog" />
<bean:define id="exception" name="errorLog" property="exception" />
<td><%= error.getRequestTime().toString("dd/MM/yyyy HH:mm:ss") %></td>
<td><bean:write name="exception" property="type" /></td>
<td><bean:write name="error" property="requester" /></td>
<td><a href="<%= "/ciapl/manager/errorReport.do?method=reportDetail&oid=" + error.getExternalId()  %>"><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.detail" /></a></td>

</tr>
</logic:iterate>
</table>


