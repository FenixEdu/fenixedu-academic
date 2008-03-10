<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.permissionSupportService"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.permissionSupportService"/>
	</em>
</p>


<html:form action="/tsdSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>


<p>
	<html:link href="javascript:void(0)" onclick="document.forms[0].viewType.value=1; document.forms[0].method.value='loadTeacherServiceDistributionsForPermissionServices'; document.forms[0].submit();">
		<bean:message key="label.teacherServiceDistribution.permissionsForTSDProcess"/>
	</html:link> |
	<b><bean:message key="label.teacherServiceDistribution.permissionsForTSDGroup"/> </b>
</p>

<br/>

<table class="tstyle5 thlight thmiddle thright mbottom0">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.TeacherServiceDistribution"/>:
		</th>
		<td>
			<html:select property="tsd" onchange="this.form.method.value='loadTeacherServiceDistributionsForPermissionServices'; this.form.submit();">
				<html:options collection="tsdOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>			
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.person"/>:
		</th>
		<td>
			<html:select property="person" onchange="this.form.method.value='loadTeacherServiceDistributionsForPermissionServices'; this.form.submit();">
				<html:options collection="departmentPersonList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.permissions"/>:
		</th>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.coursesValuationPermission" property="coursesValuationManagers"/> <bean:message key="label.teacherServiceDistribution.coursesValuationPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.teachersValuationPermission" property="teachersValuationManagers"/> <bean:message key="label.teacherServiceDistribution.teachersValuationPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.coursesManagementPermission" property="coursesManagementGroup"/> <bean:message key="label.teacherServiceDistribution.coursesManagementPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.teachersManagementPermission" property="teacherManagementGroup"/> <bean:message key="label.teacherServiceDistribution.teachersManagementPermission"/><br/>
		</td>
	</tr>
</table>



<p>
	<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='addCoursesAndTeachersValuationPermissionToPerson'; this.form.submit()"><bean:message key="button.update"/></html:button>
</p>

<br>

<logic:empty name="personPermissionsDTOEntryListForTeacherServiceDistribution">
	<p class="mtop15">
		<em>
			<bean:message key="label.teacherServiceDistribution.noPersonsWithValidPermissionsInGrouping"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="personPermissionsDTOEntryListForTeacherServiceDistribution">
	<table class='tstyle4'>
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.person"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.coursesValuationPermission"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.teachersValuationPermission"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.coursesManagementPermission"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.teachersManagementPermission"/>
			</th>
		</tr>
	<logic:notPresent name="notCoursesAndTeachersValuationManagers">
		<logic:iterate name="personPermissionsDTOEntryListForTeacherServiceDistribution" id="personPermissionsDTOEntry">
			<tr>
				<td class="courses">
					<bean:write name="personPermissionsDTOEntry" property="person.name"/> 
					(<bean:write name="personPermissionsDTOEntry" property="person.mostImportantAlias"/>)
				</td>
				<td class="acenter">
					<logic:equal name="personPermissionsDTOEntry" property="coursesValuationPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
					</logic:equal>
					<logic:notEqual name="personPermissionsDTOEntry" property="coursesValuationPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
					</logic:notEqual>
				</td>
				<td class="acenter">
					<logic:equal name="personPermissionsDTOEntry" property="teachersValuationPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
					</logic:equal>
					<logic:notEqual name="personPermissionsDTOEntry" property="teachersValuationPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
					</logic:notEqual>
				</td>						
				<td class="acenter">
					<logic:equal name="personPermissionsDTOEntry" property="coursesManagementPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
					</logic:equal>
					<logic:notEqual name="personPermissionsDTOEntry" property="coursesManagementPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
					</logic:notEqual>
				</td>			
				<td class="acenter">
					<logic:equal name="personPermissionsDTOEntry" property="teachersManagementPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
					</logic:equal>
					<logic:notEqual name="personPermissionsDTOEntry" property="teachersManagementPermission" value="true">
						<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
					</logic:notEqual>
				</td>			
			</tr>
		</logic:iterate>
	</logic:notPresent>
	</table>
</logic:notEmpty>
</html:form>

<br/>

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>



