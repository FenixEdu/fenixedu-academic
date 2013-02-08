<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<html:messages id="message" message="true">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>


<logic:present name="departmentCreditsBean">

	<fr:form action="/managePersonFunctionsShared.do?method=showDepartmentPersonFunctions">
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
			<fr:destination name="cancel" path="/exportCredits.do?method=exportDepartmentPersonFunctions" />
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible"><bean:message key="label.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.export" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</fr:form>
	<logic:notEmpty name="departmentCreditsBean" property="departmentPersonFunctions">
		<bean:define id="departmentOid" name="departmentCreditsBean" property="department.externalId"/>
		<fr:view name="departmentCreditsBean" property="departmentPersonFunctions">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction">
				<fr:slot name="person.username" key="label.teacher.id"/>
				<fr:slot name="person.name" key="label.name"/>
				<fr:slot name="function.name" key="label.managementPosition.position"/>
				<fr:slot name="function.unit.presentationName" key="label.managementPosition.unit" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>