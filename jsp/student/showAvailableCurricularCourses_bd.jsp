<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<bean:define id="actualEnrolment" name="infoEnrolmentContext" property="actualEnrolment"/>
<bean:size id="sizeAutomaticalyEnroled" name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled" />
<bean:size id="sizeToBeEnroled" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled"/>

<logic:notEqual name="sizeAutomaticalyEnroled" value="0">
	<bean:message key="label.mandatory.enrolment.curricular.courses"/> <br />
	<logic:iterate id="curricularCourseScope" name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
		<bean:write name="curricularCourseScope" property="infoCurricularCourse.name"/> <br />
	</logic:iterate>
</logic:notEqual>
<logic:notEqual name="sizeToBeEnroled" value="0">
	<html:form action="curricularCourseEnrolmentManager">
		<html:hidden property="step" value="0"/>
		<html:hidden property="method" value="verifyEnrolment" />
		<html:hidden property="optionalCourseIndex" value="" />
		<bean:message key="label.enrolment.curricular.courses"/>
		<table>
			<tr>
				<th>&nbsp;</th>	
				<th>Nome da disciplina</th>
				<th>Ano</th>
			</tr>
			<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled" indexId="index">
				<logic:equal name="curricularScope" property="infoCurricularCourse.type" 
								value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
					<bean:define id="onclick">
						if (this.checked == true) {this.form.method.value='startEnrolmentInOptional'; document.forms[0].optionalCourseIndex.value='<bean:write name="index"/>';this.form.submit();}	
					</bean:define>
					
					<bean:define id="optionalCourse" name="curricularScope" property="infoCurricularCourse"/>
					<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
						<logic:equal name="optionalEnrolment" property="infoCurricularCourse" value="<%= pageContext.findAttribute("optionalCourse").toString() %>">
							<bean:define id="optionalEnrolment">
								<br/> &nbsp;&nbsp;<bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
							</bean:define>
						</logic:equal> 
					</logic:iterate>
					
				</logic:equal>
				<logic:notEqual name="curricularScope" property="infoCurricularCourse.type" 
								value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
					<bean:define id="onclick" value=" "></bean:define>
					<bean:define id="optionalEnrolment" value="" />
				</logic:notEqual>
				<tr>
					<td>
						<html:multibox property="curricularCourses" value="<%= pageContext.findAttribute("index").toString() %>" onclick="<%= pageContext.findAttribute("onclick").toString() %>"/>
					</td>
					<td>
						<bean:write name="curricularScope" property="infoCurricularCourse.name"/>
						<logic:present name="optionalEnrolment">
							<bean:write name="optionalEnrolment" filter="false"/>
						</logic:present>
					</td>
					<td align="right">
						<bean:write name="curricularScope" property="infoCurricularSemester.infoCurricularYear.year"/>
					</td>
				</tr>
		 		<br/>
			</logic:iterate>
			<tr>
				<td colspan="3" align="center">
						<html:submit styleClass="inputbutton">
							<bean:message key="button.continue.enrolment"/>
						</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</logic:notEqual>