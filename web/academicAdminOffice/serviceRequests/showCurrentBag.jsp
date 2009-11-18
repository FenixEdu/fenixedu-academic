<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></h2>

<html:messages id="messages" message="true">
    <p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<h3><fr:view name="bag" property="date" /></h3>

<fr:view name="bag" property="registryCodeSet">
	<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
		type="net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode">
		<fr:slot name="code" />
		<fr:slot name="document" layout="link"/>
	</fr:schema>
	<fr:layout name="tabular" />
</fr:view>