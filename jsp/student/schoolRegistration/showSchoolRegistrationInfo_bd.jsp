<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<bean:define id="infoEnrollments" name="infoEnrollments" scope="request"/>
<bean:define id="infoClass" name="infoClass" scope="request"/>

<%-- <html:form action="<%= "/viewEnrollments?method=declarations&amp;degreeName="+ request.getAttribute("degreeName")%>"> --%>
<html:form action="/declaration.do?method=logOff" >

<p><strong>Página 6 de 6</strong></p>

<p align="center"><span class="error"><html:errors/></span></p>

<div style="text-align: center; width: 60%; margin: 0 19%;">
	<logic:present name="studentRegistered" scope="request">
		<p><span style="color: #070; padding: 1em;"><strong><bean:message key="label.register.success1" /></strong></span></p>
		<p style="background-color: #ffd; padding: 0.5em;"><strong><bean:message key="label.register.success3" /></strong></p>
	</logic:present>
	<logic:notPresent name="studentRegistered" scope="request">
		<p><span style="background-color: #cfc; color: #070; padding: 1em;"><strong><bean:message key="label.register.success1" /></strong></span></p>
		<p><b><bean:message key="label.register.success2" /></b></p>
	</logic:notPresent>
</div>

<div style="width: 70%; margin: 0 14%; border-top: 1px solid #aaa;">

<p align="center"><em><bean:message key="label.enrollments" /></em></p>

	<table class="table1" align="center">
		<tr>
			<th class="listClasses-header">
			<bean:message key="label.curricular.course.name" bundle="DEFAULT"/>
			</th>
		</tr>
	<logic:iterate id="enrolment" name="infoEnrollments">
	  	<tr>
			<td class="listClasses">
			<bean:write name="enrolment" property="infoCurricularCourse.name"/>
			</td>
		</tr>
	</logic:iterate>
</table>

<br/>

<table align="center" border="0" cellpadding="5" cellspacing="1">
	<tr>
		<td class="listClasses"><strong><bean:message key="label.registration.declarations"/></strong></td>
		<td class="listClasses">
			<html:link page="<%="/declaration.do?method=printDeclaration&amp;degreeName=" +
				request.getAttribute("degreeName")%>" target="_blank">
				<bean:message key="link.registration.print"/></html:link>
		</td>
	</tr>
	<tr>
		<td class="listClasses"><strong><bean:message key="link.my.timetable"/></strong></td>
		<td class="listClasses">
			<html:link page="<%="/studentTimeTable.do?method=printSchedule&amp;degreeName=" +
				request.getAttribute("degreeName")%>" target="_blank" >
			<bean:message key="link.registration.print"/></html:link>
		</td>
	</tr>
</table>

</div>

<br/><br/>

<p align="center"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Terminar" styleClass="inputbutton"/></p>
</html:form>
