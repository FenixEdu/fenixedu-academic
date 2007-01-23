<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.studentDismissal.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
	<br/>
</html:messages>

<fr:view name="studentCurricularPlan" schema="StudentCurricularPlan.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<h2><bean:message key="label.studentDismissal.chooseExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="scpID" name="studentCurricularPlan" property="idInternal" />
<fr:view name="unit" property="sortedExternalChilds">
    <fr:layout name="tree">
        <fr:property name="eachLayout" value="values"/>
        
        <fr:property name="schemaFor(Unit)" value="Unit.name"/>
        <fr:property name="childrenFor(Unit)" value="sortedExternalChilds"/>
        
        <fr:property name="schemaFor(ExternalCurricularCourse)" value="ExternalCurricularCourse.tree.view"/>
        
        <fr:property name="expandable" value="true"/>
    </fr:layout>
    
    <fr:destination name="externalCurricularCourse.choose" path="<%= "/studentDismissals.do?method=prepareCreateExternalEnrolment&amp;scpID=" + scpID + "&amp;oid=${idInternal}" %>"/>
</fr:view>
