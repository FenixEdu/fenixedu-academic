<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" scope="session"/>
<bean:define id="removedCurriCularCourseScopes" name="<%= SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY %>" scope="session"/>
<bean:size id="removedCurriCularCourseScopesSize" name="removedCurriCularCourseScopes"/>
<bean:size id="infoOptionalCurricularCoursesEnrolmentsSize" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments"/>
<bean:size id="actualEnrolmentSize" name="infoEnrolmentContext" property="actualEnrolment"/>

<br/>
<html:form action="/curricularCourseEnrolmentManager.do">
	<html:hidden property="method" value="accept"/>
	<html:hidden property="step" value="1"/>	

	<logic:notEqual name="actualEnrolmentSize" value="0">
		<b><bean:message key="label.curricular.courses.choosen"/></b>
		<ul>
	</logic:notEqual>
	<logic:equal name="actualEnrolmentSize" value="0">
		<logic:notEqual name="infoOptionalCurricularCoursesEnrolmentsSize" value="0">
			<b><bean:message key="label.curricular.courses.choosen"/></b>
			<ul>
		</logic:notEqual>
	</logic:equal>

	<logic:notEqual name="actualEnrolmentSize" value="0">
		<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment">
			<li><bean:write name="curricularScope" property="infoCurricularCourse.name"/></li>
		</logic:iterate>
	</logic:notEqual>

	<logic:notEqual name="infoOptionalCurricularCoursesEnrolmentsSize" value="0">
		<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
			<li>
				<bean:write name="optionalEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/> - <bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
			</li>
		</logic:iterate>
	</logic:notEqual>

	<logic:notEqual name="actualEnrolmentSize" value="0">
		</ul>
	</logic:notEqual>
	<logic:equal name="actualEnrolmentSize" value="0">
		<logic:notEqual name="infoOptionalCurricularCoursesEnrolmentsSize" value="0">
			<ul>
		</logic:notEqual>
	</logic:equal>

	<logic:present name="removedCurriCularCourseScopes">
		<logic:notEqual name="removedCurriCularCourseScopesSize" value="0">
			<b><bean:message key="label.curricular.course.to.remove"/></b>
			<ul>
				<logic:iterate id="curricularScope" name="removedCurriCularCourseScopes">
					<li><bean:write name="curricularScope" property="infoCurricularCourse.name"/></li>
				</logic:iterate>
			</ul>	
		</logic:notEqual>
	</logic:present>

	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment"/>	
	</html:cancel>		
</html:form>




<%--
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>

<br/>
<b><bean:message key="label.curricular.courses.choosen"/></b>
<html:form action="/curricularCourseEnrolmentManager.do">
	<html:hidden property="method" value="accept"/>
	<html:hidden property="step" value="1"/>	
	<ul>
		<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
			<li><bean:write name="curricularScope" property="infoCurricularCourse.name"/></li>
		</logic:iterate>
		<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
			<li>
				<bean:write name="optionalEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/> - <bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
			</li>
		</logic:iterate>
	</ul>	
	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment"/>	
	</html:cancel>		
</html:form>
--%>