<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="title.teaching" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="label.version.manage" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<fr:view name="departments" schema="view.departments.with.requests">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
		<fr:property name="columnClasses" value=",acenter,acenter"/>
		<fr:property name="sortBy" value="name"/>
	</fr:layout>
	<fr:destination name="viewDepartmentRequests" path="/competenceCourses/manageVersions.do?method=displayRequest&departmentID=${externalId}"/>
</fr:view>

