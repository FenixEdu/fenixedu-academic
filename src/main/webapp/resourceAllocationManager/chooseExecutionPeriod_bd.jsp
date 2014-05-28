<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page
    import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml />

<html:xhtml />

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
		<fr:layout name="flow">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<html:link styleClass="btn btn-primary" page="/chooseContext.do?method=prepare&academicInterval=${academicInterval}">
		<bean:message key="link.schedules.chooseContext"></bean:message>
	</html:link>
</fr:form>

<br/>
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
		<logic:notPresent name="noEditionAllowed">
			<fr:slot name="toEdit" key="label.edit.schedule"/>
		</logic:notPresent>
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
			<html:link page="/studentShiftEnrollmentManager.do?method=prepare" paramId="registrationOID" paramName="registration" paramProperty="externalId">
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
		<bean:define name="timeTableExecutionSemester" id="timeTableExecutionSemester" type="net.sourceforge.fenixedu.domain.ExecutionSemester"/> 
		<logic:iterate id="registration" name="registrations">
			<html:link page="<%= "/chooseExecutionPeriod.do?method=chooseStudentById&executionSemesterId=" + timeTableExecutionSemester.getExternalId() %>" paramId="registrationId" paramName="registration" paramProperty="externalId">
				<bean:write name="registration" property="student.person.name"/> - <bean:write name="registration" property="degreeNameWithDegreeCurricularPlanName"/>
			</html:link>
			<br/>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>