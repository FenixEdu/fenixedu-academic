<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>

<logic:present name="infoEquivalenceContext" scope="request">
	<bean:define id="infoEquivalenceContext" name="infoEquivalenceContext" scope="request"/>
	<bean:define id="infoEnrollmentsFromEquivalences" name="infoEquivalenceContext" property="infoEnrollmentsFromEquivalences"/>
	<bean:define id="infoStudentCurricularPlan" name="infoEquivalenceContext" property="infoStudentCurricularPlan"/>

	<bean:size id="sizeEnrollmentsFromEquivalence" name="infoEquivalenceContext" property="infoEnrollmentsFromEquivalences"/>
</logic:present>

<bean:define id="studentNumber" name="studentNumber" scope="request"/>
<bean:define id="degreeType" name="degreeType" scope="request"/>
<bean:define id="backLink" name="backLink" scope="request"/>
<bean:define id="fromStudentCurricularPlanID" name="fromStudentCurricularPlanID" scope="request"/>

<h2><bean:message key="tilte.enrollment.equivalence"/> - <bean:message key="tilte.enrollment.equivalence.delete.enrollment.equivalence"/></h2>

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
</logic:present>

<logic:equal name="sizeEnrollmentsFromEquivalence" value="0">
	<bean:define id="noCurricularCourses">
		<bean:message key="message.enrollment.equivalence.no.enrollments.from.equivalence"/>
	</bean:define>
</logic:equal>

<logic:present name="noCurricularCourses">
	<bean:write name="noCurricularCourses" filter="false"/>
</logic:present>

<logic:notPresent name="noCurricularCourses">
<logic:present name="infoEnrollmentsFromEquivalences">

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>"/>

	<html:form action="<%= path %>">

		<html:hidden property="method" value="confirm"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="studentNumber" value="<%= pageContext.findAttribute("studentNumber").toString() %>"/>
		<html:hidden property="degreeType" value="<%= pageContext.findAttribute("degreeType").toString() %>"/>
		<html:hidden property="backLink" value="<%= pageContext.findAttribute("backLink").toString() %>"/>
		<html:hidden property="fromStudentCurricularPlanID" value="<%= pageContext.findAttribute("fromStudentCurricularPlanID").toString() %>"/>

		<table border="0" cellpadding="0" cellspacing="5" width="100%">
			<tr>
				<th colspan="2">
					<bean:message key="message.enrollment.equivalence.enrollments.from.equivalence"/>
					<br/><br/>
				</th>
			</tr>
			<logic:iterate id="infoEnrolment" name="infoEnrollmentsFromEquivalences">
				<tr>
					<td align="center">
						<html:multibox property="curricularCoursesToGiveEquivalence"><bean:write name="infoEnrolment" property="idInternal"/></html:multibox>
					</td>
					<td align="left">
						<bean:define id="link">
							<%= path %>.do?method=details&enrollmentID=<bean:write name="infoEnrolment" property="idInternal"/>&studentNumber=<%= pageContext.findAttribute("studentNumber").toString() %>&degreeType=<%= pageContext.findAttribute("degreeType").toString() %>&backLink=<%= pageContext.findAttribute("backLink").toString() %>&fromStudentCurricularPlanID=<%= pageContext.findAttribute("fromStudentCurricularPlanID").toString() %>
						</bean:define>
						<html:link page="<%= pageContext.findAttribute("link").toString() %>" transaction="true">
							<bean:write name="infoEnrolment" property="infoCurricularCourse.name"/>
						</html:link>
					</td>
					
				</tr>
			</logic:iterate>
		</table>

		<br/>

		<html:submit styleClass="inputbutton"><bean:message key="button.enrollment.equivalence.continue"/></html:submit>

	</html:form>

</logic:present>
</logic:notPresent>
