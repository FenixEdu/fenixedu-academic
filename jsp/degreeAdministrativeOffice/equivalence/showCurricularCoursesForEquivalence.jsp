<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEquivalenceContext" name="<%= SessionConstants.EQUIVALENCE_CONTEXT_KEY %>" scope="session"/>
<bean:define id="infoCurricularCourseScopesToGiveEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGiveEquivalence"/>
<bean:define id="infoCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGetEquivalence"/>
<bean:size id="sizeCurricularCourseScopesToGiveEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGiveEquivalence"/>
<bean:size id="sizeCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCourseScopesToGetEquivalence"/>

<h2 align="center"><bean:message key="tilte.manual.equivalence"/></h2>

<logic:equal name="sizeCurricularCourseScopesToGiveEquivalence" value="0">
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
	<html:form action="/manualEquivalenceManager.do">
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
									<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/><br/>(<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>)
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
									<html:multibox property="curricularCoursesToGetEquivalence"><bean:write name="index"/></html:multibox>
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
</logic:notPresent>
