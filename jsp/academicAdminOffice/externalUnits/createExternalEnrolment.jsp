<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.createExternalEnrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="externalCurricularCourseId">&oid=<bean:write name="externalEnrolmentBean" property="externalCurricularCourse.idInternal" /></bean:define>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	<br/>
</html:messages>

<fr:edit id="createExternalEnrolmentBean" 
		 name="externalEnrolmentBean"
		 schema="CreateExternalEnrolmentBean.edit"
		 action="/externalUnits.do?method=createExternalEnrolment">
		 
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4"/>
	</fr:layout>
	
	<fr:destination name="cancel" path="<%= "/externalUnits.do?method=viewExternalCurricularCourse" + externalCurricularCourseId %>" />
</fr:edit>
</logic:present>
