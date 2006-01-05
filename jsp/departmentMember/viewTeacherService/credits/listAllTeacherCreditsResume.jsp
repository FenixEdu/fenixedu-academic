<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<style>
	table.ts01 {
	border-collapse: collapse;
	margin: 1em 0;
	}
	table.ts01 th {
	border: 1px solid #ccc;
	background-color: #eee;
	padding: 0.5em;
	text-align: center;
	}
	table.ts01 td {
	border: 1px solid #ccc;
	background-color: #fff;
	padding: 0.5em;
	text-align: center;
	}
	table.ts01 .highlight01 {
	background-color: #ffc;
	}
	table.ts01 .aleft {
	text-align: left;
	}
	table.ts01 .aright {
	text-align: right;
	}
	.asterisk01 {
	color: #d42;
	}		
</style>

<h2><bean:message key="label.teacherService.credits"/></h2>
<div class="infoselected">
	<p><bean:message key="label.teacherService.credits.explanation"/></p>
	<p><em><bean:message key="label.teacherService.credits.diferentCategories.explanation"/></em></p>
</div>

<bean:define id="teacherID" name="teacher" property="idInternal" />
<logic:empty name="creditsLines">
	<span class="error"><bean:message key="message.teacherCredit.notFound"/></span>
</logic:empty>
<logic:notEmpty name="creditsLines">
	<table class="ts01" cellpadding="3" cellspacing="1">
		<tr>
			<th></th>
			<th colspan="8"><bean:message key="label.teacherService.credits.resume" /></th>
			<th colspan="4"><bean:message key="label.teacherService.credits"/></th>			
		</tr>
		<tr>
			<th><bean:message key="label.executionPeriod"/></th>
			<th><bean:message key="label.credits.lessons.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>			
			<th><bean:message key="label.credits.supportLessons.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.credits.masterDegreeLessons.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>			
			<th><bean:message key="label.credits.degreeFinalProjectStudents.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.credits.institutionWorkTime.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.credits.otherTypeCreditLine.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.credits.managementPositions.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.credits.serviceExemptionSituations.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.teacherService.credits.obtained"/></th>
			<th><bean:message key="label.teacherService.credits.mandatory"/></th>						
			<th><bean:message key="label.teacherService.credits.final"/></th>		
			<th><bean:message key="label.teacherService.credits.total"/></th>			
		</tr>
	<% double totalCreditsBalance = 0; %>		
	<logic:iterate id="creditLineDTO" name="creditsLines" indexId="nrLine">
		<tr>			
			<bean:define id="executionPeriod" name="creditLineDTO" property="executionPeriod"/>		
			<logic:equal name="nrLine" value="0">
				<td class="aleft">										
					<bean:message key="label.teacherService.credits.until"/> <bean:write name="executionPeriod" property="executionYear.year"/>
				</td>
				<bean:define id="pastCredits" name="creditLineDTO" property="pastServiceCredits"/>							
				<% totalCreditsBalance = ((Double)pastCredits).doubleValue(); %>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td class="aright"><span class="asterisk01">*</span>								
					<fmt:formatNumber pattern="###.##">
						<%= totalCreditsBalance %>
					</fmt:formatNumber>
				</td>			
			</logic:equal>
			<logic:notEqual name="nrLine" value="0">
				<td class="aleft">
					<html:link title="Ver detalhes" page='<%= "/showFullTeacherCreditsSheet.do?method=showTeacherCredits&amp;teacherId=" + teacherID %>' paramId="executionPeriodId" paramName="executionPeriod" paramProperty="idInternal">
						<bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/>
					</html:link>						
				</td>
				<tiles:insert definition="creditsResumeTableLine" flush="false">
					<tiles:put name="creditLineDTO" beanName="creditLineDTO"/>
				</tiles:insert>				
				<bean:define id="totalLineCredits" name="creditLineDTO" property="totalCredits"/>
				<bean:define id="mandatoryLessonHours" name="creditLineDTO" property="mandatoryLessonHours"/>
				<td>
					<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
						<bean:write name="totalLineCredits"/>
					</fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
						<%= ((Integer)mandatoryLessonHours).intValue() %>
					</fmt:formatNumber>
				</td>
				<% totalCreditsBalance += ((Double)totalLineCredits).doubleValue() - ((Integer)mandatoryLessonHours).intValue(); %>
				<td>
					<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
						<%= ((Double)totalLineCredits).doubleValue() - ((Integer)mandatoryLessonHours).intValue() %>
					</fmt:formatNumber>
				</td>
				<logic:equal name="creditsLinesSize" value='<%= (new Integer(nrLine.intValue() + 1)).toString() %>'>
					<td class="highlight01 aright">	
						<bean:message key="label.teacherService.credits.totalSum"/>
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
							<%= totalCreditsBalance %>
						</fmt:formatNumber>
					</td>
				</logic:equal>
				<logic:notEqual name="creditsLinesSize" value='<%= (new Integer(nrLine.intValue() + 1)).toString() %>'>
					<td class="aright">						
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
							<%= totalCreditsBalance %>
						</fmt:formatNumber>
					</td>
				</logic:notEqual>
				
			</logic:notEqual>
		</tr>
	</logic:iterate>
	</table>	
	<p><span class="asterisk01">*</span><bean:message key="label.credits.pastCredits.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></p>
	<br />

	<p><strong><bean:message key="label.credits.legenda" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong></p>
	<p>
	<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.lessons.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.supportLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.masterDegreeLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;	
	<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.degreeFinalProjectStudents.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.institutionWorkTime.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.otherTypeCreditLine.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.otherTypeCreditLine.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.managementPositions.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.managementPositions.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.serviceExemptionSituations.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.serviceExemptionSituations.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>.
	</p>
	
</logic:notEmpty>
