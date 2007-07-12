<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="title.teaching" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="label.version.manage" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<fr:view name="departments" schema="view.departments.with.requests">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
		<fr:property name="columnClasses" value=",acenter,acenter"/>
		<fr:property name="sortBy" value="name"/>
	</fr:layout>
	<fr:destination name="viewDepartmentRequests" path="/competenceCourses/manageVersions.do?method=displayRequest&departmentID=${idInternal}"/>
</fr:view>

