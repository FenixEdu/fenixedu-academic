<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoExecutionPeriodList" name="<%= SessionConstants.EXECUTION_PERIOD %>" scope="request"/>

<h2><bean:message key="link.masterDegree.enrollment"/></h2>
<span class="error"><html:errors/></span>
<%--
<br/>
<br/>
<b><bean:message key="label.first.step.enrolment"/></b>
--%>
<br/>
<br/>
<html:form action="/prepareStudentForEnrollment.do">
	<html:hidden property="method" value="getStudentAndDegreeTypeForEnrolmentWithoutRules"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeType" value="2"/>
	<table border="0">
		<tr>
			<td align="left"><bean:message key="label.choose.student"/>&nbsp;</td>
			<td align="left">
				<input type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.choose.execution.period"/>&nbsp;</td>
			<td align="left">
				<html:select property="executionPeriodOID" size="1">
					<html:options collection="infoExecutionPeriodList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>
