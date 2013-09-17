<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<br />
<h2><bean:message key="label.externalUnits.createExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="unitId">&oid=<bean:write name="createExternalCurricularCourseBean" property="parentUnit.externalId" /></bean:define>

<bean:define id="schemaName" value="" />
<logic:equal name="createExternalCurricularCourseBean" property="enrolStudent" value="true">
	<bean:define id="schemaName" value="CreateExternalCurricularCourseBean.edit-withExternalEnrolmentBean" />
</logic:equal>
<logic:equal name="createExternalCurricularCourseBean" property="enrolStudent" value="false">
	<bean:define id="schemaName" value="CreateExternalCurricularCourseBean.edit" />
</logic:equal>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<fr:edit id="createExternalCurricularCourseBean" 
		 name="createExternalCurricularCourseBean"
		 schema="<%= schemaName %>"
		 action="/externalUnits.do?method=createExternalCurricularCourse">
		 
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="postback" path="/externalUnits.do?method=createExternalCurricularCoursePostback" />
	<fr:destination name="invalid"  path="/externalUnits.do?method=createExternalCurricularCourseInvalid"/>
	<fr:destination name="cancel"   path="<%= "/externalUnits.do?method=viewUnit" + unitId %>" />
</fr:edit>
