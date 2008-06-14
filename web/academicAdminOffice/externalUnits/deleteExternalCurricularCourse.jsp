<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.delete" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="externalCurricularCourse" property="name" /></h2>

<bean:define id="eccId">oid=<bean:write name="externalCurricularCourse" property="idInternal" /></bean:define>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<html:form action="<%= "/externalUnits.do?" + eccId.toString() %>">
	<html:hidden property="method" value="deleteExternalCurricularCourse" />
	<strong><bean:message key="label.externalUnits.confirmDeleteExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/>?</strong>
	<br/>
	<br/>
	<html:submit><bean:message key="label.yes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='viewExternalCurricularCourse';"><bean:message key="label.no" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</html:form>

</logic:present>
