<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.title" /></h2>
<bean:define id="error" name="error" type="net.sourceforge.fenixedu.domain.log.requests.RequestLog"/>
<bean:define id="errorLog" name="error" property="errorLog" />
<bean:define id="exception" name="errorLog" property="exception" type="net.sourceforge.fenixedu.domain.log.requests.ExceptionType"/>

<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.exception" /></b>: <html:link page="<%= "/errorReport.do?method=filter&filterBy=exception&parameter=" + exception.getExternalId() + "&page=1"%>"> <bean:write name="exception" property="type" /></html:link></p> 
<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.date" /></b>: <%= error.getRequestTime().toString("dd/MM/yyyy, HH:mm:ss") %></p>

<logic:present name="error" property="requester">
<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.user" /></b>: <html:link page="<%= "/errorReport.do?method=filter&filterBy=user&parameter=" + error.getRequester() + "&page=1"%>"> <bean:write name="error" property="requester" /></html:link>
</logic:present>
<logic:notPresent name="error" property="requester">
<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.user" /></b>: N/A</p>
</logic:notPresent>
<logic:present name="error" property="mapping">
<bean:define id="mapping" name="error" property="mapping" type="net.sourceforge.fenixedu.domain.log.requests.RequestMapping"/> 
<p><b>URL</b>: <html:link page="<%= "/errorReport.do?method=filter&filterBy=url&parameter=" + mapping.getExternalId() + "&page=1"%>"><bean:write name="mapping" property="path" />?<bean:write name="error" property="queryString" /></html:link></p>
</logic:present>
<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.requestParam" /></b>:</p>

<table class="tstyle1 thleft thlight mtop05">
<tr><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.key" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.value" /></th></tr>
<logic:iterate id="obj" name="requestParam">
<tr>
	<bean:define id="obj" name="obj"/>
	<td><%= ((String[]) obj)[0] %></td>
	<td><%= ((String[]) obj)[1] %></td>
</logic:iterate>
</table>

<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.attributeParam" /></b>:</p>

<table class="tstyle1 thleft thlight mtop05">
<tr><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.key" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.value" /></th></tr>
<logic:iterate id="obj" name="requestAttr">
<tr>
	<bean:define id="obj" name="obj"/>
	<td><%= ((String[]) obj)[0] %></td>
	<td><%= ((String[]) obj)[1] %></td>
</logic:iterate>
</table>


<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.sessionAttribute" /></b>:</p>

<table class="tstyle1 thleft thlight mtop05">
<tr><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.key" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.key" /></th></tr>
<logic:iterate id="obj" name="sessionAttr">
<tr>
	<bean:define id="obj" name="obj"/>
	<td><%= ((String[]) obj)[0] %></td>
	<td><%= ((String[]) obj)[1] %></td>
</logic:iterate>
</table>

<logic:present name="errorLog" property="referer">
<p><b><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.referer" /></b>: <bean:write name="error" property="referer" /></p>
</logic:present>




<p><b>StackTrace</b>:</p>
<script type="text/javascript" src="http://jqueryjs.googlecode.com/files/jquery-1.3.2.min.js"></script>
<script type="text/javascript">
function showOnlyFenix() {
	$(".stackFrame").each(function (){
		var pack = $(this).children(".package");
		if (!pack.html().match("^net.sourceforge.fenixedu.*")){
			$(this).hide();
		}
		
	});
	$(".hide-link").each(function (){
		$(this).attr("onClick","showAll()");
		$(this).html("Mostrar Todos");
	});
}
function showAll() {
	$(".stackFrame").each(function (){
		$(this).show();
		$(".hide-link").each(function (){
			$(this).attr("onClick","showOnlyFenix()");
			$(this).html("Apenas relativo ao fenix");
		})
	})
}
</script>
<a class="hide-link" href="#" onClick="showOnlyFenix()"><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.related" /></a>
<table class="tstyle1 thleft thlight mtop05">
<tr><th>#</th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.package" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.class" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.method" /></th><th><bean:message bundle="MANAGER_RESOURCES" key="label.errorDetail.file" /></th></tr>
<bean:define id="stackTrace" name="stackTrace" />
<% int i = 0; %>
<logic:iterate id="stackFrame" name="stackTrace">
<tr class="stackFrame">
	<bean:define id="stackFrame" name="stackFrame"/>
	<td><%= i %></td>
	<td class="package"><%= ((String[]) stackFrame)[0] %></td>
	<td><%= ((String[]) stackFrame)[1] %></td>
	<td><%= ((String[]) stackFrame)[2] %></td>
	<td><%= ((String[]) stackFrame)[3] + ":" + ((String[]) stackFrame)[4] %></td>
</tr>
<% i++; %>
</logic:iterate>
</table>
