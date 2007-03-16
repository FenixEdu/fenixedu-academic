<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mvert2">
<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<h3><bean:message key="label.student.enrollment.choose.externalUnit" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<bean:define id="studentId" name="student" property="idInternal" />
<bean:define id="contextInformation" name="contextInformation" />

<fr:view name="unit" property="sortedExternalChilds">
    <fr:layout name="tree">
        <fr:property name="eachLayout" value="values"/>
        <fr:property name="schemaFor(Unit)" value="Unit.name.tree.view"/>
        <fr:property name="childrenFor(Unit)" value="sortedExternalChilds"/>
        <fr:property name="expandable" value="true"/>
    </fr:layout>
    <fr:destination name="choose.ExternalCurricularCourses" path="<%= contextInformation.toString() + ".do?method=chooseExternalCurricularCourses&amp;studentId=" + studentId + "&amp;externalUnitId=${idInternal}" %>"/>
</fr:view>

<fr:form action="/studentExternalEnrolments.do?method=backToMainPage">
	<html:hidden property="studentId" value="<%= studentId.toString() %>"/>
	<br/>
	<html:cancel><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>
