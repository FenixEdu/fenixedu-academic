<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.academicAdminOffice.permissions.management" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	
<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<fr:view name="permissions" schema="academicAdminOffice.show.permissions" >
		<fr:layout name="tabular" >

			<fr:property name="classes" value="tstyle4 thlight mtop15" />
			<fr:property name="columnClasses" value="acenter width12em, width30em, thlight" />
			<fr:property name="checkable" value="false" />

			<fr:property name="linkFormat(edit)" value="/permissionManagement.do?method=prepareEditMembers&permissionTypeName=${permissionType.name}" />
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES" />
		</fr:layout>
	</fr:view>

</logic:present>
