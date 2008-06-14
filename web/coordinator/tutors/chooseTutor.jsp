<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="executionDegreeId" name="tutorshipManagementBean" property="executionDegreeID" />
<bean:define id="degreeCurricularPlanID" name="tutorshipManagementBean" property="degreeCurricularPlanID" />
<bean:define id="parameters" value="<%= "executionDegreeId=" + executionDegreeId + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>"/>

<h2><bean:message key="label.coordinator.tutorshipManagement" bundle="APPLICATION_RESOURCES" /></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<span class="error"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<html:errors />

<p class="mtop15 mbottom05">
	<b><bean:message key="label.coordinator.tutor.chooseTutor" bundle="APPLICATION_RESOURCES" /></b>
</p>


<p class="color888 mvert05"><bean:message key="message.coordinator.tutor.chooseTutor.help" bundle="APPLICATION_RESOURCES" /></p>
<logic:present name="tutorshipManagementBean">
	<fr:form action="/tutorManagement.do?method=prepare&forwardTo=readTutor">
		<fr:edit id="choosetutorshipManagementBean" name="tutorshipManagementBean" schema="coordinator.tutor.tutorNumber">
			<fr:destination name="invalid" path="<%= "/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutor&" +  parameters %>"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom0" />
				<fr:property name="columnClasses" value="width100px,width200px,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width200px">
					<html:submit><bean:message key="button.coordinator.tutor.chooseTutor" bundle="APPLICATION_RESOURCES"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<logic:notPresent name="tutors">
	<ul>
		<li><p class="mtop1 mbottom2">
			<html:link page="<%="/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutor&chooseFromList=true&" + parameters%>">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.tutor.chooseTutorFromListLink" /></html:link>
				<img src="<%= request.getContextPath() %>/images/external_icon.gif"/>
		</li>
	</ul>
</logic:notPresent>
		
<logic:present name="tutors">
	<logic:notEmpty name="tutors">
		<p>
			<b><bean:message key="label.coordinator.tutor.chooseTutor.tutorList" bundle="APPLICATION_RESOURCES" /></b>
		</p>
		<p class="color888 mvert05"><bean:message key="label.coordinator.tutor.chooseTutor.tutorList.help" bundle="APPLICATION_RESOURCES" /></p>
		<fr:view name="tutors" layout="tabular-sortable" schema="coordinator.chooseTutor.tutorList">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 mtop1 tdcenter"/>
				<fr:property name="columnClasses" value=",nowrap aleft,,smalltxt aleft,smalltxt aleft,,"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="teacherNumber,teacher.person.name,teacher.currentWorkingDepartment.name,teacher.category.longName,teacher.numberOfPastTutorships,teacher.numberOfActiveTutorships"/>
            	<fr:property name="sortUrl" value="<%= String.format("/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutor&chooseFromList=true&" + parameters) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "teacher.teacherNumber" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

