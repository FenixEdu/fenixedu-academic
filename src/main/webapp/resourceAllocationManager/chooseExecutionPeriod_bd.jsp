<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page
    import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml />

<html:xhtml />

<em><bean:message key="title.resourceAllocationManager.management" /></em>
<h2><bean:message key="title.manage.schedule" /></h2>

<p>
	<span class="error">
		<html:messages id="message" message="true">
    		<bean:write name="message" />
		</html:messages>
		 <!-- Error messages go here -->
		<html:errors />
	</span>
</p>

<fr:form id="executionSemesterSelectionForm" action="/chooseExecutionPeriod.do?method=choose">
	<fr:edit id="executionSemesterSelectionFormEdit" schema="academicIntervalSelectionBean.choosePostBack"
		name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose" />
	</html:submit>
</fr:form>

<br/>

<logic:present name="executionYear">
	<html:link page="/chooseExecutionPeriod.do?method=toggleFirstYearShiftsCapacity" paramId="executionYearId" paramName="executionYear" paramProperty="OID">
		<bean:message bundle="SOP_RESOURCES" key="link.toggleFirstYearShiftsCapacity" /> <bean:write name="executionYear" property="name"/>
	</html:link>
</logic:present>


<br/>
<br/>

<h3><bean:message key="title.manage.schedule.students" bundle="APPLICATION_RESOURCES" /></h3>

<fr:form id="studentSelectionForm" action="/chooseExecutionPeriod.do?method=chooseStudent">
	
	<fr:edit id="studentSelectionFormEdit" name="studentContextSelectionBean">
		
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.StudentContextSelectionBean">
			<fr:slot name="number" key="label.student.username.or.number"/>
			<fr:slot name="toEdit" key="label.edit.schedule"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose"/>
	</html:submit>
</fr:form>

<br/>

<logic:present name="toEditScheduleRegistrations">
	<logic:empty name="toEditScheduleRegistrations">
		<p><span class="error">
			<bean:message key="message.no.student.schedule.found" bundle="APPLICATION_RESOURCES"/>
		</span></p>
	</logic:empty>
	<logic:notEmpty name="toEditScheduleRegistrations">
		<logic:iterate id="registration" name="toEditScheduleRegistrations">
			<html:link page="/studentShiftEnrollmentManager.do?method=prepare" paramId="registrationOID" paramName="registration" paramProperty="idInternal">
				<bean:write name="registration" property="student.person.name"/> - <bean:write name="registration" property="degreeNameWithDegreeCurricularPlanName"/>
			</html:link>
			<br/>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>

<logic:present name="registrations">
	<logic:empty name="registrations">
		<p><span class="error">
			<bean:message key="message.no.student.schedule.found" bundle="APPLICATION_RESOURCES"/>
		</span></p>
	</logic:empty>
	<logic:notEmpty name="registrations">
		<logic:iterate id="registration" name="registrations">
			<html:link page="/chooseExecutionPeriod.do?method=chooseStudentById" paramId="registrationId" paramName="registration" paramProperty="externalId">
				<bean:write name="registration" property="student.person.name"/> - <bean:write name="registration" property="degreeNameWithDegreeCurricularPlanName"/>
			</html:link>
			<br/>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>