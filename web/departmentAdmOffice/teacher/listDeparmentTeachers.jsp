<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sortBy" property="sortBy" value=""/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="sortBy"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>
	<table width="100%">
		<tr>
			<th class="listClasses-header" width="10%">
				<html:link href="javascript:void(0)" onclick="document.forms[0].sortBy.value='teacherNumber'; document.forms[0].submit();return false;">			
					<bean:message key="label.departmentTeachersList.teacherNumber" />
				</html:link>
			</th>
			<th class="listClasses-header" style="text-align:left">
				<html:link href="javascript:void(0)" onclick="document.forms[0].sortBy.value='teacherName';document.forms[0].submit();return false;">			
					<bean:message key="label.departmentTeachersList.teacherName" />
				</html:link>
			</th>
			<th class="listClasses-header" width="10%">
				<html:link href="javascript:void(0)" onclick="document.forms[0].sortBy.value='category.code';document.forms[0].submit();return false;">
					<bean:message key="label.departmentTeachersList.teacherCategory" />
				</html:link>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.credits.resume" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</th>
			<th class="listClasses-header" width="10%">
				<bean:message key="label.departmentTeachersList.teacherCreditsSheet.details" />
			</th>
			
		</tr>
		<logic:iterate id="infoTeacherCreditsDetails" name="infoTeacherList">
			<tr>	
				<td class="listClasses">
					<bean:write name="infoTeacherCreditsDetails" property="teacherNumber"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<bean:write name="infoTeacherCreditsDetails" property="teacherName"/>			
				</td>
				<td class="listClasses">
					<logic:present name="infoTeacherCreditsDetails" property="category">
						<label title='<bean:write name="infoTeacherCreditsDetails" property="category.name.content" />'>
							<bean:write name="infoTeacherCreditsDetails" property="category.code"/>
						</label>						
					</logic:present>
					<logic:notPresent name="infoTeacherCreditsDetails" property="category">
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