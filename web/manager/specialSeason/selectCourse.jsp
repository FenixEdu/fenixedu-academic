<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES"
	key="label.manager.specialSeason.specialSeasonStatusTracker" /></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<fr:form id="searchForm" action="/specialSeason/specialSeasonStatusTracker.do?method=listStudents">
	<fr:edit id="bean" name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments.SpecialSeasonStatusTrackerBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="executionSemester" layout="menu-select" key="label.executionSemester" required="true">
				<fr:property name="format" value="${qualifiedName}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
			<fr:slot name="department" layout="menu-select-postback" key="label.department" required="true">
				<fr:property name="format" value="${name}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DepartmentsProvider"/>
				<fr:property name="saveOptions" value="true"/>
				<fr:property name="destination" value="postback"/>
			</fr:slot>
			<fr:slot name="competenceCourse" layout="menu-select" key="label.competenceCourse">
				<fr:property name="format" value="${name}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CompetenceCoursesForDepartmentProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:destination name="postback" path="/specialSeason/specialSeasonStatusTracker.do?method=updateDepartmentSelection" />
			<fr:property name="classes" value="tstyle5 thmiddle thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="MANAGER_RESOURCES" key="button.show"/>
	</html:submit>
</fr:form>