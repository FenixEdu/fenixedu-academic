<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="Util.EnrollmentState" %>
<span class="error"><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>
<logic:present name="infoCurriculumHistoricReport">
	<bean:define id="executionYear" name="infoCurriculumHistoricReport" property="infoExecutionYear"/>
	<bean:define id="semester" name="infoCurriculumHistoricReport" property="semester"/>
	<bean:define id="evaluated" name="infoCurriculumHistoricReport" property="evaluated" type="java.lang.Integer"/>
	<bean:define id="enrolled" name="infoCurriculumHistoricReport" property="enrolled" type="java.lang.Integer"/>
	<bean:define id="approved" name="infoCurriculumHistoricReport" property="aproved" type="java.lang.Integer"/>
	
	<h3 class="bluetxt"><bean:message key="message.teachingReport.executionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	&nbsp;<bean:write name="executionYear" property="year" />*&nbsp;-&nbsp;
	<bean:message key="label.period" arg0="<%= String.valueOf(semester) %>" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h3>
	
	<table width="90%" border="0" cellspacing="1">
		<tr>
			<td width="35%"><strong><bean:message key="message.teachingReport.curricularName" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" 
							property="infoCurricularCourse.name"/>
							&nbsp;- &nbsp;
				<bean:write name="infoCurriculumHistoricReport" 
							property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="enrolled" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="evaluated"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="approved"/></td>
		</tr>
		<% int ap_en = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><%= ap_en %>%</td>
		</tr>
		<% int ap_ev = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><%= ap_ev %>%</td>
		</tr>
	</table>
	<br/>
	<logic:notEmpty name="infoCurriculumHistoricReport" property="enrollments">
		<h3 class="bluetxt"><bean:message key="label.students.enrolled.exam" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h3>
		<table cellspacing="1" cellpadding="1">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.number" bundle="CURRICULUM_HISTORIC_RESOURCES" /> 
		   		</td>
				<td class="listClasses-header">
					<bean:message key="label.name" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</td>		
			   	<td class="listClasses-header">
					<bean:message key="label.Degree" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</td>
				<td class="listClasses-header">
					<bean:message key="label.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</td>
			</tr>
			<logic:iterate id="enrollment" name="infoCurriculumHistoricReport" property="enrollments">
				<tr>
					<td class="listClasses">
					 	<bean:write name="enrollment" property="infoStudentCurricularPlan.infoStudent.number"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrollment" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrollment" property="infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.sigla"/>
					 </td>
					 <td class="listClasses">
					 	<logic:notEqual name="enrollment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
							<bean:message name="enrollment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
						</logic:notEqual>
				
						<logic:equal name="enrollment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
							<bean:write name="enrollment" property="infoEnrolmentEvaluation.grade"/>
						</logic:equal>
					 </td>
				</tr>
			</logic:iterate>
		</table>
		</logic:notEmpty>
		<br />
		<p>
			<bean:message key="message.teachingReport.note1" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			<br />
			<bean:message key="message.teachingReport.note2" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
		</p>
	</table>
</logic:present>
			 
