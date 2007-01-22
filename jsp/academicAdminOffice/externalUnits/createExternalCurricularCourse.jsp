<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.createExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="unitId">&oid=<bean:write name="createExternalCurricularCourseBean" property="parentUnit.idInternal" /></bean:define>

<bean:define id="schemaName" value="" />
<logic:equal name="createExternalCurricularCourseBean" property="enrolStudent" value="true">
	<bean:define id="schemaName" value="CreateExternalCurricularCourseBean.edit-withExternalEnrolmentBean" />
</logic:equal>
<logic:equal name="createExternalCurricularCourseBean" property="enrolStudent" value="false">
	<bean:define id="schemaName" value="CreateExternalCurricularCourseBean.edit" />
</logic:equal>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	<br/>
</html:messages>

<fr:edit id="createExternalCurricularCourseBean" 
		 name="createExternalCurricularCourseBean"
		 schema="<%= schemaName %>"
		 action="/externalUnits.do?method=createExternalCurricularCourse">
		 
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4"/>
	</fr:layout>
	<fr:destination name="postback" path="/externalUnits.do?method=createExternalCurricularCoursePostback" />
	<fr:destination name="invalid"  path="/externalUnits.do?method=createExternalCurricularCourseInvalid"/>
	<fr:destination name="cancel"   path="<%= "/externalUnits.do?method=viewUnit" + unitId %>" />
</fr:edit>

</logic:present>