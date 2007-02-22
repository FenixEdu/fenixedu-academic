<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:xhtml/>

<span class="error"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>

<logic:present name="infoCurriculumHistoricReport">
	<bean:define id="executionYear" name="infoCurriculumHistoricReport" property="executionYear"/>
	<bean:define id="semester" name="infoCurriculumHistoricReport" property="semester" type="java.lang.Integer"/>
	
	<h3 class="bluetxt">
		<bean:message key="message.teachingReport.executionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
		&nbsp;<bean:write name="executionYear" property="year" />*&nbsp;-&nbsp;
		<bean:message key="label.period" arg0="<%=semester.toString()%>" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	</h3>
	
	<table width="90%" border="0" cellspacing="1" style="mbottom1">
		<tr>
			<td width="35%">
				<strong><bean:message key="message.teachingReport.curricularName" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong>
			</td>
			<td>
				<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.name"/>
					&nbsp;- &nbsp;
				<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.degreeCurricularPlan.name"/>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="enroled"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="evaluated"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="approved"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="ratioApprovedEnroled"/>%</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="ratioApprovedEvaluated"/>%</td>
		</tr>
	</table>

	<logic:notEmpty name="infoCurriculumHistoricReport" property="enrolments">
		<h3 class="bluetxt"><bean:message key="label.students.enrolled.exam" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h3>
		<table cellspacing="1" cellpadding="1">
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.number" bundle="CURRICULUM_HISTORIC_RESOURCES" /> 
		   		</th>
				<th class="listClasses-header">
					<bean:message key="label.name" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>		
			   	<th class="listClasses-header">
					<bean:message key="label.Degree" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.normal.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.special.season.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.improvment.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.equivalence.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
				<th class="listClasses-header">
					<bean:message key="label.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			</tr>
			<logic:iterate id="enrolment" name="infoCurriculumHistoricReport" property="enrolments" type="net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoEnrolmentHistoricReport">
				<tr>
					<td class="listClasses">
					 	<bean:write name="enrolment" property="studentCurricularPlan.registration.number"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="studentCurricularPlan.registration.person.nome"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="studentCurricularPlan.degreeCurricularPlan.degree.sigla"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="latestNormalEnrolmentEvaluationInformation"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="latestSpecialSeasonEnrolmentEvaluationInformation"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="latestImprovementEnrolmentEvaluationInformation"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="latestEquivalenceEnrolmentEvaluationInformation"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="latestEnrolmentEvaluationInformation"/>
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

</logic:present>
