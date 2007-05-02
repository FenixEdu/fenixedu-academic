<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.notNeedToEnrol.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<fr:form action="/notNeedToEnrolEnrolments.do">
	<html:hidden property="method" value="editNotNeedToEnrol"/>
	<logic:notEmpty name="bean" property="aprovedEnrolments">
		<fr:edit name="bean" property="aprovedEnrolments" schema="notNeedToEnroll.view.aproved.enrolment">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle1 thlight"/>
				<fr:property name="sortBy" value="aprovedEnrolment.studentCurricularPlan.name,aprovedEnrolment.curricularCourse.name"/>
			</fr:layout>
		</fr:edit>
		<fr:edit id="notNeedToEnrolBean" name="bean" visible="false"/>
		<br/>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:submit onclick="this.form.method.value='back'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notEmpty>
</fr:form>
