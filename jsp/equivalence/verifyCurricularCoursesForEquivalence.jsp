<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.action.Action" %>

<bean:define id="infoEquivalenceContext" name="<%= SessionConstants.EQUIVALENCE_CONTEXT_KEY %>" scope="session"/>
<bean:define id="chosenInfoEnrolmentsToGiveEquivalence" name="infoEquivalenceContext" property="chosenInfoEnrolmentsToGiveEquivalence"/>
<bean:define id="chosenInfoCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="chosenInfoCurricularCourseScopesToGetEquivalence"/>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>"/>

<html:form action="<%= path %>">
	<html:hidden property="method" value="accept"/>

	<b><bean:message key="label.curricular.courses.that.will.give.equivalence"/></b>
	<ul>
		<logic:iterate id="infoEnrolment" name="chosenInfoEnrolmentsToGiveEquivalence">
			<li>
				<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
			</li>
		</logic:iterate>
	</ul>
	<b><bean:message key="label.curricular.courses.that.will.get.equivalence"/></b>
	<ul>
		<logic:iterate id="infoCurricularCourseScope" name="chosenInfoCurricularCourseScopesToGetEquivalence" indexId="indexId">
			<li>
				<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/>&nbsp;&nbsp;&nbsp;
				<%--<input type="text" name="<bean:write name='infoEnrolment' property='idInternal' />_text" size="5" value=""/>--%>
				<html:text size="5" property='<%= "grades" + "["+ indexId + "]" %>'/>
			</li>
		</logic:iterate>
	</ul>	

	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment" bundle="STUDENT_RESOURCES"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment" bundle="STUDENT_RESOURCES"/>	
	</html:cancel>		
</html:form>
