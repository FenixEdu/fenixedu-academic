<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>

<logic:present name="infoEquivalenceContext" scope="request">
	<bean:define id="infoEquivalenceContext" name="infoEquivalenceContext" scope="request"/>
	<bean:define id="chosenInfoEnrollmentGradesToGiveEquivalence" name="infoEquivalenceContext" property="chosenInfoEnrollmentGradesToGiveEquivalence"/>
	<bean:define id="chosenInfoCurricularCourseGradesToGetEquivalence" name="infoEquivalenceContext" property="chosenInfoCurricularCourseGradesToGetEquivalence"/>
	<bean:define id="infoStudentCurricularPlan" name="infoEquivalenceContext" property="infoStudentCurricularPlan"/>

	<bean:size id="sizeEnrolmentsToGiveEquivalence" name="infoEquivalenceContext" property="chosenInfoEnrollmentGradesToGiveEquivalence"/>
	<bean:size id="sizeCurricularCoursesToGetEquivalence" name="infoEquivalenceContext" property="chosenInfoCurricularCourseGradesToGetEquivalence"/>
</logic:present>

<bean:define id="studentNumber" name="studentNumber" scope="request"/>
<bean:define id="degreeType" name="degreeType" scope="request"/>
<bean:define id="backLink" name="backLink" scope="request"/>
<bean:define id="fromStudentCurricularPlanID" name="fromStudentCurricularPlanID" scope="request"/>
<bean:define id="toStudentCurricularPlanID" name="toStudentCurricularPlanID" scope="request"/>

<h2><bean:message key="tilte.enrollment.equivalence"/> - <bean:message key="tilte.enrollment.equivalence.make.enrollment.equivalence"/></h2>

<span class="error"><html:errors/></span>

<logic:present name="infoStudentCurricularPlan">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#FFFFFF" class="infoselected">
				<center><b><bean:message key="message.enrollment.equivalence.info.about.chosen.student"/></b></center><br/>
				<b><bean:message key="message.enrollment.equivalence.student.number"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/><br/>
				<b><bean:message key="message.enrollment.equivalence.student.name"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/><br/>
				<b><bean:message key="message.enrollment.equivalence.info.about.current.student.plan"/></b>&nbsp;
				(<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>)&nbsp;
				<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;-&nbsp;
				<bean:write name="infoStudentCurricularPlan" property="startDate"/><br/>
			</td>
		</tr>
	</table>

	<br/>
</logic:present>

<logic:equal name="sizeEnrolmentsToGiveEquivalence" value="0">
	<bean:define id="noCurricularCourses">
		<bean:message key="errors.enrollment.equivalence.nothing.was.selected.previously"/>
	</bean:define>
</logic:equal>

<logic:equal name="sizeCurricularCoursesToGetEquivalence" value="0">
	<bean:define id="noCurricularCourses">
		<bean:message key="errors.enrollment.equivalence.nothing.was.selected.previously"/>
	</bean:define>
</logic:equal>

<logic:present name="noCurricularCourses">
	<bean:write name="noCurricularCourses" filter="false"/>
</logic:present>

<logic:notPresent name="noCurricularCourses">
<logic:present name="chosenInfoEnrollmentGradesToGiveEquivalence">
<logic:present name="chosenInfoCurricularCourseGradesToGetEquivalence">

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>"/>
	
	<html:form action="<%= path %>">

		<html:hidden property="method" value="confirm"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="studentNumber" value="<%= pageContext.findAttribute("studentNumber").toString() %>"/>
		<html:hidden property="degreeType" value="<%= pageContext.findAttribute("degreeType").toString() %>"/>
		<html:hidden property="backLink" value="<%= pageContext.findAttribute("backLink").toString() %>"/>
		<html:hidden property="fromStudentCurricularPlanID" value="<%= pageContext.findAttribute("fromStudentCurricularPlanID").toString() %>"/>
		<html:hidden property="toStudentCurricularPlanID" value="<%= pageContext.findAttribute("toStudentCurricularPlanID").toString() %>"/>

		<table cellspacing="5" border="0">
			<tr>
				<th align="left"><bean:message key="message.enrollment.equivalence.curricular.courses.that.give.equivalence"/></th>
				<th align="center"><bean:message key="message.enrollment.equivalence.curricular.course.grade"/></th>
			</tr>
			<logic:iterate id="infoEnrollmentGrade" name="chosenInfoEnrollmentGradesToGiveEquivalence">
				<bean:define id="enrollmentID" name="infoEnrollmentGrade" property="infoEnrollment.idInternal"/>
				<html:hidden property="curricularCoursesToGiveEquivalence" value="<%= pageContext.findAttribute("enrollmentID").toString() %>"/>
				<tr>
					<td align="left"><bean:write name="infoEnrollmentGrade" property="infoEnrollment.infoCurricularCourse.name"/></td>
					<td align="center"><bean:write name="infoEnrollmentGrade" property="grade"/></td>
				</tr>
			</logic:iterate>
			<tr>
				<th align="left"><br/><bean:message key="message.enrollment.equivalence.curricular.courses.that.get.equivalence"/></th>
				<th align="center"><br/><bean:message key="message.enrollment.equivalence.curricular.course.grade"/></th>
			</tr>
			<html:hidden property="size" value="<%= pageContext.findAttribute("sizeCurricularCoursesToGetEquivalence").toString() %>"/>							
			
			<logic:iterate id="infoCurricularCourseGrade" name="chosenInfoCurricularCourseGradesToGetEquivalence" indexId="courseID">
				<bean:define id="curricularCourseID" name="infoCurricularCourseGrade" property="infoCurricularCourse.idInternal"/>
				<tr>
					<td align="left">
						<bean:write name="infoCurricularCourseGrade" property="infoCurricularCourse.name"/>
					</td>
					<td align="center">
						<html:text size="2" maxlength="2" name="infoCurricularCourseGrade" property="grade" indexed="true"/>
	 					<html:hidden name="infoCurricularCourseGrade" property="curricularCourseID" value="<%= curricularCourseID.toString() %>" indexed="true"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<br/>

		<html:submit styleClass="inputbutton"><bean:message key="button.enrollment.equivalence.continue"/></html:submit>

	</html:form>

</logic:present>
</logic:present>
</logic:notPresent>
