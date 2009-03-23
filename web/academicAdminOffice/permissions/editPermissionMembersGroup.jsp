<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.academicAdminOffice.permissions.management" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><strong><bean:write name="permission" property="permissionType.localizedName" /></strong></em>
	<br/>
	<br/>
		
	<bean:define id="permissionId" name="permission" property="externalId" />
	<fr:edit id="permissionMembers" name="permissionMembers" schema="PermissionMemberBean.edit"
		action="<%="/permissionManagement.do?method=editMembers&permissionId=" +  permissionId.toString() %>">

		<fr:layout name="tabular-editable">	
			<fr:property name="classes" value="tstyle4 thlight mtop05" />
			<fr:property name="columnClasses" value="acenter width12em,, tderror1" />
			<fr:destination name="cancel" path="/permissionManagement.do?method=showPermissions" />
		</fr:layout>

	</fr:edit>

</logic:present>
