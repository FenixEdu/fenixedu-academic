<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.permissions.PhdPermission"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdPermissionsManagementDA"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.permissions.to" bundle="PHD_RESOURCES" /> '<bean:write name="permission" property="type.localizedName" />'</h2>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<h3><bean:message key="label.phd.permissions.edit.elements" bundle="PHD_RESOURCES" /></h3>

<bean:define id="permissionOID" name="permission" property="externalId" />

<fr:edit id="elements" name="elements" action="<%= "/phdPermissionsManagement.do?method=saveMembers&permissionOID=" + permissionOID %>">

	<fr:schema bundle="PHD_RESOURCES" type="<%= PhdPermissionsManagementDA.PhdElementBean.class.getName() %>">
		<fr:slot name="selected" />
		<fr:slot name="person" layout="format" readOnly="true">
			<fr:property name="format" value="${name} (F${employee.employeeNumber})" />
		</fr:slot>
	</fr:schema>

	<fr:layout name="tabular-editable" >
		<fr:property name="classes" value="tstyle4 thlight mtop15" />
		<fr:property name="columnClasses" value="acenter width12em, width30em, thlight" />
	</fr:layout>

	<fr:destination name="cancel" path="/phdPermissionsManagement.do?method=showPermissions"/>
</fr:edit>

</logic:present>
