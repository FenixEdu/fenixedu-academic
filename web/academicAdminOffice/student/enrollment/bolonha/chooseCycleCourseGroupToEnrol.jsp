<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
	<bean:define id="cycleTypeToEnrolQualifiedName" name="cycleEnrolmentBean" property="cycleTypeToEnrol.qualifiedName" />
	<h2><bean:message key="label.student.enrollment.enrolIn" bundle="ACADEMIC_OFFICE_RESOURCES" /> <bean:message  key="<%=cycleTypeToEnrolQualifiedName.toString()%>" bundle="ENUMERATION_RESOURCES"/></h2>
	
	
	<bean:define id="studentCurricularPlanId" name="cycleEnrolmentBean" property="studentCurricularPlan.idInternal" />
	<bean:define id="registrationId" name="cycleEnrolmentBean" property="studentCurricularPlan.registration.idInternal" />
	<bean:define id="executionPeriodId" name="cycleEnrolmentBean" property="executionPeriod.idInternal" />
	<bean:define id="withRules" name="withRules" />
	
	<logic:empty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<span class="error0">
			<bean:message  key="label.student.enrollment.cycleCourseGroup.noCycleDestinationAffinities" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</span>
		<br/><br/>
		<html:form action="<%="/bolonhaStudentEnrollment.do?method=cancelChooseCycleCourseGroupToEnrol&amp;scpID=" + studentCurricularPlanId.toString() + "&amp;executionPeriodID=" + executionPeriodId.toString() + "&amp;withRules=" + withRules.toString()%>">
			<html:submit altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
				<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:submit>
		</html:form>
	</logic:empty>
	
	<logic:notEmpty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
	<html:form action="<%="/bolonhaStudentEnrollment.do?scpID=" + studentCurricularPlanId.toString() + "&amp;executionPeriodID=" + executionPeriodId.toString() + "&amp;withRules=" + withRules.toString()%>">
	
		<input type="hidden" name="method" />
		<input type="hidden" name="withRules" value="<%=withRules.toString()%>"/>
		
		<logic:messagesPresent message="true">
			<div class="error0" style="padding: 0.5em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<span><bean:write name="messages" /></span>
			</html:messages>
			</div>
		</logic:messagesPresent>
		
		<fr:edit id="cycleEnrolmentBean" 
				 name="cycleEnrolmentBean" 
				 schema="CycleEnrolmentBean.chooseCycleCourseGroupToEnrol">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/bolonhaStudentEnrollment.do?method=enrolInCycleCourseGroupInvalid" />
		</fr:edit>
		
		<table>
			<tr>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInCycleCourseGroup';"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
				</td>
				<td>
					<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='cancelChooseCycleCourseGroupToEnrol';"><bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/></html:cancel>
				</td>
			</tr>
		</table>
		
	</html:form>
	</logic:notEmpty>

</logic:present>