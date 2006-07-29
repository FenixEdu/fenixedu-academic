<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

 <strong>Página 7 de 7</strong>
<p align="center"><span class="error"><html:errors/></span></p>

<html:form action="/declaration.do?method=logOff" >
<table align="center" border="0" cellpadding="5" cellspacing="5">
<tr>
	<td class="listClasses"><h3><bean:message key="label.registration.declarations"/></h3></td>
	<td class="listClasses">
		<html:link page="<%="/declaration.do?method=printDeclaration&amp;degreeName=" +
			request.getAttribute("degreeName")%>" target="_blank">
			<bean:message key="link.registration.print"/></html:link>
	</td>
</tr>
<tr>
	<td class="listClasses">
		<h3><bean:message key="link.my.timetable"/></h3>
	</td>
	<td class="listClasses">
		<html:link page="/studentTimeTable.do" target="_blank" >
		<bean:message key="link.registration.print"/></html:link>
	</td>
</tr>
</table>
<br/><br/>
<p align="center"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Terminar" styleClass="inputbutton"/></p>
</html:form>