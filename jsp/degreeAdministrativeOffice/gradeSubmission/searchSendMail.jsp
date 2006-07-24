<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="label.send.mail"/> / <bean:message key="label.send.mail.search.criteria"/></strong></h2>

<fr:form action="/markSheetSendMail.do?method=searchSendMail">
	<fr:edit id="search"
			 name="bean"
			 type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSendMailBean"
			 schema="markSheet.search.send.mail">
		<fr:destination name="postBack" path="/markSheetSendMail.do?method=prepareSearchSendMailPostBack"/>
		<fr:destination name="invalid" path="/markSheetSendMail.do?method=prepareSearchSendMailInvalid"/>
		<fr:destination name="cancel" path="/markSheetSendMail.do?method=prepareSearchSendMail" />
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="label.search"/></html:submit>
</fr:form>

<br/>
<br/>

<logic:present name="bean" property="markSheetToConfirmSendMailBean">
	<p><h3><bean:message key="label.markSheets.to.confirm"/></h3></p>
	<fr:form action="/markSheetSendMail.do?method=prepareMarkSheetsToConfirmSendMail">
		<fr:edit id="sendMailBean" name="bean" visible="false"/>
		<fr:edit id="markSheetsToSubmit" name="bean" property="markSheetToConfirmSendMailBean" 
				 schema="markSheet.send.mail.choose.markSheets" layout="tabular-editable">
			<fr:layout>
				<fr:property name="classes" value="tstyle4"/>
			    <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="label.write.mail"/></html:submit>
	</fr:form>
</logic:present>

<br/>
<br/>
<logic:present name="bean" property="gradesToSubmitExecutionCourseSendMailBean">
	<p><h3><bean:message key="label.markSheets.executionCourse.grades.to.submit"/></h3></p>
	<fr:form action="/markSheetSendMail.do?method=prepareGradesToSubmitSendMail">
		<fr:edit id="sendMailBean" name="bean" visible="false"/>
		<fr:edit id="markSheetsToSubmit" name="bean" property="gradesToSubmitExecutionCourseSendMailBean" 
				 schema="markSheet.send.mail.choose.executionCourses" layout="tabular-editable">
			<fr:layout>
				<fr:property name="classes" value="tstyle4"/>
			    <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="label.write.mail"/></html:submit>
	</fr:form>
</logic:present>


