<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%--
<bean:define id="infoCurricularCourseEnromentWithoutRules" name="<%= SessionConstants.ENROLMENT_WITHOUT_RULES_INFO_KEY %>" scope="session"/>
<bean:define id="infoExecutionDegreesNamesList" name="infoCurricularCourseEnromentWithoutRules" property="executionDegreesLableValueBeanList"/>
--%>
<bean:define id="infoExecutionDegreesNamesList" name="<%= SessionConstants.DEGREE_LIST %>" scope="request"/>
<bean:define id="yearsList" name="<%= SessionConstants.ENROLMENT_YEAR_LIST_KEY %>" scope="request"/>
<bean:define id="semestersList" name="<%= SessionConstants.ENROLMENT_SEMESTER_LIST_KEY %>"  scope="request"/>
<bean:define id="infoStudent" name="<%= SessionConstants.STUDENT %>"  scope="request"/>
<bean:define id="studentOID" name="infoStudent"  property="idInternal"/>

<h2 align="center"><bean:message key="title.student.enrolment.without.rules"/></h2>
<html:errors/>
<br/>
<b><bean:message key="label.second.step.enrolment"/></b>
<br/>
<br/>
<html:form action="/prepareEnrolmentContext.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="studentOID" value="<%= studentOID.toString() %>"/>
	<html:hidden name="getStudentByNumberAndDegreeTypeForm" property="degreeType"/>
	<html:hidden name="getStudentByNumberAndDegreeTypeForm" property="studentNumber"/>

	<bean:message key="label.choose.degree"/>
	<table border="0">
		<tr>
			<td align="left">
				<html:select property="infoExecutionDegree" size="1">
					<html:options collection="infoExecutionDegreesNamesList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<bean:message key="label.choose.year"/>
	<table border="0">
		<logic:iterate id="year" name="yearsList" indexId="index1">
			<tr>
				<td align="center">
					<html:multibox property="curricularYears">
						<bean:write name="index1"/>
					</html:multibox>
				</td>
				<td align="center">
					<bean:message name="year"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<br/>
	<bean:message key="label.choose.semester"/>
	<table border="0">
		<logic:iterate id="semester" name="semestersList" indexId="index2">
			<tr>
				<td align="center">
					<html:multibox property="curricularSemesters">
						<bean:write name="index2"/>
					</html:multibox>
				</td>
				<td align="center">
					<bean:message name="semester"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>
</center>