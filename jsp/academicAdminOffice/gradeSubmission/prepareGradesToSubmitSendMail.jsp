<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.send.mail"/> / <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheets.executionCourse.grades.to.submit"/></strong></h2>

<fr:form action="/markSheetSendMail.do?method=gradesToSubmitSendMail">
	<fr:edit id="mail" name="bean"
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSendMailBean"
		schema="markSheet.send.mail">
		<fr:layout>
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value="listClasses,," />
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.send.mail"/></html:submit>
</fr:form>
<br/>
<br/>