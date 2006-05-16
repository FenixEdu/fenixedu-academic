<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.createMarkSheet"/></h2>

<br/>

<h3><bean:message key="label.createMarkSheet.step.one"/> &gt; <u><bean:message key="label.createMarkSheet.step.two"/></u></h3>

<fr:view name="edit" 
		schema="markSheet.view.step2"
		layout="tabular">
</fr:view>

<br/>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
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

<fr:form action="/createMarkSheet.do?method=createMarkSheetStepTwo">

	<fr:edit id="edit-invisible" name="edit" visible="false"/>

	<fr:edit id="edit-enrolments" name="edit" property="enrolmentEvaluationBeans" 
			 schema="markSheet.create.step.two" layout="tabular-editable">
		<fr:layout>
			<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
		</fr:layout>
		<fr:destination name="invalid" path="/createMarkSheet.do?method=createMarkSheetStepTwoInvalid"/>
	</fr:edit>
	<br/>
	<html:submit><bean:message key="label.markSheet.submit" /></html:submit>
	<html:cancel><bean:message key="label.back"/></html:cancel>
</fr:form>