<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3>
	<bean:write name="infoDepartment" property="name"/>
</h3>
<br />
<bean:size id="listSize" name="infoTeacherList"/>
<u><bean:message key="label.departmentTeachersList.teachersFound" arg0="<%= listSize.toString() %>"/></u>
<br />
<html:form action="/listDepartmentTeachers">
	<html:hidden property="idInternal"/>
	<html:hidden property="sortBy" value=""/>
	<html:hidden property="method" value="sortBy"/>
	<table width="100%">
		<tr>
			<td class="listClasses-header" width="10%">
				<html:link href="javascript:void" onclick="document.forms[0].sortBy.value='teacherNumber'; document.forms[0].submit();return true;">			
					<bean:message key="label.departmentTeachersList.teacherNumber" />
				</html:link>
			</td>
			<td class="listClasses-header" style="text-align:left">
				<html:link href="javascript:void" onclick="document.forms[0].sortBy.value='infoPerson.nome';document.forms[0].submit();return true;">			
					<bean:message key="label.departmentTeachersList.teacherName" />
				</html:link>
			</td>
			<td class="listClasses-header" style="text-align:left">
				<html:link href="javascript:void" onclick="document.forms[0].sortBy.value='infoCategory.shortName';document.forms[0].submit();return true;">
					<bean:message key="label.departmentTeachersList.teacherCategory" />
				</html:link>
			</td>
		</tr>
		<logic:iterate id="infoTeacher" name="infoTeacherList">
			<tr>	
				<td class="listClasses">
					<bean:write name="infoTeacher" property="teacherNumber"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<bean:write name="infoTeacher" property="infoPerson.nome"/>			
				</td>
				<td class="listClasses" style="text-align:left">
					<logic:present name="infoTeacher" property="infoCategory">
						<bean:write name="infoTeacher" property="infoCategory.shortName"/>			
					</logic:present>
					<logic:notPresent name="infoTeacher" property="infoCategory">
						--
					</logic:notPresent>
				</td>
				
			</tr>
		</logic:iterate>
	</table>
</html:form>