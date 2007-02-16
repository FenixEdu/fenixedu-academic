<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.Enrolment" %>
<html:xhtml/>

<h2><bean:message key="title.student.enrollment.resume" bundle="STUDENT_RESOURCES"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

	<div class="infoop2">
	<p><b><bean:message bundle="APPLICATION_RESOURCES" key="label.student"/>:</b> <bean:write name="studentCurricularPlan" property="student.person.nome" /> / 
	<bean:message key="label.student.number"/> <bean:write name="studentCurricularPlan" property="student.number" /></p>
	<p><b><bean:message key="label.student.enrollment.executionPeriod"/></b>: <bean:write name="executionPeriod" property="name" /> <bean:write name="executionPeriod" property="executionYear.year" /></p>
	</div>
	

	<p class="mtop2"><span style="padding: 0.5em; background-color: #000; color: #efe; font-weight: bold;"><bean:message key="message.student.enrollment.confirmation" /></span></p>

		
	<table class="tstyle4">
	<tr>
		<th><bean:message key="message.student.enrolled.curricularCourses" /></th>
		<th><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
	</tr>
	<logic:iterate id="enrollmentElem" name="studentCurrentSemesterEnrollments" type="net.sourceforge.fenixedu.domain.Enrolment">
		<tr>
			<td class="listClasses courses">
				<bean:write name="enrollmentElem" property="curricularCourse.name"/>
			</td>
			<td>
				<a href="@enrollment.faq.url@" target="_blank">
					<bean:message name="enrollmentElem" property="condition.name" bundle="ENUMERATION_RESOURCES"/>
				</a>
			</td>
		</tr>
	</logic:iterate>
	</table>
	

	<div class="infoop mvert1">
		<h4><bean:message key="title.credits.warning" bundle="APPLICATION_RESOURCES"/>:</h4>
		<bean:message key="label.credits.warning"/>
	</div>
	

<table class="tstyle4">
	<logic:present name="studentCurricularPlan" property="branch">
		<tr>
			<th>
				<bean:message key="label.student.enrollment.specializationArea" />: (<bean:message key="label.student.enrollment.branch" bundle="STUDENT_RESOURCES"/>)
			</th>
			<td>
				<bean:write name="studentCurricularPlan" property="branch.name" />&nbsp;
			</td>
					
		<logic:present name="studentCurricularPlan" property="secundaryBranch">
		<th>
				<bean:message key="label.branch.credits" />:&nbsp;
		</th>
		<td>
			<bean:write name="studentCurricularPlan" property="creditsInSpecializationArea" />
			&nbsp;<bean:message key="label.student.enrollment.from"/>&nbsp;
			<bean:write name="studentCurricularPlan" property="branch.specializationCredits" />
		</td>
		</tr>
		<tr>
			<th><bean:message key="label.student.enrollment.secondaryArea" />:&nbsp;</th>
			<td><bean:write name="studentCurricularPlan" property="secundaryBranch.name" /></td> 
			<th><bean:message key="label.branch.credits" />:&nbsp;</th>
			<td>
				<bean:write name="studentCurricularPlan" property="creditsInSecundaryArea" />
				&nbsp;<bean:message key="label.student.enrollment.from"/>&nbsp;
				<bean:write name="studentCurricularPlan" property="secundaryBranch.secondaryCredits" />
			</td>
		</tr>
		</logic:present>
		<logic:notPresent name="studentCurricularPlan" property="secundaryBranch">
		</tr>
		</logic:notPresent>
	</logic:present>
	<logic:notPresent name="studentCurricularPlan" property="branch">
		<tr>
			<th>
				<bean:message key="label.student.enrollment.specializationArea" /> (<bean:message key="label.student.enrollment.branch" bundle="STUDENT_RESOURCES"/>):
			</th>
			<td >
				<bean:message key="label.student.enrollment.no.area" />
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.student.enrollment.secondaryArea" />:&nbsp;
			</th>
			<td >
				<bean:message key="label.student.enrollment.no.area" />
			</td>
		</tr>
	</logic:notPresent>
	</table>
	


<logic:present name="curriculum">
	<h4 class="mbottom05"><bean:message key="message.student.curriculum"/></h4>
	<table class="tstyle4 mtop05">
		<tr class="header">
			<th><bean:message key="label.executionYear" /></th>
			<th><bean:message key="label.semester" /></th>
			<th><bean:message key="label.degree.name" /></th>
			<th><bean:message key="label.curricular.course.name" /></th>
			<th><bean:message key="label.finalEvaluation" /></th>
		</tr>
		<logic:iterate id="curriculumElem" name="curriculum">
			<tr>
				<td>
					<bean:write name="curriculumElem" property="executionPeriod.executionYear.year"/>
				</td>
				<td>
					<bean:write name="curriculumElem" property="executionPeriod.semester"/>
				</td>
				<td>
					<bean:write name="curriculumElem" property="curricularCourse.degreeCurricularPlan.degree.sigla"/>
				</td>
				<td style="text-align:left">
					<bean:write name="curriculumElem" property="curricularCourse.name"/>
					
					<% if (curriculumElem instanceof Enrolment &&  ((Enrolment)curriculumElem).isExtraCurricular()) { %>
						(<bean:message bundle="APPLICATION_RESOURCES" key="option.curricularCourse.extra"/>)
					
					<% } else if (curriculumElem instanceof net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse) { %>
						(<bean:message bundle="APPLICATION_RESOURCES" key="option.curricularCourse.optional"/>)
					
	            	<% } %> 
				</td>
			  <td>
				<logic:notEqual name="curriculumElem" property="enrollmentState.name" value="APROVED">
					<bean:message name="curriculumElem" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
				</logic:notEqual>
				
				<logic:equal name="curriculumElem" property="enrollmentState.name" value="APROVED">
					<bean:write name="curriculumElem" property="latestEnrolmentEvaluation.grade"/>
				</logic:equal>
			  </td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
