<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.rectifyMarkSheet"/></h2>

<br/>

<h3><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.rectifyMarkSheet.step.one"/> &gt; <u><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.rectifyMarkSheet.step.two"/></u></h3>

<fr:view name="rectifyBean" property="markSheet" schema="degreeAdministrativeOffice.markSheet.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
	    <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h3><bean:write name="rectifyBean" property="enrolmentEvaluation.enrolment.studentCurricularPlan.student.person.name"/> (<bean:write name="rectifyBean" property="enrolmentEvaluation.enrolment.studentCurricularPlan.student.number"/>)</h3>
<html:errors/>
<logic:messagesPresent message="true">
	<html:messages bundle="DEGREE_OFFICE_RESOURCES" id="messages" message="true">
		<span class="error0"><bean:write name="messages" /></span>
	</html:messages>
	<br/><br/>
</logic:messagesPresent>

<fr:form action="/rectifyMarkSheet.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="rectifyMarkSheetStepTwo" />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" name="markSheetManagementForm" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />
	
	<bean:define id="evaluationID" name="rectifyBean" property="enrolmentEvaluation.idInternal" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationID" name="markSheetManagementForm" property="evaluationID" value="<%= evaluationID.toString() %>"  />

	<fr:edit id="step2" nested="true" name="rectifyBean" schema="markSheet.rectify.two">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
		    <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>

	<br/>
	<span class="warning0"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="message.markSheet.rectify"/></span>
	<br/><br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.rectify"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.cancel"/></html:cancel>
</fr:form>