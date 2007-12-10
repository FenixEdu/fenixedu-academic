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
	<b><bean:message key="label.teacherServiceDistribution.permissionsForTSDProcess"/></b> | 
	<html:link href="javascript:void(0)" onclick="document.forms[0].viewType.value=2; document.forms[0].method.value='loadTeacherServiceDistributionsForPermissionServices'; document.forms[0].submit();">
	<bean:message key="label.teacherServiceDistribution.permissionsForTSDGroup"/> 
	</html:link>
</p>

<br/>

<table class="tstyle5 thlight thright thmiddle mbottom0">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.person"/>:
		</th>
		<td>
			<html:select property="person" onchange="this.form.method.value='loadTeacherServiceDistributionsForPermissionServices'; this.form.submit()">
				<html:options collection="departmentPersonList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.permissions"/>:
		</th>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.phaseManagementPermission" property="phaseManagementPermission"/> <bean:message key="label.teacherServiceDistribution.phaseManagementPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.automaticValuationPermission" property="automaticValuationPermission"/> <bean:message key="label.teacherServiceDistribution.automaticValuationPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.omissionConfigurationPermission" property="omissionConfigurationPermission"/> <bean:message key="label.teacherServiceDistribution.omissionConfigurationPermission"/><br/>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.tsdCoursesAndTeachersManagementPermission" property="tsdCoursesAndTeachersManagementPermission"/> <bean:message key="label.teacherServiceDistribution.tsdCoursesAndTeachersManagementPermission"/>
		</td>
	</tr>
</table>


<p>
	<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setPermissionsToPerson'; this.form.submit()">
		<bean:message key="button.update"/>
	</html:button>
</p>

</html:form>

<br>

<logic:empty name="personPermissionsDTOEntryList">
	<p class="mtop15">
		<em>
			<bean:message key="label.teacherServiceDistribution.noPersonsWithValidPermissions"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="personPermissionsDTOEntryList">
	<table class='tstyle4'>
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
				<bean:message key="label.teacherServiceDistribution.tsdCoursesAndTeachersManagementPermission"/>
			</th>
		</tr>
	<logic:iterate name="personPermissionsDTOEntryList" id="personPermissionsDTOEntry">
		<tr>
			<td>
				<bean:write name="personPermissionsDTOEntry" property="person.name"/>
				(<bean:write name="personPermissionsDTOEntry" property="person.mostImportantAlias"/>)
			</td>
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="phaseManagementPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="phaseManagementPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
				</logic:notEqual>
			</td>			
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="automaticValuationPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="automaticValuationPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
				</logic:notEqual>
			</td>			
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="omissionConfigurationPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="omissionConfigurationPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
				</logic:notEqual>
			</td>			
			<td class="acenter">
				<logic:equal name="personPermissionsDTOEntry" property="competenceCoursesAndTeachersManagementPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/>
				</logic:equal>
				<logic:notEqual name="personPermissionsDTOEntry" property="competenceCoursesAndTeachersManagementPermission" value="true">
					<html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/>
				</logic:notEqual>
			</td>			
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<br>

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>


