<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="Util.EnrolmentState" %>
<h2><bean:message key="title.student.LEEC.enrollment"/></h2>
<span class="error"><html:errors/></span>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<b><bean:message key="label.student.enrollment.number"/></b>
			<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number" />&nbsp;-&nbsp;
			<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
			<br />
			<b><bean:message key="label.student.enrollment.executionPeriod"/></b>
			<bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name" />&nbsp;				
			<bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" />
		</td>
	</tr>
	<tr>
		<td style="text-align:center">
			<br /><br /><b><bean:message key="message.student.enrollment.confirmation" /></b><br /><br />
		</td>
	</tr>
</table>
<br />
<table>	
	<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
		<tr>
			<td>
				<bean:message key="label.student.enrollment.specializationArea" />:&nbsp;
			</td>
			<td>
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.name" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>
				<bean:message key="label.branch.credits" />:&nbsp;
			</td>
			<td>
				<logic:notEmpty name="creditsInSpecializationArea">
					<bean:write name="infoStudentEnrolmentContext" property="creditsInSpecializationArea" />
				</logic:notEmpty>
				<logic:empty name="creditsInSpecializationArea">
					0
				</logic:empty>
				&nbsp;<bean:message key="label.student.enrollment.from"/>&nbsp;
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.specializationCredits" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea" />:&nbsp;
			</td>
			<td>
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.name" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>
				<bean:message key="label.branch.credits" />:&nbsp;
			</td>
			<td>
				<logic:notEmpty name="creditsInSecundaryArea">
					<bean:write name="infoStudentEnrolmentContext" property="creditsInSecundaryArea" />
				</logic:notEmpty>
				<logic:empty name="creditsInSecundaryArea">
					0
				</logic:empty>
				&nbsp;<bean:message key="label.student.enrollment.from"/>&nbsp;
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.secondaryCredits" />
			</td>
		</tr>
	</logic:present>
	<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
		<tr>
			<td>
				<bean:message key="label.student.enrollment.specializationArea" />:&nbsp;
			</td>
			<td colspan='4' style="text-align:left">
				<bean:message key="label.student.enrollment.no.area" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea" />:&nbsp;
			</td>
			<td colspan='4' style="text-align:left">
				<bean:message key="label.student.enrollment.no.area" />
			</td>
		</tr>
	</logic:notPresent>
	<tr>
		<td colspan="5">
			<br />
			<b><bean:message key="message.student.enrolled.curricularCourses" /></b>
		</td>
	</tr>
	<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" type="DataBeans.InfoEnrolment">
		<tr>
			<td colspan='5'>
				<bean:write name="enrollmentElem" property="infoCurricularCourse.name"/>
			</td>
		</tr>
	</logic:iterate>
</table>
<logic:present name="curriculum">
	<table>
		<tr>
			<td colspan="5">
				<br />
				<b><bean:message key="message.student.curriculum" /></b>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="label.executionYear" /></td>
			<td class="listClasses-header"><bean:message key="label.semester" /></td>
			<td class="listClasses-header"><bean:message key="label.degree.name" /></td>
			<td class="listClasses-header"><bean:message key="label.curricular.course.name" /></td>
			<td class="listClasses-header"><bean:message key="label.finalEvaluation" /></td>
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
				</td>
			  <td class="listClasses">
				<logic:notEqual name="curriculumElem" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
					<bean:message name="curriculumElem" property="enrolmentState.name" bundle="ENUMERATION_RESOURCES" />
				</logic:notEqual>
				
				<logic:equal name="curriculumElem" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
					<bean:write name="curriculumElem" property="infoEnrolmentEvaluation.grade"/>
				</logic:equal>
			  </td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
