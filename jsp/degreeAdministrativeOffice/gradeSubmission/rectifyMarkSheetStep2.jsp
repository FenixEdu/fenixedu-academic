<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.util.EnrolmentEvaluationState"%>
<%@page import="net.sourceforge.fenixedu.util.DateFormatUtil"%>
<h2><bean:message key="label.rectifyMarkSheet"/></h2>

<br/>

<h3><bean:message key="label.rectifyMarkSheet.step.one"/> &gt; <u><bean:message key="label.rectifyMarkSheet.step.two"/></u></h3>

<fr:view name="rectifyBean" property="markSheet"
		schema="markSheet.view"
		layout="tabular">
</fr:view>

<h3><bean:write name="rectifyBean" property="enrolmentEvaluation.enrolment.studentCurricularPlan.student.person.name"/> (<bean:write name="rectifyBean" property="enrolmentEvaluation.enrolment.studentCurricularPlan.student.number"/>)</h3>
<html:errors/>
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<span class="error0"><bean:write name="messages" /></span>
	</html:messages>
	<br/><br/>
</logic:messagesPresent>

<fr:form action="/rectifyMarkSheet.do">
	<html:hidden name="markSheetManagementForm" property="method" value="rectifyMarkSheetStepTwo" />
	<bean:define id="evaluationID" name="rectifyBean" property="enrolmentEvaluation.idInternal" />
	<html:hidden name="markSheetManagementForm" property="evaluationID" value="<%= evaluationID.toString() %>"  />

	<fr:edit nested="true" name="rectifyBean" schema="markSheet.rectify.two" layout="tabular"/>			

	<html:submit><bean:message key="label.actual.grade"/></html:submit>
	<html:cancel onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message key="label.cancel"/></html:cancel>
</fr:form>