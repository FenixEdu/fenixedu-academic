<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="backLink" name="backLink" scope="request"/>
<bean:define id="degreeType" name="degreeType" scope="request"/>

<h2><bean:message key="tilte.enrollment.equivalence"/></h2>

<span class="error"><html:errors/></span>

<html:form action="/prepareStudentForEnrollmentEquivalence.do" focus="studentNumber">

	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeType" value="<%= pageContext.findAttribute("degreeType").toString() %>"/>
	<html:hidden property="backLink" value="<%= pageContext.findAttribute("backLink").toString() %>"/>

	<table border="0">
		<tr>
			<td align="left"><bean:message key="message.enrollment.equivalence.student.number"/>&nbsp;</td>
			<td align="left">
				<input type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>
	</table>

	<br/>

	<html:submit styleClass="inputbutton">
		<bean:message key="button.enrollment.equivalence.continue"/>
	</html:submit>

</html:form>
