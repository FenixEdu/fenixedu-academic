<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3>
	<bean:write name="infoDepartment" property="name"/>
</h3>
<br />

<u><strong><bean:message key="label.credits.legenda" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong></u>:
<p>
<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.lessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.supportLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.degreeFinalProjectStudents.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.institutionWorkTime.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>.
</p>

<bean:size id="listSize" name="infoTeacherList"/>
<u><bean:message key="label.departmentTeachersList.teachersFound" arg0="<%= listSize.toString() %>"/></u>
<br />
<bean:parameter id="executionPeriodId" name="executionPeriodId" />
<html:form action="/listDepartmentTeachers">
	<html:hidden property="idInternal"/>
	<html:hidden property="sortBy" value=""/>
	<html:hidden property="method" value="sortBy"/>
	<html:hidden property="executionPeriodId"/>
	<table width="100%">
		<tr>
			<td class="listClasses-header" width="10%">
				<html:link href="javascript:void()" onclick="document.forms[0].sortBy.value='teacherNumber'; document.forms[0].submit();return true;">			
					<bean:message key="label.departmentTeachersList.teacherNumber" />
				</html:link>
			</td>
			<td class="listClasses-header" style="text-align:left">
				<html:link href="javascript:void()" onclick="document.forms[0].sortBy.value='infoPerson.nome';document.forms[0].submit();return true;">			
					<bean:message key="label.departmentTeachersList.teacherName" />
				</html:link>
			</td>
			<td class="listClasses-header" width="10%">
				<html:link href="javascript:void()" onclick="document.forms[0].sortBy.value='infoCategory.shortName';document.forms[0].submit();return true;">
					<bean:message key="label.departmentTeachersList.teacherCategory" />
				</html:link>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.credits.resume" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.departmentTeachersList.teacherCreditsSheet.details" />
			</td>
			
		</tr>
		<logic:iterate id="infoTeacherCreditsDetails" name="infoTeacherList">
			<tr>	
				<td class="listClasses">
					<bean:write name="infoTeacherCreditsDetails" property="teacherNumber"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<bean:write name="infoTeacherCreditsDetails" property="infoPerson.nome"/>			
				</td>
				<td class="listClasses">
					<logic:present name="infoTeacherCreditsDetails" property="infoCategory">
						<bean:write name="infoTeacherCreditsDetails" property="infoCategory.code"/>			
					</logic:present>
					<logic:notPresent name="infoTeacherCreditsDetails" property="infoCategory">
						--
					</logic:notPresent>
				</td>
				<td class="listClasses">
					<logic:present name="infoTeacherCreditsDetails" property="infoCredits">
						<font size="-5">
							<b>
								<bean:write name="infoTeacherCreditsDetails" property="infoCredits.lessonsFormatted"/>						
							</b>
							<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
							<b>	
								<bean:write name="infoTeacherCreditsDetails" property="infoCredits.supportLessonsFormatted"/>						
							</b>							
							<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,

							<b>
								<bean:write name="infoTeacherCreditsDetails" property="infoCredits.degreeFinalProjectStudentsFormatted"/>						
							</b>							
							<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
							<b>							
								<bean:write name="infoTeacherCreditsDetails" property="infoCredits.institutionWorkTimeFormatted"/>						
							</b>							
							<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
						</font>
					</logic:present>
					
					<logic:notPresent name="infoTeacherCreditsDetails" property="infoCredits">
						--
					</logic:notPresent>
					
				</td>
				<td class="listClasses">
					<html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&amp;page=1&amp;executionPeriodId=" + executionPeriodId %>'  paramId="teacherNumber" paramName="infoTeacherCreditsDetails" paramProperty="teacherNumber">
						<bean:message key="link.view"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</html:form>