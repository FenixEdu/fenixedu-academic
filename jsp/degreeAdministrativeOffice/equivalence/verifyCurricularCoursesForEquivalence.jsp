<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEquivalenceContext" name="<%= SessionConstants.EQUIVALENCE_CONTEXT_KEY %>" scope="session"/>
<bean:define id="chosenInfoCurricularCourseScopesToGiveEquivalence" name="infoEquivalenceContext" property="chosenInfoCurricularCourseScopesToGiveEquivalence"/>
<bean:define id="chosenInfoCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="chosenInfoCurricularCourseScopesToGetEquivalence"/>

<html:form action="/manualEquivalenceManager.do">
	<html:hidden property="method" value="accept"/>

	<b><bean:message key="label.curricular.courses.that.will.give.equivalence"/></b>
	<ul>
		<logic:iterate id="infoCurricularCourseScope" name="chosenInfoCurricularCourseScopesToGiveEquivalence">
			<li><bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/></li>
		</logic:iterate>
	</ul>
	<b><bean:message key="label.curricular.courses.that.will.get.equivalence"/></b>
	<ul>
		<logic:iterate id="infoCurricularCourseScope" name="chosenInfoCurricularCourseScopesToGetEquivalence">
			<li><bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/></li>
		</logic:iterate>
	</ul>	

	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment"/>	
	</html:cancel>		
</html:form>
