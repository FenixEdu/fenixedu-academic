<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.Globals" %>

<bean:define id="infoEquivalenceContext" name="<%= SessionConstants.EQUIVALENCE_CONTEXT_KEY %>" scope="session"/>
<bean:define id="infoEnrolmentsToGiveEquivalence" name="infoEquivalenceContext" property="infoEnrolmentsToGiveEquivalence"/>
<bean:define id="infoCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGetEquivalence"/>
<bean:size id="sizeEnrolmentsToGiveEquivalence" name="infoEquivalenceContext" property="infoEnrolmentsToGiveEquivalence"/>
<bean:size id="sizeCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGetEquivalence"/>

<h2 align="center"><bean:message key="tilte.manual.equivalence"/></h2>

<logic:equal name="sizeEnrolmentsToGiveEquivalence" value="0">
	<bean:define id="noCurricularCourseScopes">
		<bean:message key="message.no.curricular.courses.to.give.equivalence"/>
	</bean:define>
</logic:equal>

<logic:equal name="sizeCurricularCourseScopesToGetEquivalence" value="0">
	<bean:define id="noCurricularCourseScopes">
		<bean:message key="message.no.curricular.courses.to.get.equivalence"/>
	</bean:define>
</logic:equal>

<logic:present name="noCurricularCourseScopes">
	<bean:write name="noCurricularCourseScopes" filter="false"/>
</logic:present>

<logic:notPresent name="noCurricularCourseScopes">
	<b><bean:message key="label.second.step.equivalence"/></b>
	<br/>
	<br/>
	<b><bean:message key="message.equivalence.note"/></b>
	<br/>
	<br/>

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>"/>

	<html:form action="<%= path %>">
		<html:hidden property="method" value="verify"/>

		<table border="0" cellpadding="0" cellspacing="5" width="100%">
			<tr>
				<td width="50%"><b><bean:message key="label.curricular.courses.to.give.equivalence"/></b><br/><br/></td>
				<td width="50%"><b><bean:message key="label.curricular.courses.to.get.equivalence"/></b><br/><br/></td>
			</tr>
			<tr>
				<td valign="top" width="50%">
					<table border="1" cellpadding="0" cellspacing="0"width="100%">
						<tr>
							<th>&nbsp;</th>
							<th align="center"><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/></th>
							<th align="center"><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/></th>
							<th align="center"><bean:message key="label.curricular.course.semester"/></th>
						</tr>
						<logic:iterate id="infoEnrolment" name="infoEnrolmentsToGiveEquivalence" indexId="index">
							<tr>
								<td align="center">
									<html:multibox property="curricularCoursesToGiveEquivalence"><bean:write name="index"/></html:multibox>
								</td>
								<td align="center">
									<bean:define id="link">
										<%= path %>.do?method=details&enrolmentIndex=<bean:write name="index"/>
									</bean:define>
									<html:link page="<%= pageContext.findAttribute("link").toString() %>" transaction="true">
										<%--<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/><br/>(<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>)--%>
										<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
									</html:link>
								</td>
								<td align="center">
									<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year"/>
								</td>
								<td align="center">
									<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.semester"/>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
				<td valign="top" width="50%">
					<table border="1" cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<th>&nbsp;</th>
							<th align="center"><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/></th>
							<th align="center"><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/></th>
							<th align="center"><bean:message key="label.curricular.course.semester"/></th>
						</tr>
						<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourseScopesToGetEquivalence" indexId="index">
							<tr>
								<td align="center">
									<html:multibox property="curricularCoursesToGetEquivalence"><bean:write name="index"/></html:multibox>
								</td>
								<td align="center">
									<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/>
								</td>
								<td align="center">
									<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>
								</td>
								<td align="center">
									<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
		</table>
		<br/><br/>
		<center>
			<html:submit styleClass="inputbutton"><bean:message key="button.continue.enrolment" bundle="STUDENT_RESOURCES"/></html:submit>
			<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		</center>
	</html:form>
</logic:notPresent>
