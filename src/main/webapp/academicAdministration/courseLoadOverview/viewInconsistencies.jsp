<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.courseLoadOverview.viewInconsistencies"/>
</h2>

<fr:form action="/courseLoadOverview.do?method=viewInconsistencies">
	<fr:edit id="courseLoadOverviewBean" name="courseLoadOverviewBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.CourseLoadOverviewBean" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="executionSemester" layout="menu-select-postback" key="label.semester" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.ExecutionSemesterProvider" />
					<fr:property name="destination" value="viewInconsistencies" />
					<fr:property name="format" value="${name} - ${executionYear.name}" />
				</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear" />
		</fr:layout>
	</fr:edit>
</fr:form>

<p>
	<html:link action="/courseLoadOverview.do?method=downloadInconsistencies" paramId="executionSemesterOid" paramName="courseLoadOverviewBean" paramProperty="executionSemester.externalId">
		<bean:message key="link.downloadInconsistencies" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>
