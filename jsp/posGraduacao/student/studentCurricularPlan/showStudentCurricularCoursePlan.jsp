<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<h2 align="left"><bean:message key="title.studentCurricularPlan"/></h2>

<table border="0" cellspacing="3" cellpadding="10">
	<tr>
		<td>
			<b><bean:message key="label.student.degree" /></b>
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.name" />
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome" />
		</td>						
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.branch" /></b>
			<logic:empty name="studentCurricularPlan" property="infoBranch">
				<bean:message key="label.masterDegree.administrativeOffice.noBranch"/>
			</logic:empty>
			<logic:notEmpty name="studentCurricularPlan" property="infoBranch">
				<logic:equal name="studentCurricularPlan" property="infoBranch.name" value="<%= new String("") %>">
					<bean:message key="label.masterDegree.administrativeOffice.noBranch"/>
				</logic:equal>
				<logic:notEqual name="studentCurricularPlan" property="infoBranch.name" value="<%= new String("") %>">
						<bean:write name="studentCurricularPlan" property="infoBranch.name"/>
				</logic:notEqual>
			</logic:notEmpty>
		</td>						
	</tr>		
	<tr>
		<td>
			<b><bean:message key="label.student.specialization" /></b>
			<bean:write name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES" />
		</td>
	</tr>		
	<tr>
		<td>
			<b><bean:message key="label.student.state" /></b>
			<bean:message name="studentCurricularPlan" property="currentState.name" bundle="ENUMERATION_RESOURCES"/>
		</td>						
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.startDate" /></b>
			<bean:write name="studentCurricularPlan" property="startDateFormatted" />
		</td>								
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.credits" /></b>
			<bean:write name="studentCurricularPlan" property="givenCredits" />
		</td>
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.completedCourses" /></b>	
			<bean:write name="studentCurricularPlan" property="completedCourses" />
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="label.student.enrolledCourses" /></b>
			<bean:write name="studentCurricularPlan" property="enrolledCourses" />
		</td>
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.classification" /></b>
			<bean:write name="studentCurricularPlan" property="classification" />
		</td>
	</tr>	
	<tr>
		<td>
		<strong><bean:message key="label.student.observations"/></strong>
		<bean:write name="studentCurricularPlan" property="observations"/>
		</td>
	</tr>
	<tr>
		<td>
			<logic:notPresent name="studentCurricularPlan" property="infoEnrolments">
				<p><bean:message key="message.no.enrolments" /></p>
			</logic:notPresent>
			
			<logic:present name="studentCurricularPlan" property="infoEnrolments">
				<bean:size id="sizeEnrolments" name="studentCurricularPlan" property="infoEnrolments" />

				<logic:lessEqual name="sizeEnrolments" value="0">
					<p><h2><bean:message key="message.no.enrolments" /></h2></p>
				</logic:lessEqual>
				
				<logic:greaterThan name="sizeEnrolments" value="0">
					<table>
						<tr>
							<td colspan="3" align="center"><h3><bean:message key="title.enrolments"/></h3></td>
						</tr>	
						<tr>
							<th align="left"><bean:message key="label.enrolment.curricularCourse"/></th>
							<th align="left"><bean:message key="label.curricularPlan"/></th>
							<th align="left"><bean:message key="label.enrolment.type"/></th>
							<th align="left"><bean:message key="label.enrolment.state"/></th>
							<th align="left"><bean:message key="label.enrolment.year"/></th>							
						</tr>
						<logic:iterate id="infoEnrolment" name="studentCurricularPlan" property="infoEnrolments">
							<tr>	
								<td>
									<bean:write name="infoEnrolment" property="infoCurricularCourse.code" />&nbsp;
									<bean:write name="infoEnrolment" property="infoCurricularCourse.name" />
								</td>
								<td>
									<bean:write name="infoEnrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.name" />
								</td>									
								<td>
									<bean:message name="infoEnrolment" property="enrollmentTypeResourceKey" bundle="ENUMERATION_RESOURCES"/>
								</td>							
								<td>
									<bean:define id="state" name="infoEnrolment" property="enrollmentState" />
									<bean:message key="<%= pageContext.findAttribute("state").toString() %>"/>
								</td>
								<td>
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year" />&nbsp;
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.name" />
								</td>
							</tr>		
						</logic:iterate>
					</table>
				</logic:greaterThan>
			</logic:present>				
		</td>
	</tr>	
</table>	
<bean:define id="link">/editStudentCurricularPlan.do?method=prepare&studentCurricularPlanId=<bean:write name="studentCurricularPlan" property="idInternal"/></bean:define>   
<html:link page='<%= pageContext.findAttribute("link").toString() %>'>
	<bean:message key="label.masterDegree.administrativeOffice.edit"/>
</html:link>		






