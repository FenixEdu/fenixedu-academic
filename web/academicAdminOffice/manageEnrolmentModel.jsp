<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.manageEnrolmentModel" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<ul class="mbottom05">
	<li>
		<html:link page="/student.do?method=visualizeRegistration"
			paramId="registrationID" paramName="enrolmentModelBean"
			paramProperty="registration.idInternal">
			<bean:message key="link.student.back"
				bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</li>
</ul>


<fr:edit name="enrolmentModelBean" schema="student.manageEnrolmentModel">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
</fr:edit>

