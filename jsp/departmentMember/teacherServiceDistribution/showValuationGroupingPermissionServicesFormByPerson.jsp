<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h3>
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
</h3>

<ul>
<li>
<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
</li>
</ul>

<html:form action="/valuationGroupingSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<br/>


<html:link href="javascript:document.forms[0].viewType.value=2; document.forms[0].method.value='loadValuationGroupingsForPermissionServices'; document.forms[0].submit();">
<bean:message key="label.teacherService.viewByValuationGrouping"/> 
</html:link> | 
<b> <bean:message key="label.teacherService.viewByPerson"/> </b>
<br/>
<br/>
<br/>

<logic:empty name="personPermissionsDTOEntryList">
	<span class="error">
		<b><bean:message key="label.teacherServiceDistribution.noPersonsWithValidPermissions"/></b>
	</span>
</logic:empty>

<logic:notEmpty name="personPermissionsDTOEntryList">
	<table class='vtsbc' width="85%">
		<tr>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.person"/></b>
			</th>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.phaseManagementPermission"/></b>
			</th>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.automaticValuationPermission"/></b>
			</th>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.omissionConfigurationPermission"/></b>
			</th>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.valuationCompetenceCoursesAndTeachersManagementPermission"/></b>
			</th>
		</tr>
	<logic:iterate name="personPermissionsDTOEntryList" id="personPermissionsDTOEntry">
		<tr>
			<td class="courses" width="40%">
				<bean:write name="personPermissionsDTOEntry" property="person.name"/>
			</td>
			<td width="5%">
				<logic:equal name="personPermissionsDTOEntry" property="phaseManagementPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="phaseManagementPermission" value="true">
					<bean:message key="label.no"/>
				</logic:notEqual>
			</td>			
			<td width="10%">
				<logic:equal name="personPermissionsDTOEntry" property="automaticValuationPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="automaticValuationPermission" value="true">
					<bean:message key="label.no"/>
				</logic:notEqual>
			</td>			
			<td width="15%">
				<logic:equal name="personPermissionsDTOEntry" property="omissionConfigurationPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="omissionConfigurationPermission" value="true">
					<bean:message key="label.no"/>
				</logic:notEqual>
			</td>			
			<td width="15%">
				<logic:equal name="personPermissionsDTOEntry" property="valuationCompetenceCoursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="valuationCompetenceCoursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.no"/>
				</logic:notEqual>
			</td>			
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<br/>
<br/>
<br/>

<table class="search">
	<tr>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.person"/>:</b>
		</td>
		<td>
			<html:select property="person" onchange="this.form.method.value='loadValuationGroupingsForPermissionServices'; this.form.submit()">
				<html:options collection="departmentPersonList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
</table>

<br/>

<b> <bean:message key="label.teacherServiceDistribution.permissions"/>:</b>
<ul>
	<li> <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.phaseManagementPermission" property="phaseManagementPermission"/> <bean:message key="label.teacherServiceDistribution.phaseManagementPermission"/> </li>
	<li> <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.automaticValuationPermission" property="automaticValuationPermission"/> <bean:message key="label.teacherServiceDistribution.automaticValuationPermission"/> </li>
	<li> <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.omissionConfigurationPermission" property="omissionConfigurationPermission"/> <bean:message key="label.teacherServiceDistribution.omissionConfigurationPermission"/> </li>
	<li> <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.valuationCompetenceCoursesAndTeachersManagementPermission" property="valuationCompetenceCoursesAndTeachersManagementPermission"/> <bean:message key="label.teacherServiceDistribution.valuationCompetenceCoursesAndTeachersManagementPermission"/> </li>
</ul>

<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setPermissionsToPerson'; this.form.submit()">
	<bean:message key="button.update"/>
</html:button>
</html:form>

<br/>
<br/>

