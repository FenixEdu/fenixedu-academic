<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.permissions.PhdPermission"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<br/>
<h3><bean:message key="label.phd.permissions.management" bundle="PHD_RESOURCES" /></h3>

<fr:view name="permissions">

	<fr:schema bundle="PHD_RESOURCES" type="<%= PhdPermission.class.getName() %>">
		<fr:slot name="type.localizedName"><fr:property name="classes" value="nowrap" /></fr:slot>
		<fr:slot name="elements" layout="list">
			<fr:property name="eachLayout" value="values" />
			<fr:property name="eachSchema" value="phd.show.permissions.member" />
			<fr:property name="sortBy" value="name"/>
		</fr:slot>
	</fr:schema>
		
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight mtop15" />
		<fr:property name="columnClasses" value="acenter width12em, width30em, thlight" />

		<fr:property name="linkFormat(edit)" value="/phdPermissionsManagement.do?method=prepareEditMembers&permissionOID=${externalId}" />
		<fr:property name="key(edit)" value="label.edit" />
		<fr:property name="bundle(edit)" value="PHD_RESOURCES" />
	</fr:layout>

</fr:view>

</logic:present>
