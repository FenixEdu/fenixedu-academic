<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEquivalenceContext" name="<%= SessionConstants.EQUIVALENCE_CONTEXT_KEY %>" scope="request"/>
<bean:define id="infoCurricularCourseScopesToGiveEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGiveEquivalence"/>
<bean:define id="infoCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGetEquivalence"/>
<bean:size id="sizeCurricularCourseScopesToGiveEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGiveEquivalence"/>
<bean:size id="sizeCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGetEquivalence"/>

<h2 align="center"><bean:message key="tilte.manual.equivalence"/></h2>

<html:form action="/manualEquivalenceManager.do">
	<html:hidden property="method" value="verify"/>

	<table border="0" cellpadding="0" cellspacing="5" width="100%">
		<tr>
			<td width="50%"><b><bean:message key="label.curricular.courses.to.give.equivalence"/></b></td>
			<td width="50%"><b><bean:message key="label.curricular.courses.to.get.equivalence"/></b></td>
		</tr>
		<tr>
			<td valign="top" width="50%">
				<table border="1" cellpadding="0" cellspacing="0"width="100%">
					<tr>
						<th>&nbsp;</th>
						<th align="center"><bean:message key="label.curricular.course.name"/></th>
						<th align="center"><bean:message key="label.curricular.course.semester"/></th>
						<th align="center"><bean:message key="label.curricular.course.year"/></th>
					</tr>
					<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourseScopesToGiveEquivalence" indexId="index">
						<tr>
							<td align="center">
								<html:multibox property="curricularCoursesToGiveEquivalence"><bean:write name="index"/></html:multibox>
							</td>
							<td align="center">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/>
							</td>
							<td align="center">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>
							</td>
							<td align="center">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>
							</td>
						</tr>
					</logic:iterate>
				</table>
			</td>
			<td valign="top" width="50%">
				<table border="1" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<th>&nbsp;</th>
						<th align="center"><bean:message key="label.curricular.course.name"/></th>
						<th align="center"><bean:message key="label.curricular.course.semester"/></th>
						<th align="center"><bean:message key="label.curricular.course.year"/></th>
					</tr>
					<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourseScopesToGetEquivalence" indexId="index">
						<tr>
							<td align="center">
								<html:multibox property="curricularCoursesToGiveEquivalence"><bean:write name="index"/></html:multibox>
							</td>
							<td align="center">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/>
							</td>
							<td align="center">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>
							</td>
							<td align="center">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>
							</td>
						</tr>
					</logic:iterate>
				</table>
			</td>
		</tr>
	</table>
	<br/><br/>
	<center>
		<html:submit styleClass="inputbutton"><bean:message key="button.continue.enrolment"/></html:submit>
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
	</center>
</html:form>
