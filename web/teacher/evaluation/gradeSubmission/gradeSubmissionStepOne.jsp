<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<fr:form action="/markSheetManagement.do">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="gradeSubmissionStepOne" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" name="markSheetManagementForm" property="executionCourseID" />
	
	<em><bean:message key="message.evaluationElements" bundle="APPLICATION_RESOURCES"/></em>
	<h2><bean:message key="label.submit.listMarks"/></h2>
	
	<div class="infoop2">
		<bean:message key="label.submitMarks.introduction"/>
	</div>

	<p class="breadcumbs mbottom1"><span class="actual"><bean:message key="label.markSheet.gradeSubmission.step.one"/></span> &gt; <span><bean:message key="label.markSheet.gradeSubmission.step.two"/></span></p>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<fr:edit id="submissionBean"
			 name="submissionBean"
			 schema="markSheet.teacher.gradeSubmission.step.one"
			 type="net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop1"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>

	<p>
		<span class="warning0"><bean:message key="label.submitMarks.remainder"/></span>
	</p>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
	</p>
	
</fr:form>
