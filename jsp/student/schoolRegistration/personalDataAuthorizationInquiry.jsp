<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice" %>

 <strong>Página 3 de 7</strong>
<html:form action="/dislocatedStudent?method=prepareDislocatedStudentInquiry">


<h2><bean:message key="label.enrollment.personalData.inquiry" bundle="STUDENT_RESOURCES"/></h2>

<span class="error"><html:errors/></span>
<br/><br/>
<b><bean:message key="label.enrollment.personalData.authorization" bundle="STUDENT_RESOURCES"/></b>
<br/><br/>
<table cellpadding=5>
<tr>
	<td>&nbsp;</td>
	<td><html:radio property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.PROFESSIONAL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.professionalEnds" bundle="STUDENT_RESOURCES"/></td>	
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:radio property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.SEVERAL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.nonComericalEnds" bundle="STUDENT_RESOURCES"/></td>	
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:radio property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.ALL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.allEnds" bundle="STUDENT_RESOURCES"/></td>	
</tr>
</table>
<br/>

<html:radio property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.NO_END.toString() %>" />&nbsp;&nbsp;
<b><bean:message key="label.enrollment.personalData.noAuthorization" bundle="STUDENT_RESOURCES"/></b>

<br/><br/>
<html:submit styleClass="inputbutton"><bean:message key="button.continue" bundle="STUDENT_RESOURCES"/></html:submit>
</html:form>

<br/><br/><br/><br/><br/><br/><br/><br/>
<bean:message key="label.enrollment.personalData.changes" bundle="STUDENT_RESOURCES"/>