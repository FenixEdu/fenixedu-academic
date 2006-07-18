<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<fr:form action="/markSheetManagement.do">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="gradeSubmissionStepOne" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" name="markSheetManagementForm" property="executionCourseID" />
	
	<h2><bean:message key="label.submit.listMarks"/></h2>
	<table class="infoop"><tr><td><bean:message key="label.submitMarks.introduction"/></td></tr></table>
	<br/>
	<h3><u><bean:message key="label.markSheet.gradeSubmission.step.one"/></u> &gt; <bean:message key="label.markSheet.gradeSubmission.step.two"/></h3>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br/><br/>
	</logic:messagesPresent>

	<fr:edit id="submissionBean"
			 name="submissionBean"
			 schema="markSheet.teacher.gradeSubmission.step.one"
			 type="net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	    	<fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
	
</fr:form>
