<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<bean:define id="actualEnrolment" name="infoEnrolmentContext" property="actualEnrolment"/>

<bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name" /> <br />

<html:errors />
<%-- Verificar se isto funciona quando está empty --%>
<logic:present  name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
	<bean:message key="label.mandatory.enrolment.curricular.courses"/> <br />
	<logic:iterate id="curricularCourseScope" name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
		<bean:write name="curricularCourseScope" property="infoCurricularCourse.name"/> <br />
	</logic:iterate>
</logic:present>
<br />

<bean:message key="label.enrolment.curricular.courses"/> <br />
<html:form action="curricularCourseEnrolmentManager">
	<html:hidden property="step" value="0"/>
	<html:hidden property="method" value="verifyEnrolment" />
	<html:hidden property="optionalCourseIndex" value="" />
	<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled" indexId="index">

		<logic:notEqual name="curricularScope" property="infoCurricularCourse.type" 
							value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
			<html:multibox property="curricularCourses" value="<%= pageContext.findAttribute("index").toString() %>"/>
			<bean:write name="curricularScope" property="infoCurricularCourse.name"/>			
		</logic:notEqual>
		<logic:equal name="curricularScope" property="infoCurricularCourse.type" 
					value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
			<bean:define id="onclick">
				if (this.checked == true) {this.form.method.value='startEnrolmentInOptional'; document.forms[0].optionalCourseIndex.value='<bean:write name="index"/>';this.form.submit();}
			</bean:define>
			<html:multibox property="curricularCourses" value="<%= pageContext.findAttribute("index").toString() %>" onclick="<%= pageContext.findAttribute("onclick").toString() %>"/>
			
			<bean:write name="curricularScope" property="infoCurricularCourse.name"/> 
<%--			<html:link href='<%= pageContext.findAttribute("link").toString() %>'>
				<bean:message key="link.choose.optional.curricular.course"/>
			</html:link> --%>
			<bean:define id="optionalCourse" name="curricularScope" property="infoCurricularCourse"/>
			<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
				<logic:equal name="optionalEnrolment" property="infoCurricularCourse" value="<%= pageContext.findAttribute("optionalCourse").toString() %>">
					- <bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
				</logic:equal> 
			</logic:iterate>
		</logic:equal>
		<br/>
	</logic:iterate>
	<html:submit value="Continuar"/>
</html:form>
<%--
Actual Enrolment <br />
<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
	<bean:write name="curricularScope" property="infoCurricularCourse.name"/><br/>
</logic:iterate> --%>
