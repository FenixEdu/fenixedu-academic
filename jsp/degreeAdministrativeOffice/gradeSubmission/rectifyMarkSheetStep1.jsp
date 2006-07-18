<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<%@page import="net.sourceforge.fenixedu.util.EnrolmentEvaluationState"%>
<h2><bean:message key="label.rectifyMarkSheet"/></h2>

<br/>

<h3><u><bean:message key="label.rectifyMarkSheet.step.one"/></u> &gt; <bean:message key="label.rectifyMarkSheet.step.two"/></h3>

<fr:view name="rectifyBean" property="markSheet" schema="degreeAdministrativeOffice.markSheet.view">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
<br/>

<fr:form action="/rectifyMarkSheet.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="rectifyMarkSheetStepOneByStudentNumber" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" name="markSheetManagementForm" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />

	<strong><bean:message key="label.rectifyMarkSheet.chooseStudent"/></strong><br/><br/>
	a) <bean:message key="label.rectifyMarkSheet.chooseStudent.studentNumber"/><br/><br/>


	<fr:hasMessages for="step1">
		<fr:messages>
			<span class="error0"><fr:message/></span>
		</fr:messages>
	</fr:hasMessages>

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true">
			<span class="error0"><bean:write name="messages" /></span>
		</html:messages>
	</logic:messagesPresent>
	<table>
		<tr>
			<td>
				<fr:edit id="step1" nested="true" name="rectifyBean" schema="markSheet.rectify.one" layout="tabular">
					<fr:layout>
						<fr:property name="hideValidators" value="true"/>
					</fr:layout>
				</fr:edit>
			</td>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.continue"/></html:submit>
			</td>				
		</tr>
	</table>
	<br/><br/>
	b) <bean:message key="label.rectifyMarkSheet.chooseStudent.fromList"/><br/><br/>
	<table class="tstyle4">
		<tr>
			<th>
				<bean:message key="label.student.number"/>
			</th>
		
			<th>
				<bean:message key="label.student.name"/>
			</th>
			<th>
				<bean:message key="label.evaluationDate"/>
			</th>
			<th>
				<bean:message key="label.student.grade"/>
			</th>		
			<th>
				&nbsp;
			</th>					
		</tr>
		
		<bean:define id="url" name="rectifyBean" property="url" />
		
		<logic:iterate id="evaluation" name="enrolmentEvaluations" type="net.sourceforge.fenixedu.domain.EnrolmentEvaluation" >
			<bean:define id="evaluationID" name="evaluation" property="idInternal"/>
			<bean:define id="studentNumber" name="evaluation" property="enrolment.studentCurricularPlan.student.number"/>
			<bean:define id="msID" name="msID"/>		
			<tr>
				<td>
					<% if(evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ) || evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)){ %>
						<html:link action='<%= "/rectifyMarkSheet.do?method=rectifyMarkSheetStepOneByEvaluation&evaluationID=" +  evaluationID + "&msID=" + msID + url %>'>
							<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.number"/>
						</html:link>					
					<% } else { %>
						<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.number"/>					
					<% }  %>						
				</td>
				<td>
					<% if(evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ) || evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)){ %>
						<html:link action='<%= "/rectifyMarkSheet.do?method=rectifyMarkSheetStepOneByEvaluation&evaluationID=" +  evaluationID + "&msID=" + msID + url %>'>
							<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.person.name"/>		
						</html:link>										
					<% } else { %>
						<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.person.name"/>		
					<% }  %>						
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy">
						<bean:write name="evaluation" property="examDate.time"/>
					</dt:format>
				</td>
				<td>
					<bean:write name="evaluation" property="grade"/>
				</td>
				<td>				
					<logic:equal name="evaluation" property="enrolmentEvaluationState" value="<%= EnrolmentEvaluationState.RECTIFIED_OBJ.toString() %>">
						<html:link action='<%= "/rectifyMarkSheet.do?method=showRectificationHistoric" + url %>' paramId="evaluationID" paramName="evaluationID">
							<bean:message key="label.markSheet.rectificationHistoric" />		
						</html:link>										
					</logic:equal>
					&nbsp;
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message key="label.back"/></html:cancel>
</fr:form>
