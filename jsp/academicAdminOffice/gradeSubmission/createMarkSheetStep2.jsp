<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.createMarkSheet"/></h2>

<br/>

<h3><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.createMarkSheet.step.one"/> &gt; <u><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.createMarkSheet.step.two"/></u></h3>

<fr:view name="edit" 
		schema="markSheet.view.step2">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
</fr:view>

<br/><br/>

<logic:messagesPresent message="true">
	<ul>
	<html:messages bundle="DEGREE_OFFICE_RESOURCES" id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
</logic:messagesPresent>

<fr:hasMessages for="edit-enrolments">
	<ul>
	<fr:messages>
		<li><fr:message/></li>
	</fr:messages>
	</ul>
</fr:hasMessages>

<fr:form action="/createMarkSheet.do">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="createMarkSheetStepTwo" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" name="markSheetManagementForm" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />

	<fr:edit id="edit-invisible" name="edit" visible="false"/>

	<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.students.capitalized"/>:
	<fr:edit id="edit-enrolments" name="edit" property="enrolmentEvaluationBeans" 
			 schema="markSheet.create.step.two" layout="tabular-editable">
		<fr:layout>
			<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
		<fr:destination name="invalid" path="/createMarkSheet.do?method=createMarkSheetStepTwoInvalid"/>
	</fr:edit>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.submit" /></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backSearchMarkSheet';"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="button.cancel"/></html:cancel>
</fr:form>