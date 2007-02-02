<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.createExternalEnrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mvert2">
<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="studentCurricularPlan" property="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<h3><bean:message key="label.studentDismissal.chooseExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

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
