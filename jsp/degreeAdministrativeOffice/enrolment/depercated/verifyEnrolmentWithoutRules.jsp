<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" scope="session"/>
<bean:define id="removedCurriCularCourses" name="<%= SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY %>" scope="session"/>
<bean:size id="removedCurriCularCourseSize" name="removedCurriCularCourses"/>
<bean:size id="actualEnrolmentSize" name="infoEnrolmentContext" property="actualEnrolment"/>

<br/>
<html:form action="/curricularCourseEnrolmentWithoutRulesManager.do">
	<html:hidden property="method" value="accept"/>

	<logic:notEqual name="actualEnrolmentSize" value="0">
		<b><bean:message key="label.curricular.courses.choosen" bundle="STUDENT_RESOURCES"/></b>
		<ul>
			<logic:iterate id="curricularCourse" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
				<li><bean:write name="curricularCourse" property="name"/></li>
			</logic:iterate>
		</ul>	
	</logic:notEqual>

	<logic:present name="removedCurriCularCourses">
		<logic:notEqual name="removedCurriCularCourseSize" value="0">
			<b><bean:message key="label.curricular.course.to.remove"/></b>
			<ul>
				<logic:iterate id="curricularCourse" name="removedCurriCularCourses">
					<li><bean:write name="curricularCourse" property="name"/></li>
				</logic:iterate>
			</ul>	
		</logic:notEqual>
	</logic:present>
	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment" bundle="STUDENT_RESOURCES"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment" bundle="STUDENT_RESOURCES"/>	
	</html:cancel>		
</html:form>





















<%--
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" scope="session"/>
<bean:size id="sizeToEnroll" name="infoEnrolmentContext" property="actualEnrolment"/>
<logic:present name="<%= SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY %>" scope="session">
	<bean:define id="erolmentsToRemoveList" name="<%= SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY %>" scope="session"/>
	<bean:size id="sizeToRemove" name="erolmentsToRemoveList"/>
</logic:present>

<br/>
<html:form action="/curricularCourseEnrolmentWithoutRulesManager.do">
	<html:hidden property="method" value="accept"/>
	<logic:notEqual name="sizeToEnroll" value="0">
		<b><bean:message key="label.curricular.courses.choosen" bundle="STUDENT_RESOURCES"/></b>
		<ul>
			<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
				<li><bean:write name="curricularScope" property="infoCurricularCourse.name"/></li>
			</logic:iterate>
		</ul>
	</logic:notEqual>
	<br/>
	<logic:present name="<%= SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY %>" scope="session">
		<logic:notEqual name="sizeToRemove" value="0">
			<b><bean:message key="label.curricular.course.to.remove"/></b>
			<ul>
				<logic:iterate id="infoEnrolment" name="erolmentsToRemoveList">
					<li><bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/></li>
				</logic:iterate>
			</ul>
		</logic:notEqual>
	</logic:present>
	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment" bundle="STUDENT_RESOURCES"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment" bundle="STUDENT_RESOURCES"/>	
	</html:cancel>		
</html:form>
--%>