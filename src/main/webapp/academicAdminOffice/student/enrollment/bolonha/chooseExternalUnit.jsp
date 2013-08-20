<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mvert2">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<h3><bean:message key="label.student.enrollment.choose.externalUnit" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<bean:define id="registrationId" name="registration" property="externalId" />
<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />
<logic:notEmpty name="parameters">
	<bean:define id="parameters">&amp;<bean:write name="parameters"/></bean:define>
</logic:notEmpty>

<fr:view name="unit" property="sortedExternalChilds">
    <fr:layout name="tree">
        <fr:property name="eachLayout" value="values"/>
        <fr:property name="schemaFor(Unit)" value="Unit.name.tree.view"/>
        <fr:property name="childrenFor(Unit)" value="sortedExternalChilds"/>
        <fr:property name="expandable" value="true"/>
    </fr:layout>
    <fr:destination name="choose.ExternalCurricularCourses" path="<%= contextInformation.toString() + "method=chooseExternalCurricularCourses&amp;registrationId=" + registrationId + parameters + "&amp;externalUnitId=${externalId}" %>"/>
</fr:view>

<fr:form action="<%= contextInformation.toString() + "method=backToMainPage" + parameters %>">
	<html:hidden property="registrationId" value="<%= registrationId.toString() %>"/>
	<br/>
	<html:cancel><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>
