<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message
	key="label.projectSubmissions.submitProject.title" /></h2>
	
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
		<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
	</html:messages>
	<br/><br/>
</logic:messagesPresent>

<fr:view name="project"
	schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
	</fr:layout>
</fr:view>

<bean:define id="attendsId" name="attends" property="idInternal" />
<bean:define id="projectId" name="project" property="idInternal" />

<fr:edit id="createProjectSubmission"
	name="projectSubmission"
	schema="projectSubmission.create"
	action="<%="/projectSubmission.do?method=submitProject&attendsId=" + attendsId + "&projectId=" + projectId%>">

	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight mtop05" />
	</fr:layout>

	<fr:hidden slot="project" name="project" />
	<fr:hidden slot="attends" name="attends" />
	<fr:hidden slot="studentGroup" name="studentGroup" />
	<fr:hidden slot="person" name="person" />
	
    <fr:destination name="cancel" path="<%= "/projectSubmission.do?method=viewProjectSubmissions&attendsId=" + attendsId  + "&projectId="+ projectId%>"/>
</fr:edit>
