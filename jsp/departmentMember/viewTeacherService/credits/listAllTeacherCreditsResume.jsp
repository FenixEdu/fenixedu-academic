<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<table width="100%" cellspacing="0" cellpadding="5">
	<td class="infoop">
		<span class="emphasis-box">info</span>
	</td>
	<td class="infoop">
		<bean:message key="label.teacherService.credits.explanation"/>
	</td>
</table>
<br />
<br />
<bean:define id="teacherID" name="teacher" property="idInternal" />
<logic:empty name="creditsLines">
	<span class="error"><bean:message key="message.teacherCredit.notFound"/></span>
</logic:empty>
<logic:notEmpty name="creditsLines">
	<table cellpadding="3" cellspacing="1">
		<tr>
			<td colspan="3"> </td>
			<td colspan="3" class="listClasses-header">Créditos</td>			
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="label.executionPeriod" /></td>
			<td class="listClasses-header" colspan="2"><bean:message key="label.teacherService.credits.resume" /></td>			
			<td class="listClasses-header"><bean:message key="label.teacherService.credits.obtained"/></td>
			<td class="listClasses-header"><bean:message key="label.teacherService.credits.mandatory"/></td>						
			<td class="listClasses-header"><bean:message key="label.teacherService.credits.final"/></td>		
		</tr>
	<% double totalCreditsBalance = 0; %>		
	<logic:iterate id="creditLineDTO" name="creditsLines" indexId="nrLine">
		<tr>
			<td class="listClasses" >
				<bean:define id="executionPeriod" name="creditLineDTO" property="executionPeriod"/>
				<bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/>
			</td>
			<logic:equal name="nrLine" value="0">
				<bean:define id="pastCredits" name="creditLineDTO" property="pastServiceCredits"/>				
				<td colspan="2" class="listClasses" >
					<b><bean:write name="pastCredits"/></b> (CP)
				</td>				
				<% totalCreditsBalance = ((Double)pastCredits).doubleValue(); %>
				<td class="listClasses">0.0</td>
				<td class="listClasses">0.0</td>
				<td class="listClasses"><bean:write name="creditLineDTO" property="pastServiceCredits"/></td>				
			</logic:equal>
			<logic:notEqual name="nrLine" value="0">
				<td class="listClasses" >
					<tiles:insert definition="creditsResumeLine" flush="false">
						<tiles:put name="creditLineDTO" beanName="creditLineDTO"/>
					</tiles:insert>
				</td>
				<td class="listClasses" >
					<html:link page='<%= "/showFullTeacherCreditsSheet.do?method=showTeacherCredits&amp;teacherId=" + teacherID %>' paramId="executionPeriodId" paramName="executionPeriod" paramProperty="idInternal">
						<bean:message key="link.teacherService.credits.details"/>
					</html:link>
				</td>
				<bean:define id="totalLineCredits" name="creditLineDTO" property="totalCredits"/>
				<bean:define id="mandatoryLessonHours" name="creditLineDTO" property="mandatoryLessonHours"/>
				<td class="listClasses">
					<fmt:formatNumber pattern="###.##">
						<bean:write name="totalLineCredits"/>
					</fmt:formatNumber>
				</td>
				<td class="listClasses"><%= ((Integer)mandatoryLessonHours).intValue() %></td>
				<% totalCreditsBalance += ((Double)totalLineCredits).doubleValue() - ((Integer)mandatoryLessonHours).intValue(); %>
				<td class="listClasses">
					<fmt:formatNumber pattern="###.##">
						<%= ((Double)totalLineCredits).doubleValue() - ((Integer)mandatoryLessonHours).intValue() %>
					</fmt:formatNumber>
				</td>
			</logic:notEqual>
		</tr>
	</logic:iterate>
	<tr>
		<td></td><td></td><td></td><td></td><td></td>
		<td>			
			<b><bean:message key="label.teacherService.credits.total"/>: </b>
			<fmt:formatNumber pattern="###.##">
				<%= totalCreditsBalance %>
			</fmt:formatNumber>
		</td>
	</tr>
	</table>
	
	<br />
	<br />
	<u><strong><bean:message key="label.credits.legenda" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong></u>:
	<p>
	<bean:message key="label.credits.pastCredits.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.pastCredits.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.lessons.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<%--	<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.masterDegreeLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp; --%>
	<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.supportLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.degreeFinalProjectStudents.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.institutionWorkTime.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.otherTypeCreditLine.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.otherTypeCreditLine.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.managementPositions.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.managementPositions.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.serviceExemptionSituations.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.serviceExemptionSituations.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>.
	
</logic:notEmpty>
