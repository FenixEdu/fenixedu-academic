<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="Util.EnrolmentState, Util.CurricularCourseType, DataBeans.InfoEnrolmentInOptionalCurricularCourse" %>

<h2><bean:message key="title.student.LEEC.optional.enrollment"/></h2>

<span class="error"><html:errors/></span>

<br/>

<table>	
	<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
		<tr>
			<td>
				<bean:message key="label.student.enrollment.specializationArea"/>:&nbsp;
			</td>
			<td>
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.name"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea"/>:&nbsp;
			</td>
			<td>
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.name"/>
			</td>
		</tr>
	</logic:present>
	<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
		<tr>
			<td>
				<bean:message key="label.student.enrollment.specializationArea"/>:&nbsp;
			</td>
			<td colspan='4' style="text-align:left">
				<bean:message key="label.student.enrollment.no.area"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea"/>:&nbsp;
			</td>
			<td colspan='4' style="text-align:left">
				<bean:message key="label.student.enrollment.no.area"/>
			</td>
		</tr>
	</logic:notPresent>

	<bean:define id="curricularCoursesToEnroll" name="infoStudentEnrolmentContext" property="studentInfoEnrollmentsWithStateEnrolled"/>	
	<bean:size id="curricularCoursesToEnrollSize" name="curricularCoursesToEnroll"/>

	<logic:greaterThan name="curricularCoursesToEnrollSize" value="0">
		<tr>
			<td colspan="5">
				<br/>
				<b><bean:message key="message.student.enrolled.curricularCourses"/>:</b>
			</td>
		</tr>
	
		<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentInfoEnrollmentsWithStateEnrolled" type="DataBeans.InfoEnrolmentInOptionalCurricularCourse">
			<tr>
				<td colspan='5'>
					<bean:write name="enrollmentElem" property="infoCurricularCourse.name"/>&nbsp;-&nbsp;<bean:write name="enrollmentElem" property="infoCurricularCourseForOption.name"/>
				</td>
			</tr>
		</logic:iterate>
	</logic:greaterThan>
</table>
<br/>
<br/>
<logic:present name="curriculum">
	<table>
		<tr>
			<td colspan="5">
				<br/>
				<b><bean:message key="message.student.curriculum"/>:</b>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="label.executionYear"/></td>
			<td class="listClasses-header"><bean:message key="label.semester"/></td>
			<td class="listClasses-header"><bean:message key="label.degree.name"/></td>
			<td class="listClasses-header"><bean:message key="label.curricular.course.name"/></td>
			<td class="listClasses-header"><bean:message key="label.finalEvaluation"/></td>
		</tr>
		<logic:iterate id="curriculumElem" name="curriculum">
			<tr>
				<td class="listClasses">
					<bean:write name="curriculumElem" property="infoExecutionPeriod.infoExecutionYear.year"/>
				</td>
				<td class="listClasses">
					<bean:write name="curriculumElem" property="infoExecutionPeriod.semester"/>
				</td>
				<td class="listClasses">
					<bean:write name="curriculumElem" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<bean:write name="curriculumElem" property="infoCurricularCourse.name"/>
					<logic:equal name="curriculumElem" property="infoCurricularCourse.type" value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
						<% if (pageContext.findAttribute("curriculumElem") instanceof InfoEnrolmentInOptionalCurricularCourse)
						   {%>
							<logic:notEmpty name="curriculumElem" property="infoCurricularCourseForOption">
								-&nbsp;<bean:write name="curriculumElem" property="infoCurricularCourseForOption.name"/>
							</logic:notEmpty>
							<logic:empty name="curriculumElem" property="infoCurricularCourseForOption">
								-&nbsp;<bean:message key="message.not.regular.optional.enrollment"/>
							</logic:empty>
						   <%}
						%>
					</logic:equal>
				</td>
			  <td class="listClasses">
				<logic:notEqual name="curriculumElem" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
					<bean:message name="curriculumElem" property="enrolmentState.name" bundle="ENUMERATION_RESOURCES"/>
				</logic:notEqual>
				
				<logic:equal name="curriculumElem" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
					<bean:write name="curriculumElem" property="infoEnrolmentEvaluation.grade"/>
				</logic:equal>
			  </td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
