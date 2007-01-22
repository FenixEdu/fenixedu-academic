<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.department" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="unitResultBean" property="unit.name"/> </h2>

<fr:view name="unitResultBean" schema="AbstractExternalUnitResultBean.view-breadCrumbs-path">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true" />
	</fr:layout>
</fr:view>

<br/>
<bean:define id="unitId">&amp;oid=<bean:write name="unitResultBean" property="unit.idInternal"/></bean:define>
<ul>
	<li><html:link page="<%="/externalUnits.do?method=prepareCreateExternalCurricularCourse" + unitId %>"><bean:message key="label.externalUnits.createExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
</ul>
<ul>
	<li><bean:message key="label.externalUnits.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>:<bean:size id="size" name="externalCurricularCourses" /> <%= size %></li>
</ul>

<h3><bean:message key="label.externalUnits.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</h3>
<logic:notEmpty name="externalCurricularCourses">
	<fr:view name="externalCurricularCourses" schema="ExternalCurricularCourseResultBean.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="externalCurricularCourses">
	<em><bean:message key="label.externalUnits.noExternalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
</logic:empty>

</logic:present>
