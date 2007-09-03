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
	<b><bean:message key="label.teacherService.viewByValuationGrouping"/> </b> | 
	<html:link href="javascript:document.forms[0].viewType.value=1; document.forms[0].method.value='loadValuationGroupingsForPermissionServices'; document.forms[0].submit();">
		<bean:message key="label.teacherService.viewByPerson"/>
	</html:link> 
</p>


<table class="tstyle5 thlight thmiddle">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>
		</th>
		<td>
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadValuationGroupingsForPermissionServices'; this.form.submit();">
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>			
		</td>
	</tr>
</table>


<logic:present name="parentGroupingName">
	<p>
		<bean:message key="label.teacherServiceDistribution.parentGrouping"/>: <b><bean:write name="parentGroupingName"/></b>
	</p>
</logic:present>

<table class="tstyle5 thlight thmiddle">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.person"/>:
		</th>
		<td>
			<html:select property="person" onchange="this.form.method.value='loadValuationGroupingsForPermissionServices'; this.form.submit();">
				<html:options collection="departmentPersonList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
</table>


<p><b></b></p>

<table class="tstyle5 thlight thmiddle">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.permissions"/>:
		</th>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.coursesAndTeachersValuationPermission" property="coursesAndTeachersValuationPermission"/> <bean:message key="label.teacherServiceDistribution.coursesAndTeachersValuationPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.coursesAndTeachersManagementPermission" property="coursesAndTeachersManagementPermission"/> <bean:message key="label.teacherServiceDistribution.coursesAndTeachersManagementPermission"/>
		</td>
	</tr>
</table>



<p>
	<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='addCoursesAndTeachersValuationPermissionToPerson'; this.form.submit()"><bean:message key="button.update"/></html:button>
</p>


<logic:empty name="personPermissionsDTOEntryListForValuationGrouping">
	<p class="mtop15">
		<em>
			<bean:message key="label.teacherServiceDistribution.noPersonsWithValidPermissionsInGrouping"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="personPermissionsDTOEntryListForValuationGrouping">
	<table class='tstyle4'>
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.person"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.coursesAndTeachersValuationPermission"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.coursesAndTeachersManagementPermission"/>
			</th>
		</tr>
	<logic:notPresent name="notCoursesAndTeachersValuationManagers">
	<logic:iterate name="personPermissionsDTOEntryListForValuationGrouping" id="personPermissionsDTOEntry">
		<tr>
			<td class="courses">
				<bean:write name="personPermissionsDTOEntry" property="person.name"/>
			</td>
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="coursesAndTeachersValuationPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="coursesAndTeachersValuationPermission" value="true">
					<bean:message key="label.empty"/>
				</logic:notEqual>
			</td>			
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="coursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.yes"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="coursesAndTeachersManagementPermission" value="true">
					<bean:message key="label.empty"/>
				</logic:notEqual>
			</td>			
		</tr>
	</logic:iterate>
	</logic:notPresent>
	</table>
</logic:notEmpty>
</html:form>


