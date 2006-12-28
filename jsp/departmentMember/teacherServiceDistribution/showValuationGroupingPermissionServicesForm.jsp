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


<b> <bean:message key="label.teacherService.viewByValuationGrouping"/> </b> | 
<html:link href="javascript:document.valuationGroupingSupportForm.viewType.value=1; document.valuationGroupingSupportForm.method.value='loadValuationGroupingsForPermissionServices'; document.valuationGroupingSupportForm.submit();">
	<bean:message key="label.teacherService.viewByPerson"/>
</html:link> 
<br/>
<br/>
<br/>

<table class="search">
	<tr>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.ValuationGrouping"/></b>
		</td>
		<td>
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadValuationGroupingsForPermissionServices'; this.form.submit();">
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>			
		</td>
	</tr>
</table>
<br/>
<br/>

<logic:present name="parentGroupingName">
	<bean:message key="label.teacherServiceDistribution.parentGrouping"/>: <b><bean:write name="parentGroupingName"/></b>
</logic:present>
<table class="search">
	<tr>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.person"/>:</b>
		</td>
		<td>
			<html:select property="person" onchange="this.form.method.value='loadValuationGroupingsForPermissionServices'; this.form.submit();">
				<html:options collection="departmentPersonList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td>
			
		</td>
	</tr>
</table>
<br/>

<b> <bean:message key="label.teacherServiceDistribution.permissions"/>:</b>
<ul>
	<li> <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.coursesAndTeachersValuationPermission" property="coursesAndTeachersValuationPermission"/> <bean:message key="label.teacherServiceDistribution.coursesAndTeachersValuationPermission"/> </li>
	<li> <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.coursesAndTeachersManagementPermission" property="coursesAndTeachersManagementPermission"/> <bean:message key="label.teacherServiceDistribution.coursesAndTeachersManagementPermission"/> </li>
</ul>
<br/>
<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='addCoursesAndTeachersValuationPermissionToPerson'; this.form.submit()"><bean:message key="button.update"/></html:button>

<br/>
<br/>
<br/>

<logic:empty name="personPermissionsDTOEntryListForValuationGrouping">
	<span class="error">
		<b><bean:message key="label.teacherServiceDistribution.noPersonsWithValidPermissionsInGrouping"/></b>
	</span>
</logic:empty>

<logic:notEmpty name="personPermissionsDTOEntryListForValuationGrouping">
	<table class='vtsbc'>
		<tr>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.person"/></b>
			</th>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.coursesAndTeachersValuationPermission"/></b>
			</th>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.coursesAndTeachersManagementPermission"/></b>
			</th>
		</tr>
	<logic:notPresent name="notCoursesAndTeachersValuationManagers">
	<logic:iterate name="personPermissionsDTOEntryListForValuationGrouping" id="personPermissionsDTOEntry">
		<tr>
			<td class="courses">
				<bean:write name="personPermissionsDTOEntry" property="person.name"/>
			</td>
			<td>
				<logic:equal name="personPermissionsDTOEntry" property="coursesAndTeachersValuationPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="coursesAndTeachersValuationPermission" value="true">
					<bean:message key="label.no"/>
				</logic:notEqual>
			</td>			
			<td>
				<logic:equal name="personPermissionsDTOEntry" property="coursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="coursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.no"/>
				</logic:notEqual>
			</td>			
		</tr>
	</logic:iterate>
	</logic:notPresent>
	</table>
</logic:notEmpty>
</html:form>

<br/>
<br/>

