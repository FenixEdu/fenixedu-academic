<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<h3 class="mtop15 mbottom025"><bean:message key="label.academicAdminOffice"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	
	<br/>
	<em><bean:message
		key="label.academicAdminOffice.permissions.management"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	
	<br/>
	<bean:message key="campus" bundle="ACADEMIC_OFFICE_RESOURCES" />
	<bean:write name="workingCampus" property="name"/>
	
	<fr:view name="permissions"
		schema="academicAdminOffice.show.permissions" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight mtop15" />
			<fr:property name="columnClasses" value="acenter width12em, width30em, thlight" />
			<fr:property name="checkable" value="false" />
			<fr:property name="linkFormat(Editar)"
				value="/permissionManagement.do?method=prepareEditMembers&permissionTypeName=${permissionType.name}" />
		</fr:layout>
	</fr:view>

</logic:present>