<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="enrolmentsList" name="<%= SessionConstants.ENROLMENT_LIST %>" scope="request"/>
<bean:size id="sizeenrolmentsList" name="enrolmentsList"/>

<center>
	<h2><bean:message key="title.coordinator.equivalence"/></h2>

<logic:equal name="sizeenrolmentsList" value="0">
	</center>
	<b><bean:message key="label.no.curricular.courses.to.give.equivalence"/></b>
</logic:equal>

<logic:notEqual name="sizeenrolmentsList" value="0">
	<b><bean:message key="label.first.step.coordinator.equivalence"/></b>
	<br/>
	<br/>
	</center>
	<table border="1" width="100%">
		<tr>
			<th align="center"><bean:message key="label.curricular.course.name"/></th>
			<th align="center"><bean:message key="label.degree.name"/></th>
			<th align="center"><bean:message key="label.student.number"/></th>
			<th align="center">&nbsp;</th>
		</tr>
		<logic:iterate id="infoEnrolment" name="enrolmentsList" indexId="index">
			<bean:define id="link">
				/equivalence.do?method=show&studentOID=<bean:write name="infoEnrolment" property="infoStudentCurricularPlan.infoStudent.idInternal"/>
			</bean:define>
			<tr>
				<td align="center">
						<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
				</td>
				<td align="center">
						<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>
				</td>
				<td align="center">
						<bean:write name="infoEnrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
				</td>
				<td align="center">
					<html:link page="<%= pageContext.findAttribute("link").toString() %>">
						<bean:message key="label.chosse.situation"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEqual>
