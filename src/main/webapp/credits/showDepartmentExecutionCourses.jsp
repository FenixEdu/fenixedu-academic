<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<html:messages id="message" message="true">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>


<logic:present name="departmentCreditsBean">

	<fr:form action="/projectTutorialCourses.do?method=showDepartmentExecutionCourses">
		<fr:edit id="departmentCreditsBean" name="departmentCreditsBean">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean">
				<fr:slot name="department" key="label.department" layout="menu-select">
					<fr:property name="from" value="availableDepartments"/>
					<fr:property name="format" value="${name}"/>
				</fr:slot>
				<fr:slot name="executionSemester" key="label.execution-period" layout="menu-select" required="true">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider" />
					<fr:property name="format" value="${executionYear.year} - ${semester}º semestre" />
					<fr:property name="nullOptionHidden" value="true" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="cancel" path="/exportCredits.do?method=exportDepartmentCourses" />
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible"><bean:message key="label.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.export" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</fr:form>
	<logic:notEmpty name="departmentCreditsBean" property="departmentExecutionCourses">
		<bean:define id="departmentOid" name="departmentCreditsBean" property="department.externalId"/>
		<fr:view name="departmentCreditsBean" property="departmentExecutionCourses">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
				<fr:slot name="name" key="label.course"/>
				<fr:slot name="degreePresentationString" key="label.degrees"/>
				<fr:slot name="projectTutorialCourse" key="label.projectTutorialCourseType"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				<fr:property name="link(change)" value="<%="/projectTutorialCourses.do?method=changeExecutionCourseType&departmentOid="+departmentOid%>" />
				<fr:property name="key(change)" value="label.changeType" />
				<fr:property name="param(change)" value="externalId/executionCourseOid" />
				<fr:property name="bundle(change)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
				<fr:property name="visibleIf(change)" value="executionPeriod.executionYear.current" />
				<fr:property name="confirmationKey(change)" value="message.confirmation.changeExecutionCourseType" />
				<fr:property name="confirmationBundle(change)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
				<fr:property name="confirmationTitleKey(change)" value="label.changeType" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>