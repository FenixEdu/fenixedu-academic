<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<span class="error"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>

<logic:present name="infoCurriculumHistoricReport">
	<bean:define id="executionYear" name="infoCurriculumHistoricReport" property="executionYear"/>
	<bean:define id="semester" name="infoCurriculumHistoricReport" property="semester" type="java.lang.Integer"/>
	
	<em><bean:write name="executionYear" property="year" />* - <bean:message key="label.period" arg0="<%=semester.toString()%>" bundle="CURRICULUM_HISTORIC_RESOURCES"/></em>
	<h2>
		<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.name"/>
			&nbsp;- &nbsp;
		<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.degreeCurricularPlan.name"/>
	</h2>
	
	<table width="30%" border="0" cellspacing="1" class="mbottom2">
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
		<h3><bean:message key="label.students.enrolled.exam" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h3>

		<fr:view name="infoCurriculumHistoricReport" property="enrolments" schema="info.enrolment.historic.report">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thbold tacenter" />
			</fr:layout>
		</fr:view>

	</logic:notEmpty>

	<p class="mtop15">
		<bean:message key="message.teachingReport.note1" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
		<br/>
		<bean:message key="message.teachingReport.note2" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	</p>

</logic:present>
