<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<h2><bean:message key="link.students.search" /></h2>

	<hr />
	<br />

	<fr:form action="/searchStudents.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>

		<fr:edit id="searchStudentsWithEnrolmentsByDepartment" name="searchStudentsWithEnrolmentsByDepartment"
				schema="net.sourceforge.fenixedu.domain.student.SearchStudentsWithEnrolmentsByDepartment" >
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>
		</fr:edit>

		<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
	</fr:form>

</logic:present>
