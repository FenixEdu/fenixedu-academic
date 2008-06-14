<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<bean:define id="teacherNumber" name="tutorshipManagementBean" property="teacherNumber" />
<bean:define id="executionDegreeId" name="tutorshipManagementBean" property="executionDegreeID" />
<bean:define id="degreeCurricularPlanID" name="tutorshipManagementBean" property="degreeCurricularPlanID" />
<bean:define id="parameters" value="<%= "teacherNumber=" + teacherNumber + "&executionDegreeId=" + executionDegreeId + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>" />

<h2><bean:message key="label.coordinator.tutorshipManagement" bundle="APPLICATION_RESOURCES" /></h2>

<div class="infoop2">
	<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	
		<!-- SELECTED TEACHER -->
		<p class="mvert025"><bean:message key="label.tutor" />: 
			<bean:write name="tutorshipManagementBean" property="teacher.person.name" />
			(<bean:write name="tutorshipManagementBean" property="teacher.teacherNumber" />)
		</p>
		<!-- CURRENT EXECUTION YEAR -->
		<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
		<p class="mvert025">
			<bean:message key="label.masterDegree.coordinator.executionYear"/> 
			<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
		</p>
	</logic:present>
</div>


<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>


<!-- TUTORSHIP INFORMATION -->
<p class="mbottom05">
	<b><bean:message key="label.coordinator.tutor.tutorshipInfo.tutorshipHistory" bundle="APPLICATION_RESOURCES" /></b>
</p>

<fr:view name="tutorshipManagementBean" schema="coordinator.tutor.tutorshipInfo">
	<fr:layout name="tabular">
   	    <fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
    </fr:layout>
</fr:view>

<ul>
	<li>
		<html:link page="<%= "/tutorManagement.do?method=viewTutorshipHistory&forwardTo=readTutor&" + parameters %>">
			<bean:message key="link.coordinator.tutor.viewHistory" bundle="APPLICATION_RESOURCES" />
		</html:link>
	</li>
</ul>

<!-- ADD NEW TUTORSHIP FORM -->
<!-- ASSOCIATE A SINGLE STUDENT TO SELECTED TUTOR -->
<p class="mtop15 mbottom05">
	<b><bean:message key="label.coordinator.tutor.associateStudent" bundle="APPLICATION_RESOURCES" /></b>
</p>

<p class="color888 mvert05"><bean:message key="message.coordinator.tutor.associateOneStudent.help" bundle="APPLICATION_RESOURCES" /></p>
<fr:form action="/tutorshipManagement.do?method=insertTutorshipWithOneStudent">
	<fr:edit id="associateOneStudentBean" name="tutorshipManagementBean" schema="coordinator.tutor.addTutorship">
		<fr:destination name="invalid" path="<%= "/tutorManagement.do?method=prepare&forwardTo=readTutor&" +  parameters %>" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft thmiddle mtop0 mbottom0" />
			<fr:property name="columnClasses" value="width125px,width250px,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	<table class="tstyle5 gluetop mtop0">
		<tr>
			<td class="width125px"></td>
			<td class="width250px">
				<html:submit><bean:message key="button.coordinator.tutor.associateOneStudent" bundle="APPLICATION_RESOURCES" /></html:submit>
			</td>
		</tr>
	</table>
</fr:form>


<!-- TUTORSHIP MANAGEMENT FORM -->
<!-- IT SHOWS ALL STUDENTS FROM SELECTED TUTOR, ORDERED BY ENTRY YEAR -->
<p>
	<b><bean:message key="label.coordinator.tutor.manageStudents" bundle="APPLICATION_RESOURCES" /></b>
</p>

<p class="color888 mtop05 mbottom0">
	<bean:message key="message.coordinator.tutor.manageStudents.help" bundle="APPLICATION_RESOURCES" /></p>
<p class="color888 mtop0 mbottom1">
	<bean:message key="message.coordinator.tutor.manageStudents.changeTutorshipDate.help" bundle="APPLICATION_RESOURCES"/></p>
<ul>
	<li>
		<html:link page="<%= "/changeTutorship.do?method=prepareChangeTutorshipsEndDates&" + parameters %>">
			<bean:message key="link.coordinator.tutor.manageStudents.changeTutorshipDate" bundle="APPLICATION_RESOURCES" /></html:link>
	</li>
</ul>

<logic:present name="tutorshipManagementBeansByEntryYear">
	<fr:form action="/tutorshipManagement.do?method=manageTutorships">
		<fr:edit id="tutorshipManagementBean" name="tutorshipManagementBean" visible="false"/>
		<logic:iterate id="manageTutorshipBean" name="tutorshipManagementBeansByEntryYear" indexId="i">
			<bean:message key="label.studentsEntryYear" bundle="APPLICATION_RESOURCES" />&nbsp;
			<span class="highlight1"><bean:write name="manageTutorshipBean" property="executionYear.year" /></span>
			<fr:edit id="<%= "manageTutorshipBean" + i %>" name="manageTutorshipBean" schema="coordinator.tutor.studentsListTabularOption">
				<fr:destination name="invalid" path="<%= "/tutorManagement.do?method=prepare&forwardTo=readTutor&" +  parameters %>" />
				<fr:layout>
					<fr:property name="displayLabel" value="false"/>
				</fr:layout>
			</fr:edit>
		</logic:iterate>
		<html:submit property="transfer"><bean:message key="button.coordinator.tutor.transfer" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit property="remove"><bean:message key="button.coordinator.tutor.remove" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:reset><bean:message key="button.coordinator.tutor.clear" bundle="APPLICATION_RESOURCES" /></html:reset>
	</fr:form>
</logic:present>
<logic:notPresent name="tutorshipManagementBeansByEntryYear">
	<p class="mtop1"><span class="error">
		<bean:message key="label.coordinator.tutor.emptyStudentsList" bundle="APPLICATION_RESOURCES" /></span></p>
</logic:notPresent>

