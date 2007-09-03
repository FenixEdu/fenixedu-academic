<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.permissionSupportService"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
			<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.permissionSupportService"/>
	</em>
</p>


<html:form action="/valuationGroupingSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>


<p>
	<html:link href="javascript:document.forms[0].viewType.value=2; document.forms[0].method.value='loadValuationGroupingsForPermissionServices'; document.forms[0].submit();">
	<bean:message key="label.teacherService.viewByValuationGrouping"/> 
	</html:link> | 
	<b><bean:message key="label.teacherService.viewByPerson"/></b>
</p>


<table class="tstyle5 thlight">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.person"/>:
		</th>
		<td>
			<html:select property="person" onchange="this.form.method.value='loadValuationGroupingsForPermissionServices'; this.form.submit()">
				<html:options collection="departmentPersonList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
</table>

<table class="tstyle5 thlight">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.permissions"/>:
		</th>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.phaseManagementPermission" property="phaseManagementPermission"/> <bean:message key="label.teacherServiceDistribution.phaseManagementPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.automaticValuationPermission" property="automaticValuationPermission"/> <bean:message key="label.teacherServiceDistribution.automaticValuationPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.omissionConfigurationPermission" property="omissionConfigurationPermission"/> <bean:message key="label.teacherServiceDistribution.omissionConfigurationPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.valuationCompetenceCoursesAndTeachersManagementPermission" property="valuationCompetenceCoursesAndTeachersManagementPermission"/> <bean:message key="label.teacherServiceDistribution.valuationCompetenceCoursesAndTeachersManagementPermission"/>
		</td>
	</tr>
</table>


<p>
	<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setPermissionsToPerson'; this.form.submit()">
		<bean:message key="button.update"/>
	</html:button>
</p>

</html:form>



<logic:empty name="personPermissionsDTOEntryList">
	<p>
		<span class="error">
			<bean:message key="label.teacherServiceDistribution.noPersonsWithValidPermissions"/>
		</span>
	</p>
</logic:empty>

<logic:notEmpty name="personPermissionsDTOEntryList">
	<table class='tstyle4 thlight thmiddle'>
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.person"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.phaseManagementPermission"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.automaticValuationPermission"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.omissionConfigurationPermission"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.valuationCompetenceCoursesAndTeachersManagementPermission"/>
			</th>
		</tr>
	<logic:iterate name="personPermissionsDTOEntryList" id="personPermissionsDTOEntry">
		<tr>
			<td>
				<bean:write name="personPermissionsDTOEntry" property="person.name"/>
			</td>
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="phaseManagementPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="phaseManagementPermission" value="true">
					<bean:message key="label.empty"/>
				</logic:notEqual>
			</td>			
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="automaticValuationPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="automaticValuationPermission" value="true">
					<bean:message key="label.empty"/>
				</logic:notEqual>
			</td>			
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="omissionConfigurationPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="omissionConfigurationPermission" value="true">
					<bean:message key="label.empty"/>
				</logic:notEqual>
			</td>			
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="valuationCompetenceCoursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="valuationCompetenceCoursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.empty"/>
				</logic:notEqual>
			</td>			
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>


