<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3>
	<bean:write name="infoDepartment" property="name"/>
</h3>
<br />

<tiles:insert definition="creditsLegend"/>

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
					<font size="-5">
						<tiles:insert definition="creditsResume" flush="false">
							<tiles:put name="infoCredits" beanName="infoTeacherCreditsDetails" beanProperty="infoCredits"/>
						</tiles:insert>
					</font>
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