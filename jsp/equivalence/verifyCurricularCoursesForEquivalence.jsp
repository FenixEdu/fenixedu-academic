<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.Globals" %>

<bean:define id="infoEquivalenceContext" name="<%= SessionConstants.EQUIVALENCE_CONTEXT_KEY %>" scope="session"/>
<bean:define id="chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade" name="infoEquivalenceContext" property="chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade"/>
<bean:define id="chosenInfoCurricularCourseScopesToGetEquivalence" name="infoEquivalenceContext" property="chosenInfoCurricularCourseScopesToGetEquivalence"/>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>"/>

<html:form action="<%= path %>">
	<html:hidden property="method" value="confirm"/>

	<b><bean:message key="label.curricular.courses.that.will.give.equivalence"/></b>
	<br/>
	<table border="0" cellpadding="0" cellspacing="5">
		<tr>
			<th>&nbsp;</th>
			<th align="center"><bean:message key="label.enrolment.grade"/></th>
		</tr>
		<logic:iterate id="infoCurricularCourseScopeGrade" name="chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade">
			<tr>
				<td><bean:write name="infoCurricularCourseScopeGrade" property="infoCurricularCourseScope.infoCurricularCourse.name"/></td>
				<td align="center"><bean:write name="infoCurricularCourseScopeGrade" property="grade"/></td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<br/>
	<b><bean:message key="label.curricular.courses.that.will.get.equivalence"/></b>
	<br/>
	<table border="0" cellpadding="0" cellspacing="5">
		<tr>
			<th>&nbsp;</th>
			<th align="center"><bean:message key="label.enrolment.grade"/></th>
		</tr>
		<logic:iterate id="infoCurricularCourseScope" name="chosenInfoCurricularCourseScopesToGetEquivalence" indexId="indexId">
			<tr>
				<td valign="middle" align="left">
					<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name"/>
				</td>
				<td>
					<html:text size="2" maxlength="2" property='<%= "grades" + "["+ indexId + "]" %>'/>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">	
		<bean:message key="button.continue.enrolment" bundle="STUDENT_RESOURCES"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment" bundle="STUDENT_RESOURCES"/>	
	</html:cancel>		
</html:form>
