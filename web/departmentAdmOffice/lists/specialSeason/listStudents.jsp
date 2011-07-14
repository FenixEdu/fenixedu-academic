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

<fr:form id="beanForm" action="/specialSeason/specialSeasonStatusTracker.do?method=exportXLS">
	<fr:edit id="bean" name="bean" visible="false"/>
</fr:form>
<p class="mtop15 mbottom1">
	<a href="javascript:var form = document.getElementById('beanForm');form.method.value='exportXLS';form.submit()">
		<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
		<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</a>
</p>

<fr:view name="bean" property="entries">
	<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments.SpecialSeasonStatusTrackerRegisterBean" bundle="MANAGER_RESOURCES">
		<fr:slot name="studentNumber" key="specialSeason.label.studentNumber"/>
		<fr:slot name="studentName" key="specialSeason.label.studentName"/>
		<fr:slot name="degreeSigla" key="specialSeason.label.degreeSigla"/>
		<fr:slot name="courseName" key="specialSeason.label.courseName"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thleft" />
		<fr:property name="columnClasses" value=",,,,,,tdclear tderror1" />
	</fr:layout>		
</fr:view>
