<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<fr:form action="/editMarkSheet.do">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="editMarkSheet" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" name="markSheetManagementForm" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />
	
	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.edit"/> <bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet"/></h2>
	
	<logic:messagesPresent message="true">
		<ul class="mvert15">
			<html:messages bundle="DEGREE_OFFICE_RESOURCES" id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<fr:view name="edit" schema="markSheet.view.edit">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright"/>
	        <fr:property name="columnClasses" value=",,"/>
		</fr:layout>
	</fr:view>
	
	<logic:equal name="edit" property="markSheet.markSheetState.name" value="NOT_CONFIRMED">
		<p class="mbottom05"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.edit"/>:</p>
		<fr:edit id="edit-markSheet" name="edit" schema="markSheet.edit">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight thright mvert05"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	
		<p class="mtop05">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='updateMarkSheet';"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.update" /></html:submit>
		</p>
	</logic:equal>
	
	<logic:notEqual name="edit" property="markSheet.markSheetState.name" value="NOT_CONFIRMED">
		<fr:edit id="edit-markSheet" name="edit" visible="false"/>
	</logic:notEqual>
	<logic:notEmpty name="edit" property="enrolmentEvaluationBeansToEdit">
	
		<p class="mtop2 mbottom05"><strong><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.students.with.grade" />:</strong></p>
		
		<fr:hasMessages for="edit-marksheet-enrolments">
			<ul>
			<fr:messages>
				<li><fr:message/></li>
			</fr:messages>
			</ul>
		</fr:hasMessages>
		


		<fr:edit id="edit-marksheet-enrolments" name="edit" property="enrolmentEvaluationBeansToEdit" 
				 schema="markSheet.edit.enrolmentEvaluations" layout="tabular-editable"
	             nested="true">
			<fr:layout>
				<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
				<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
		        <fr:property name="columnClasses" value=",,"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	
	<logic:notEmpty name="edit" property="enrolmentEvaluationBeansToAppend">

		<p class="mtop15 mbottom05"><strong><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.students.without.grade" />:</strong></p>
		
		<fr:hasMessages for="append-enrolments">
			<ul>
			<fr:messages>
				<li><fr:message/></li>
			</fr:messages>
			</ul>
		</fr:hasMessages>
		
		<fr:edit id="append-enrolments" name="edit" property="enrolmentEvaluationBeansToAppend" 
				 schema="markSheet.edit.enrolmentEvaluations" layout="tabular-editable"
	             nested="true">
			<fr:layout>
				<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
				<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	
	<p>	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.change" /></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backSearchMarkSheet';"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.back"/></html:cancel>
	</p>
</fr:form>