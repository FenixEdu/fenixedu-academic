<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="degreeTypeList" name="<%= SessionConstants.DEGREE_TYPE %>"/>

<h2 align="center"><bean:message key="link.student.enrolment.in.optional.curricular.course.without.rules"/></h2>
<html:errors/>
<br/>
<b><bean:message key="label.first.step.enrolment"/></b>
<center>
<br/>
<br/>
<html:form action="/prepareStudentDataForEnrolmentInOptionalWithoutRules.do">
	<html:hidden property="method" value="getStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules"/>
	<html:hidden property="page" value="1"/>
	<table border="0">
		<tr>
			<td align="left"><bean:message key="label.choose.degree.type"/>&nbsp;</td>
			<td align="left">
				<html:select property="degreeType" size="1">
					<html:options collection="degreeTypeList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.choose.student"/>&nbsp;</td>
			<td align="left">
				<input type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>
</center>