<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.student.enrollment" /></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>
<html:form action="/curricularCoursesEnrollment" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="start"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/> 
	<logic:present name="executionDegreeId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	<logic:present name="curricularCoursesEnrollmentForm" property="registrationId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationId" property="registrationId" />
	</logic:present>
	<table class="infoop">
		<tr>
			<td><bean:message key="label.choose.student" />&nbsp;</td>
			<td><input alt="input.studentNumber" type="text" name="studentNumber" size="5" maxlength="5"/></td>
		</tr>
	</table>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit.student"/></html:submit>
</html:form>
