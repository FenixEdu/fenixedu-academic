<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

<br />

<logic:present name="teachers">
	<logic:notEmpty name="teachers">
		<b><bean:message key="label.coordinator.tutor.chooseTutorHistory" bundle="APPLICATION_RESOURCES" /></b>
		<br />
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
				<fr:property name="sortableSlots" value="teacher.teacherNumber,teacherName,teacher.currentWorkingDepartment.name,teacher.category.longName,teacher.numberOfPastTutorships,teacher.numberOfActiveTutorships"/>
            	<fr:property name="sortUrl" value="<%= String.format("/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutorHistory&" + parameters ) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "teacher.teacherNumber" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

