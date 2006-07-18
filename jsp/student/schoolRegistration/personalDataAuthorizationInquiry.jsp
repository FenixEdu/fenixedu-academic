<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice" %>

<p><strong>Página 3 de 6</strong></p>

<div style="width:79%; margin: 0 10%;">

<html:form action="/dislocatedStudent?method=prepareDislocatedStudentInquiry">
	<h2 style="text-align: center;"><bean:message key="label.schoolRegistration.personalData.inquiry"/></h2>
	<span class="error"><html:errors/></span>
	<br/>

<br/>

<div class="infoop">

	<b><bean:message key="label.enrollment.personalData.authorization" bundle="STUDENT_RESOURCES"/></b>
	<br/>
	<br/>
	<table>
		<tr>
			<td>&nbsp;</td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.PROFESSIONAL_ENDS.toString() %>" /></td>
			<td><bean:message key="label.enrollment.personalData.professionalEnds" bundle="STUDENT_RESOURCES"/></td>	
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.SEVERAL_ENDS.toString() %>" /></td>
			<td><bean:message key="label.enrollment.personalData.nonComericalEnds" bundle="STUDENT_RESOURCES"/></td>	
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.ALL_ENDS.toString() %>" /></td>
			<td><bean:message key="label.enrollment.personalData.allEnds" bundle="STUDENT_RESOURCES"/></td>	
		</tr>
		</table>

	<br />

	<div style="padding-left: 1em;">
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.NO_END.toString() %>" />
		<b><bean:message key="label.enrollment.personalData.noAuthorization" bundle="STUDENT_RESOURCES"/></b>
	</div>

</div>
	
<p style="background-color: #ffd;">Nota: <bean:message key="label.enrollment.personalData.changes" bundle="STUDENT_RESOURCES"/></p>

<br/>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue" bundle="STUDENT_RESOURCES"/></html:submit>
</html:form>



</div>
