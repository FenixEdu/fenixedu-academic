<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.send.mail"/> / <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.send.mail.search.criteria"/></strong></h2>

<fr:form action="/markSheetSendMail.do?method=searchSendMail">
	<fr:edit id="search"
			 name="bean"
			 type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSendMailBean"
			 schema="markSheet.search.send.mail">
		<fr:destination name="postBack" path="/markSheetSendMail.do?method=prepareSearchSendMailPostBack"/>
		<fr:destination name="invalid" path="/markSheetSendMail.do?method=prepareSearchSendMailInvalid"/>
		<fr:destination name="cancel" path="/markSheetSendMail.do?method=prepareSearchSendMail" />
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight thright"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.search"/></html:submit>
</fr:form>


<logic:present name="bean" property="markSheetToConfirmSendMailBean">
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheets.to.confirm"/>:</strong></p>
	<fr:form action="<%="/markSheetSendMail.do?method=prepareMarkSheetsToConfirmSendMail&" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/messaging")%>">
		<fr:edit id="sendMailBean" name="bean" visible="false"/>
		<fr:edit id="markSheetsToSubmit" name="bean" property="markSheetToConfirmSendMailBean" 
				 schema="markSheet.send.mail.choose.markSheets" layout="tabular-editable">
			<fr:layout>
				<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			    <fr:property name="columnClasses" value=",,"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.write.mail"/></html:submit>
	</fr:form>
</logic:present>


<logic:present name="bean" property="gradesToSubmitExecutionCourseSendMailBean">
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheets.executionCourse.grades.to.submit"/>:</strong></p>
	<fr:form action="<%="/markSheetSendMail.do?method=prepareGradesToSubmitSendMail&" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/messaging")%>">
		<fr:edit id="sendMailBean" name="bean" visible="false"/>
		<fr:edit id="markSheetsToSubmit" name="bean" property="gradesToSubmitExecutionCourseSendMailBean" 
				 schema="markSheet.send.mail.choose.executionCourses" layout="tabular-editable">
			<fr:layout>
				<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			    <fr:property name="columnClasses" value="acenter,,acenter,acenter"/>
			    
			    <fr:property name="sortBy" value="executionCourse.name" />
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.write.mail"/></html:submit>
	</fr:form>
</logic:present>


