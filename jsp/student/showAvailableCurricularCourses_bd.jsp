<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<bean:define id="actualEnrolment" name="infoEnrolmentContext" property="actualEnrolment"/>

<h5>
<html:errors />
</h5>
<br />
<br />

<bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name" /> <br />

<%-- Verificar se isto funciona quando está empty --%>
<logic:present  name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
	Disciplinas a que se encontra automaticaticamente inscrito <br />
	<logic:iterate id="curricularCourseScope" name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
		<bean:write name="curricularCourseScope" property="infoCurricularCourse.name"/> <br />
	</logic:iterate>
</logic:present>
<br />

Disciplinas para inscrição <br />
<html:form action="curricularCourseEnrolmentManager">
	<html:hidden property="step" value="0"/>
	<html:hidden property="method" value="verifyEnrolment" />
	<html:hidden property="optionalCourseIndex" value="" />
	<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled" indexId="index">

		<logic:notEqual name="curricularScope" property="infoCurricularCourse.type" 
							value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
			<html:multibox property="curricularCourses" value="<%= pageContext.findAttribute("index").toString() %>"/>
		</logic:notEqual>

		<bean:write name="curricularScope" property="infoCurricularCourse.name"/>
		
		<logic:equal name="curricularScope" property="infoCurricularCourse.type" 
					value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
			<bean:define id="link">
				javascript:document.forms[0].method.value='startEnrolmentInOptional'; document.forms[0].optionalCourseIndex.value='<bean:write name="index"/>';document.forms[0].submit();
			</bean:define>
			<html:link href='<%= pageContext.findAttribute("link").toString() %>'>
				Escolher opção
			</html:link>
			<bean:define id="optionalCourse" name="curricularScope" property="infoCurricularCourse"/>
			<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
				<logic:equal name="optionalEnrolment" property="infoCurricularCourse" value="<%= pageContext.findAttribute("optionalCourse").toString() %>">
					<br/><bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
				</logic:equal> 
			</logic:iterate>
		</logic:equal>
		<br/>
	</logic:iterate>
	<html:submit value="Continuar"/>
</html:form>

Actual Enrolment <br />
<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
	<bean:write name="curricularScope" property="infoCurricularCourse.name"/><br/>
</logic:iterate>
