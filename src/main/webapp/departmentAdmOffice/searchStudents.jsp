<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<h2><bean:message key="link.students.search" /></h2>

	<hr />
	<br />

	<fr:form action="/searchStudents.do">
		<fr:edit id="searchStudentsWithEnrolmentsByDepartment" name="searchStudentsWithEnrolmentsByDepartment"
				schema="net.sourceforge.fenixedu.domain.student.SearchStudentsWithEnrolmentsByDepartment">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>
		</fr:edit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" value="search"><bean:message key="button.submit"/></html:submit>

		<bean:define id="studentCurricularPlans" name="searchStudentsWithEnrolmentsByDepartment" property="search"/>
		<logic:notEmpty name="studentCurricularPlans">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" value="download"><bean:message key="button.download"/></html:submit>

			<fr:view name="studentCurricularPlans"
					schema="net.sourceforge.fenixedu.domain.StudentCurricularPlan.List">
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,,,"/>
		    	    <fr:property name="sortBy" value="degreeCurricularPlan.degree.sigla=asc,registration.student.number=asc"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

	</fr:form>
</logic:present>
