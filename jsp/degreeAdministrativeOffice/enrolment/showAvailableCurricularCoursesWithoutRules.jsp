<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>
<bean:size id="sizeToBeEnroled" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled"/>

<logic:notEqual name="sizeToBeEnroled" value="0">
	<html:form action="curricularCourseEnrolmentWithoutRulesManager">
		<html:hidden property="step" value="0"/>
		<html:hidden property="method" value="verifyEnrolment"/>
		<table>
			<tr>
				<td colspan="3"><b><bean:message key="label.enrolment.curricular.courses"/>:</b></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><u>Nome da disciplina</u></td>
				<td align="right"><u>Ano</u></td>
			</tr>
			<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled" indexId="index">
				<tr>
					<td>
						<html:multibox property="curricularCourses">
							<bean:write name="index"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="curricularScope" property="infoCurricularCourse.name"/>
					</td>
					<td align="right">
						<bean:write name="curricularScope" property="infoCurricularSemester.infoCurricularYear.year"/>
					</td>
				</tr>
		 		<br/>
			</logic:iterate>
			<tr>
				<td colspan="3" align="center">
					<p>&nbsp;</p>
					<html:submit styleClass="inputbutton">
						<bean:message key="button.continue.enrolment"/>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</logic:notEqual>