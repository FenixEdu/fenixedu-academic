<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>

<logic:present name="infoEquivalenceContext" scope="request">
	<bean:define id="infoEquivalenceContext" name="infoEquivalenceContext" scope="request"/>
	<bean:define id="infoEnrolmentsToGiveEquivalence" name="infoEquivalenceContext" property="infoEnrolmentsToGiveEquivalence"/>
	<bean:define id="infoCurricularCoursesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCoursesToGetEquivalence"/>
	<bean:define id="infoStudentCurricularPlan" name="infoEquivalenceContext" property="infoStudentCurricularPlan"/>

	<bean:size id="sizeEnrolmentsToGiveEquivalence" name="infoEquivalenceContext" property="infoEnrolmentsToGiveEquivalence"/>
	<bean:size id="sizeCurricularCoursesToGetEquivalence" name="infoEquivalenceContext" property="infoCurricularCoursesToGetEquivalence"/>
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
		<bean:message key="message.enrollment.equivalence.no.curricular.courses.to.give.equivalence"/>
	</bean:define>
</logic:equal>

<logic:equal name="sizeCurricularCoursesToGetEquivalence" value="0">
	<bean:define id="noCurricularCourses">
		<bean:message key="message.enrollment.equivalence.no.curricular.courses.to.get.equivalence"/>
	</bean:define>
</logic:equal>

<logic:present name="noCurricularCourses">
	<bean:write name="noCurricularCourses" filter="false"/>
</logic:present>

<logic:notPresent name="noCurricularCourses">
<logic:present name="infoEnrolmentsToGiveEquivalence">
<logic:present name="infoCurricularCoursesToGetEquivalence">

	<table>
		<tr>
			<td><img src="<%= request.getContextPath() %>/images/dotist_info.gif" alt=""/></td>
			<td class="infoop">
				<strong><bean:message key="message.enrollment.equivalence.note"/></strong>
			</td>
		</tr>
	</table>

	<br/>

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>"/>

	<html:form action="<%= path %>">

		<html:hidden property="method" value="grades"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="studentNumber" value="<%= pageContext.findAttribute("studentNumber").toString() %>"/>
		<html:hidden property="degreeType" value="<%= pageContext.findAttribute("degreeType").toString() %>"/>
		<html:hidden property="backLink" value="<%= pageContext.findAttribute("backLink").toString() %>"/>
		<html:hidden property="fromStudentCurricularPlanID" value="<%= pageContext.findAttribute("fromStudentCurricularPlanID").toString() %>"/>
		<html:hidden property="toStudentCurricularPlanID" value="<%= pageContext.findAttribute("toStudentCurricularPlanID").toString() %>"/>

		<table border="0" cellpadding="0" cellspacing="5" width="100%">
			<tr>
				<td width="50%"><b><bean:message key="message.enrollment.equivalence.curricular.courses.to.give.equivalence"/></b><br/><br/></td>
				<td width="50%"><b><bean:message key="message.enrollment.equivalence.curricular.courses.to.get.equivalence"/></b><br/><br/></td>
			</tr>
			<tr>
				<td valign="top" width="50%">
					<table border="0" cellpadding="0" cellspacing="5"width="100%">
						<tr>
							<th>&nbsp;</th>
							<th align="left"><bean:message key="message.enrollment.equivalence.curricular.course.name"/></th>
						</tr>
						<logic:iterate id="infoEnrolment" name="infoEnrolmentsToGiveEquivalence">
							<tr>
								<td align="left">
									<html:multibox property="curricularCoursesToGiveEquivalence"><bean:write name="infoEnrolment" property="idInternal"/></html:multibox>
								</td>
								<td align="left">
									<bean:define id="link">
										<%= path %>.do?method=details&enrollmentID=<bean:write name="infoEnrolment" property="idInternal"/>&studentNumber=<%= pageContext.findAttribute("studentNumber").toString() %>&degreeType=<%= pageContext.findAttribute("degreeType").toString() %>&backLink=<%= pageContext.findAttribute("backLink").toString() %>&fromStudentCurricularPlanID=<%= pageContext.findAttribute("fromStudentCurricularPlanID").toString() %>&toStudentCurricularPlanID=<%= pageContext.findAttribute("toStudentCurricularPlanID").toString() %>
									</bean:define>
									<html:link page="<%= pageContext.findAttribute("link").toString() %>" transaction="true">
										<bean:write name="infoEnrolment" property="infoCurricularCourse.name"/>
									</html:link>
								</td>
								
							</tr>
						</logic:iterate>
					</table>
				</td>
				<td valign="top" width="50%">
					<table border="0" cellpadding="0" cellspacing="5" width="100%">
						<tr>
							<th>&nbsp;</th>
							<th align="left"><bean:message key="message.enrollment.equivalence.curricular.course.name"/></th>
						</tr>
						<logic:iterate id="infoCurricularCourse" name="infoCurricularCoursesToGetEquivalence">
							<tr>
								<td align="left">
									<html:multibox property="curricularCoursesToGetEquivalence"><bean:write name="infoCurricularCourse" property="idInternal"/></html:multibox>
								</td>
								<td align="left">
									<bean:write name="infoCurricularCourse" property="name"/>
								</td>
								
							</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
		</table>

		<br/>

		<html:submit styleClass="inputbutton"><bean:message key="button.enrollment.equivalence.continue"/></html:submit>

	</html:form>

</logic:present>
</logic:present>
</logic:notPresent>
