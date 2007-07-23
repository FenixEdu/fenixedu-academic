<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<bean:define id="forwardTo" name="forwardTo" toScope="request" />
<bean:define id="teacherNumber" name="tutorshipManagementBean" property="teacherNumber" />
<bean:define id="executionDegreeId" name="tutorshipManagementBean" property="executionDegreeID" />
<bean:define id="degreeCurricularPlanID" name="tutorshipManagementBean" property="degreeCurricularPlanID" />
<bean:define id="parameters" value="<%= "forwardTo=" + forwardTo + "&teacherNumber=" + teacherNumber + "&executionDegreeId=" + executionDegreeId + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>" />

<h2><bean:message key="label.coordinator.tutorshipHistory" bundle="APPLICATION_RESOURCES"/></h2>

<div class="infoop2">
	<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	
		<!-- SELECTED TEACHER -->
		<p class="mvert025"><b><bean:message key="label.tutor"/>:&nbsp;</b>
			<bean:write name="tutorshipManagementBean" property="teacher.person.name" />
			<bean:write name="tutorshipManagementBean" property="teacher.teacherNumber" />
		</p>
		<!-- CURRENT EXECUTION YEAR -->
		<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
		<p class="mvert025">
			<b><bean:message key="label.masterDegree.coordinator.executionYear"/></b>
			<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
		</p>
	</logic:present>
</div>

<ul>
	<li>
		<p class="mtop2 mbottom1"><html:link page="<%= "/tutorManagement.do?method=prepare&" +  parameters %>">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.back" /></html:link></p>
	</li>
</ul>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
			<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<!-- TUTORSHIP INFORMATION -->
<fr:view name="tutorshipManagementBean" schema="coordinator.tutor.tutorshipInfo">
	<fr:layout name="tabular">
   	    <fr:property name="classes" value="tstyle2 thlight thright"/>
    </fr:layout>
</fr:view>

<!-- DETAILED TUTORSHIP INFORMATION -->
<logic:present name="activeTutorships">
	<logic:notEmpty name="activeTutorships">
		<p class="mtop2 mbottom1"/>
			<b><bean:message key="label.coordinator.tutor.tutorshipInfo.currentTutoredStudents" bundle="APPLICATION_RESOURCES"/></b></p>
		<fr:view name="activeTutorships" schema="coordinator.tutorshipHistory.studentsList">
			<fr:layout name="tabular">
		  	    <fr:property name="classes" value="tstyle2 thlight tdcenter mtop05"/>
		  	    <fr:property name="columnClasses" value=",width150px aleft,,,,,,"/>
		   </fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>


<logic:present name="pastTutorships">
	<logic:notEmpty name="pastTutorships">
		<p class="mtop2 mbottom1"/>
			<b><bean:message key="label.coordinator.tutor.tutorshipInfo.pastTutoredStudents" bundle="APPLICATION_RESOURCES"/></b></p>
		<fr:view name="pastTutorships" schema="coordinator.tutorshipHistory.pastStudentsList">
			<fr:layout name="tabular">
		  	    <fr:property name="classes" value="tstyle2 thlight tdcenter mtop05"/>
		  	    <fr:property name="columnClasses" value=",width150px aleft,,,,,,aleft"/>		  	    
		   </fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
