<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<fr:form action="/markSheetManagement.do">

	<script type="text/javascript">
	<!--
		function setCheckBoxValue(value) {
			elements = document.getElementsByTagName('input');
			for (i = 0; i < elements.length; i++) {
				if (elements[i].type == 'checkbox') {
					elements[i].checked = value;	
				}
			}
		}
	//-->
	</script>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="gradeSubmissionStepTwo" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" name="markSheetManagementForm" property="executionCourseID" />
	
	<em><bean:message key="message.evaluationElements" bundle="APPLICATION_RESOURCES"/></em>
	<h2><bean:message key="label.submit.listMarks"/></h2>

	<div class="infoop2">
		<bean:message key="label.submitMarks.introduction"/>
	</div>


	<p class="breadcumbs"><span><bean:message key="label.markSheet.gradeSubmission.step.one"/></span> &gt; <span class="actual"><bean:message key="label.markSheet.gradeSubmission.step.two"/></span></p>
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<fr:view name="submissionBean"
			 schema="markSheet.teacher.gradeSubmission.step.two.view"
			 type="net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
		</fr:layout>
	</fr:view>
	
	<logic:notEmpty name="submissionBean" property="marksToSubmit">
	
		<fr:edit id="submissionBean-invisible" name="submissionBean" visible="false"/>
		

		<div class="infoop mvert1">
			<bean:message key="label.submitMarks.instructions"/>
		</div>

		
		<p class="mvert1"><a href="javascript:setCheckBoxValue(true)"><bean:message key="button.selectAll"/></a> | <a href="javascript:setCheckBoxValue(false)"><bean:message key="button.selectNone"/></a></p>
		
		<fr:edit id="marksToSubmit" name="submissionBean" property="marksToSubmit" 
				 schema="markSheet.teacher.gradeSubmission.marksToSubmit" layout="tabular-editable">
			<fr:layout>
				<fr:property name="sortBy" value="attends.aluno.number"/>
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value="acenter,,,acenter,,,"/>
			</fr:layout>
			<fr:destination name="invalid" path="/markSheetManagement.do?method=invalid"/>
		</fr:edit>

		<p><a href="javascript:setCheckBoxValue(true)"><bean:message key="button.selectAll"/></a> | <a href="javascript:setCheckBoxValue(false)"><bean:message key="button.selectNone"/></a></p>

		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.submit"/></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backToMainPage';"><bean:message key="button.cancel"/></html:cancel>
		</p>
	</logic:notEmpty>

</fr:form>
