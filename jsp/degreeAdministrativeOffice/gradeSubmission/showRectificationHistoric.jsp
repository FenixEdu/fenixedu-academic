<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="label.markSheet.rectificationHistoric"/></h2>

<fr:view name="enrolmentEvaluation" property="markSheet"
		schema="markSheet.view"
		layout="tabular">
</fr:view>

<h3><bean:write name="enrolmentEvaluation" property="enrolment.studentCurricularPlan.student.person.name"/> (<bean:write name="enrolmentEvaluation" property="enrolment.studentCurricularPlan.student.number"/>)</h3>

<html:errors/>
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<span class="error0"><bean:write name="messages" /></span>
	</html:messages>
	<br/><br/>
</logic:messagesPresent>

<logic:present name="enrolmentEvaluation">
	<table>
	  <tr>
	    <td><bean:message key="label.markSheet.original.grade"/></td>
	    <td><bean:write name="enrolmentEvaluation" property="grade"/> (<dt:format pattern="dd/MM/yyyy"><bean:write name="enrolmentEvaluation" property="examDate.time"/></dt:format>)</td>
	  </tr>
	  <logic:iterate id="evaluation" name="rectificationEvaluations">
		  <tr>
		    <td><bean:message key="label.markSheet.rectification"/></td>
		    <td>
		    	<bean:write name="evaluation" property="grade"/> (<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="examDate.time"/></dt:format>)<br/>
  			    	<logic:notEmpty name="evaluation" property="markSheet.reason">
	 				<bean:message key="label.markSheet.reason"/>:
			    	<bean:write name="evaluation" property="markSheet.reason"/>
		    	</logic:notEmpty>
		    </td>
		  </tr>  
	  </logic:iterate>
	</table>
</logic:present>

<%-- 

	BACK BUTTON

<fr:form action="/rectifyMarkSheet.do">
	<html:hidden name="markSheetManagementForm" property="method" value="rectifyMarkSheetStepTwo" />
	<bean:define id="evaluationID" name="rectifyBean" property="enrolmentEvaluation.idInternal" />
	<html:hidden name="markSheetManagementForm" property="evaluationID" value="<%= evaluationID.toString() %>"  />

	<fr:edit id="step2" nested="true" name="rectifyBean" schema="markSheet.rectify.two" layout="tabular"/>			

	<br/>
	<span class="warning0"><bean:message key="message.markSheet.rectify"/></span>
	<br/><br/>
	<html:submit styleClass="inputbutton"><bean:message key="label.rectify"/></html:submit>
	<html:cancel styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message key="label.cancel"/></html:cancel>
</fr:form>

--%>