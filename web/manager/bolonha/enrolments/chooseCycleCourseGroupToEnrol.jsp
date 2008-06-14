<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">
	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
	
	<bean:define id="cycleTypeToEnrolQualifiedName" name="cycleEnrolmentBean" property="cycleTypeToEnrol.qualifiedName" />
	<h2><bean:message key="label.student.enrollment.enrolIn" bundle="ACADEMIC_OFFICE_RESOURCES" /> <bean:message  key="<%=cycleTypeToEnrolQualifiedName.toString()%>" bundle="ENUMERATION_RESOURCES"/></h2>
	
	
	<bean:define id="studentCurricularPlanId" name="cycleEnrolmentBean" property="studentCurricularPlan.idInternal" />
	
	<logic:empty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<span class="error0">
			<bean:message  key="label.student.enrollment.cycleCourseGroup.noCycleDestinationAffinities" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</span>
		<br/><br/>
		<fr:form action="<%="/bolonhaStudentEnrolment.do?method=prepareShowDegreeModulesToEnrol&amp;scpId=" + studentCurricularPlanId.toString()%>">
			<html:cancel altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
				<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:cancel>
		</fr:form>
	</logic:empty>
	
	<logic:notEmpty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<logic:messagesPresent message="true">
			<div class="error0" style="padding: 0.5em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<span><bean:write name="messages" /></span>
			</html:messages>
			</div>
		</logic:messagesPresent>
		
		<fr:edit id="cycleEnrolmentBean" 
				 name="cycleEnrolmentBean" 
				 schema="CycleEnrolmentBean.chooseCycleCourseGroupToEnrol"
				 action="/bolonhaStudentEnrolment.do?method=enrolInCycleCourseGroup">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/bolonhaStudentEnrolment.do?method=enrolInCycleCourseGroupInvalid" />
			<fr:destination name="cancel" path="<%="/bolonhaStudentEnrolment.do?method=prepareShowDegreeModulesToEnrol&scpId=" + studentCurricularPlanId.toString()%>"/>
		</fr:edit>
	</logic:notEmpty>
</logic:present>

