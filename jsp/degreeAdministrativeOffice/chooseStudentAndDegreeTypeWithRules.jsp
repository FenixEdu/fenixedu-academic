<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="degreeTypeList" name="<%= SessionConstants.DEGREE_TYPE %>"/>

<center>
<h2><bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="title.student.enrolment.with.rules"/></h2>
<html:form action="/prepareStudentEnrolment.do?method=withRules">
	<%--<html:hidden property="page" value="1"/>--%>
	<html:hidden property="method" value="start"/>
	<table border="0">
		<tr>
			<td align="left"><bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.choose.degree.type"/>:&nbsp;</td>
			<td align="left">
				<html:select property="degreeType" size="1">
					<html:options collection="degreeTypeList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.choose.student"/>:&nbsp;</td>
			<td align="left">
				<input type="text" name="studentNumber" size="5" value=""/>
				<%--<html:text property="studentNumber" size="5" value=""/>--%>
			</td>
		</tr>
	</table>
	<p>&nbsp;</p>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>
</center>