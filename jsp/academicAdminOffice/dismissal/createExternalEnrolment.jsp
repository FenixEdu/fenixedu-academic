<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.createExternalEnrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	<br/>
</html:messages>

<fr:view name="studentCurricularPlan" schema="StudentCurricularPlan.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
<br/>

<bean:define id="scpID">&scpID=<bean:write name="studentCurricularPlan" property="idInternal" /></bean:define>

<fr:edit id="createExternalEnrolmentBean" 
		 name="externalEnrolmentBean"
		 schema="CreateExternalEnrolmentBean.edit-without-student"
		 action="<%="/studentDismissals.do?method=createExternalEnrolment" + scpID %>">
		 
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4"/>
	</fr:layout>

	<fr:destination name="cancel" path="<%= "/studentDismissals.do?method=manage" + scpID %>" />
</fr:edit>
</logic:present>
