<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="executionDegreeId" name="tutorshipManagementBean" property="executionDegreeID" />
<bean:define id="degreeCurricularPlanID" name="tutorshipManagementBean" property="degreeCurricularPlanID" />
<bean:define id="parameters" value="<%= "executionDegreeId=" + executionDegreeId + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>" />

<h2><bean:message key="label.coordinator.tutorshipHistory" bundle="APPLICATION_RESOURCES" /></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<span class="error"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<html:errors />


<logic:present name="teachers">
	<logic:notEmpty name="teachers">
		<p>
			<b><bean:message key="label.coordinator.tutor.chooseTutorHistory" bundle="APPLICATION_RESOURCES" /></b>
		</p>

		<p class="color888 mvert05"><bean:message key="label.coordinator.tutor.chooseTutorHistory.help" bundle="APPLICATION_RESOURCES" /></p>
		
		<bean:message key="label.coordinator.tutor.chooseTutorHistory.filter" bundle="APPLICATION_RESOURCES"/>: 
		<html:link page="<%= "/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutorHistory&" + parameters %>">
			<bean:message bundle="APPLICATION_RESOURCES" key="link.coordinator.tutor.chooseTutorHistory.filter.showAll" />
		</html:link>,
		<html:link page="<%= "/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutorHistory&filtered=true&" + parameters %>">
			<bean:message bundle="APPLICATION_RESOURCES" key="link.coordinator.tutor.chooseTutorHistory.filter.showTutorsWithTutorshipHistory"/>
		</html:link>
		
		<fr:view name="teachers" layout="tabular-sortable" schema="coordinator.chooseTutorHistory.tutorList">
			<fr:layout>
				<fr:property name="columnClasses" value=",nowrap aleft,,smalltxt aleft,smalltxt aleft,,"/>
				<fr:property name="classes" value="tstyle1 mtop1 tdcenter"/>
				<fr:property name="columnClasses" value=",nowrap aleft,smalltxt aleft,smalltxt aleft,,"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="teacher.teacherId,teacherName,teacher.currentWorkingDepartment.name,teacher.category,teacher.numberOfPastTutorships,teacher.numberOfActiveTutorships"/>
            	<fr:property name="sortUrl" value="<%= String.format("/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutorHistory&" + parameters ) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "teacher.teacherId" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

