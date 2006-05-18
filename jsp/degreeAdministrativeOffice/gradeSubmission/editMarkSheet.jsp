<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<fr:form action="/editMarkSheet.do">

	<html:hidden name="markSheetManagementForm" property="method" value="editMarkSheet" />
	<html:hidden name="markSheetManagementForm" property="epID" />
	<html:hidden name="markSheetManagementForm" property="dID" />
	<html:hidden name="markSheetManagementForm" property="dcpID" />
	<html:hidden name="markSheetManagementForm" property="ccID"  />	
	<html:hidden name="markSheetManagementForm" property="msID" />
	<html:hidden name="markSheetManagementForm" property="tn" />
	<html:hidden name="markSheetManagementForm" property="ed"/>
	<html:hidden name="markSheetManagementForm" property="mss" />
	<html:hidden name="markSheetManagementForm" property="mst" />
	
	<h2><bean:message key="label.markSheet.edit" /></h2>
	<br/>
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br/>
	</logic:messagesPresent>
	
	<fr:view name="edit" schema="markSheet.view.edit" layout="tabular" />
	<br/>
	<br/>
	
	<fr:edit id="edit-markSheet" name="edit" schema="markSheet.edit" layout="tabular" nested="true"/>
	<html:submit onclick="this.form.method.value='updateMarkSheet';"><bean:message key="label.markSheet.update" /></html:submit>
	<br/>
	
	<logic:notEmpty name="edit" property="enrolmentEvaluationBeansToEdit">
		<br/><br/>
		<bean:message key="label.markSheet.students.with.grade" />:
		<br/>

		<fr:edit id="edit-marksheet-enrolments" name="edit" property="enrolmentEvaluationBeansToEdit" 
				 schema="markSheet.edit.enrolmentEvaluations" layout="tabular-editable"
	             nested="true">
			<fr:layout>
				<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	
	<logic:notEmpty name="edit" property="enrolmentEvaluationBeansToAppend">
		<br/><br/>
		<bean:message key="label.markSheet.students.without.grade" />:
		<br/>
		<fr:edit id="append-enrolments" name="edit" property="enrolmentEvaluationBeansToAppend" 
				 schema="markSheet.edit.enrolmentEvaluations" layout="tabular-editable"
	             nested="true">
			<fr:layout>
				<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	<br/>
	
	<html:submit><bean:message key="label.markSheet.change" /></html:submit>
	<html:cancel onclick="this.form.method.value='backSearchMarkSheet';"><bean:message key="label.back"/></html:cancel>
</fr:form>