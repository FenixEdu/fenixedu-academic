<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.create" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message name="createUnitBean" property="unitType.name" bundle="ENUMERATION_RESOURCES" /></h2>

<bean:define id="unitId">&oid=<bean:write name="createUnitBean" property="parentUnit.idInternal" /></bean:define>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<fr:edit id="createUnitBean" name="createUnitBean" 
		 schema="CreateExternalUnitBean.edit"
		 action="/externalUnits.do?method=createExternalUnit">
	
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	
	<fr:destination name="cancel" path="<%= "/externalUnits.do?method=viewUnit" + unitId %>" />
</fr:edit>

</logic:present>
