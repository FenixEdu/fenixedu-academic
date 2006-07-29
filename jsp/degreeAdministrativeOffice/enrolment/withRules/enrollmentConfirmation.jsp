<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>

<style>
.enrollment_confirmation {
padding: 0.5em;
background-color: #000;
color: #efe;
font-weight: bold;
}
</style>


<h2><bean:message key="title.student.enrollment.resume" bundle="STUDENT_RESOURCES"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>

	<div class="infoselected">
	<p><b><bean:message bundle="APPLICATION_RESOURCES" key="label.student"/>:</b> <bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome" /> / 
	<bean:message key="label.student.number"/> <bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number" /></p>
	<p><b><bean:message key="label.student.enrollment.executionPeriod"/></b>: <bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name" /> <bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" /></p>
	</div>

	
	<br />	
	<p><span class="enrollment_confirmation"><bean:message key="message.student.enrollment.confirmation" /></span></p>
	<br />
	
	
	<table class="style1">
	<tr>
		<th class="listClasses-header"><bean:message key="message.student.enrolled.curricularCourses" /></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
	</tr>
	<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment">
		<tr>
			<td class="listClasses courses">
				<bean:write name="enrollmentElem" property="infoCurricularCourse.name"/>
			</td>
			<td class="listClasses">
				<a href="@enrollment.faq.url@" target="_blank">
					<bean:define id="condition" name="enrollmentElem" property="condition"/>
					<bean:message key='<%=condition.toString()%>' bundle="ENUMERATION_RESOURCES"/>
				</a>
			</td>
		</tr>
	</logic:iterate>
	</table>
	
	
	<br />

	<div class="infoop">
	<h4><bean:message key="title.credits.warning"/>:</h4>
	<bean:message key="label.credits.warning"/>
	</div>
	
	<br />


<table class="style1">
	<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.student.enrollment.specializationArea" />: (<bean:message key="label.student.enrollment.branch" bundle="STUDENT_RESOURCES"/>)
			</th>
			<td class="listClasses">
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.name" />&nbsp;
			</td>
					
		<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch">
		<th class="listClasses-header">
				<bean:message key="label.branch.credits" />:&nbsp;
		</th>
		<td class="listClasses">
			<bean:write name="infoStudentEnrolmentContext" property="creditsInSpecializationArea" />
			&nbsp;<bean:message key="label.student.enrollment.from"/>&nbsp;
			<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.specializationCredits" />
		</td>
		</tr>
		<tr>
			<th class="listClasses-header"><bean:message key="label.student.enrollment.secondaryArea" />:&nbsp;</th>
			<td class="listClasses"><bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.name" /></td> 
			<th class="listClasses-header"><bean:message key="label.branch.credits" />:&nbsp;</th>
			<td class="listClasses">
				<bean:write name="infoStudentEnrolmentContext" property="creditsInSecundaryArea" />
				&nbsp;<bean:message key="label.student.enrollment.from"/>&nbsp;
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.secondaryCredits" />
			</td>
		</tr>
		</logic:present>
		<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch">
		</tr>
		</logic:notPresent>
	</logic:present>
	<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.student.enrollment.specializationArea" /> (<bean:message key="label.student.enrollment.branch" bundle="STUDENT_RESOURCES"/>):
			</th>
			<td  class="listClasses">
				<bean:message key="label.student.enrollment.no.area" />
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.student.enrollment.secondaryArea" />:&nbsp;
			</th>
			<td  class="listClasses">
				<bean:message key="label.student.enrollment.no.area" />
			</td>
		</tr>
	</logic:notPresent>
	</table>

	
	<br/>


<logic:present name="curriculum">
	<h4><bean:message key="message.student.curriculum" /></h4>
	<table class="style1">
		<tr class="header">
			<th class="listClasses-header"><bean:message key="label.executionYear" /></th>
			<th class="listClasses-header"><bean:message key="label.semester" /></th>
			<th class="listClasses-header"><bean:message key="label.degree.name" /></th>
			<th class="listClasses-header"><bean:message key="label.curricular.course.name" /></th>
			<th class="listClasses-header"><bean:message key="label.finalEvaluation" /></th>
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
					<% if ( !((InfoEnrolment) curriculumElem).getEnrollmentTypeResourceKey().equals("option.curricularCourse.normal") ) {%>
						(<bean:message name="curriculumElem" property="enrollmentTypeResourceKey" />)
					<% } %>
				</td>
			  <td class="listClasses">
				<logic:notEqual name="curriculumElem" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:message name="curriculumElem" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
				</logic:notEqual>
				
				<logic:equal name="curriculumElem" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:write name="curriculumElem" property="infoEnrolmentEvaluation.grade"/>
				</logic:equal>
			  </td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
