<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>


<h2><bean:message key="label.departmentTeachersList.title"/></h2>
<h3>
	<logic:iterate id="department" name="departmentsList" indexId="nrIter">
		<logic:equal name="nrIter" value="0">
			<bean:write name="department" property="realName"/>
		</logic:equal>
		<logic:notEqual name="nrIter" value="0">
			, <bean:write name="department" property="realName"/>
		</logic:notEqual>
	</logic:iterate>
</h3>

<html:form action="/prepareListDepartmentTeachersCredits">
	<logic:notEmpty name="executionPeriods">
		<p><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId" onchange="this.form.submit()">
			<html:option key="choose.execution.period" value=""/>
			<html:options collection="executionPeriods" property="value" labelProperty="label"/>
			</html:select>
			<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
		</p>
	</logic:notEmpty>
</html:form>

<br />

<tiles:insert definition="creditsLegend"/>

<bean:define id="executionPeriodId" name="executionPeriodForm" property="executionPeriodId"/>
<bean:define id="teachersCreditsListSize" name="teachersCreditsListSize"/>
<u><bean:message key="label.departmentTeachersList.teachersFound" arg0="<%= teachersCreditsListSize.toString() %>"/></u>
<br />
<table class="ts01">
	<tr>
		<th colspan="3"><bean:message key="label.teacher" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>		
		<th colspan="8"><bean:message key="label.teacherService.credits.resume"/></th>				
		<th colspan="4"><bean:message key="label.teacherService.credits"/></th>	
		<th></th>
	</tr>
	<tr>		
		<th>
			<html:link page="/prepareListDepartmentTeachersCredits.do?sortBy=number" paramProperty="executionPeriodId">
				<bean:message key="label.departmentTeachersList.teacherNumber" />
			</html:link>
		</th>
		<th>
			<html:link page="/prepareListDepartmentTeachersCredits.do?sortBy=name" paramProperty="executionPeriodId">
				<bean:message key="label.departmentTeachersList.teacherName" />
			</html:link>
		</th>
		<th><bean:message key="label.departmentTeachersList.teacherCategory" /></th>		
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
		<th><bean:message key="label.departmentTeachersList.teacherCreditsSheet.details" /></th>
	</tr>
	<logic:iterate id="teachersCredits" name="teachersCreditsList">	
		<bean:define id="executionPeriod" name="teachersCredits" property="creditLineDTO.executionPeriod"/>	
		<tr>	
			<td>
				<bean:write name="teachersCredits" property="teacher.teacherNumber"/>
			</td>
			<td style="text-align:left">
				<bean:write name="teachersCredits" property="teacher.person.name"/>			
			</td>
			<td>
				<logic:present name="teachersCredits" property="category">
					<label title='<bean:write name="teachersCredits" property="category.name.content" />'>
						<bean:write name="teachersCredits" property="category.code"/>
					</label>						
				</logic:present>
				<logic:notPresent name="teachersCredits" property="category">
					--
				</logic:notPresent>
			</td>
			<bean:define id="totalLineCredits" name="teachersCredits" property="creditLineDTO.totalCredits"/>
			<bean:define id="balanceOfCredits" name="teachersCredits" property="creditLineDTO.balanceOfCredits"/>
			<bean:define id="mandatoryLessonHours" name="teachersCredits" property="creditLineDTO.mandatoryLessonHours"/>
						
			<% int mandatoryHours = ((Integer)mandatoryLessonHours).intValue(); %>	
					
			<logic:notEqual name="executionPeriod" property="executionYear.year" value="2002/2003">					
				<tiles:insert definition="creditsResumeTableLine" flush="false">
					<tiles:put name="creditLineDTO" beanName="teachersCredits" beanProperty="creditLineDTO"/>
				</tiles:insert>				
				<td>
					<fmt:formatNumber minFractionDigits="2" pattern="###.##">
						<bean:write name="totalLineCredits"/>
					</fmt:formatNumber>
				</td>				
				<td>
					<fmt:formatNumber minFractionDigits="2" pattern="###.##">
						<%= mandatoryHours %>
					</fmt:formatNumber>
				</td>																				
				<% double totalCredits = (Math.round(((((Double)totalLineCredits).doubleValue() - mandatoryHours) * 100.0))) / 100.0; %>
				<td>
					<fmt:formatNumber minFractionDigits="2" pattern="###.##">					
						<%= totalCredits %>
					</fmt:formatNumber>
				</td>	
				<td>					
					<fmt:formatNumber minFractionDigits="2" pattern="###.##">
						<%= ((Math.round(((((Double)balanceOfCredits).doubleValue() + totalCredits) * 100.0))) / 100.0) %>
					</fmt:formatNumber>
				</td>
				<td>
					<html:link page='<%= "/showFullTeacherCreditsSheet.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teachersCredits" paramProperty="teacher.idInternal">
						<bean:message key="link.view"/>
					</html:link>
				</td>				
			</logic:notEqual>
			<logic:equal name="executionPeriod" property="executionYear.year" value="2002/2003">				
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td style="text-align: center;">--</td>
				<td>
					<%= ((Math.round((((Double)balanceOfCredits).doubleValue() * 100.0))) / 100.0) %>
				</td>
				<td style="text-align: center;">--</td>																		
			</logic:equal>
		</tr>
	</logic:iterate>
</table>