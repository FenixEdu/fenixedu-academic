<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
 
<em><bean:message key="message.evaluationElements"/></em>
<h2><bean:message key="label.teacher.executionCourseManagement.evaluation.project.editProjectObservation.title"/></h2>

<bean:define id="executionCourseID" value="<%= request.getParameter("executionCourseID")%>"/>
<bean:define id="studentGroupID" value="<%= request.getParameter("studentGroupID")%>"/>
<bean:define id="edit" value="<%= request.getParameter("edit") %>"/>
<bean:define id="projectOID" name="project" property="externalId"/>

<logic:equal name="edit" value="false">
<ul>
	<li>
		<html:link page="<%= "/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&executionCourseID=" + executionCourseID + "&projectOID=" + projectOID%>">
			<bean:message key="label.return"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&amp;edit=true&amp;studentGroupID=" + studentGroupID + "&amp;projectOID=" +  projectOID + "&executionCourseID=" + executionCourseID %>">
			<bean:message key="label.teacher.executionCourseManagement.evaluation.project.editObservation"/>
		</html:link>
	</li>
</ul>
</logic:equal>

<logic:equal name="edit" value="false">
<fr:view name="projectSubmission" schema="projectSubmission.viewObservation">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright"/>
        <fr:property name="rowClasses" value="aleft,aleft,aleft,"/>
    </fr:layout>
</fr:view>

<p class="mtop05 mbottom05"><bean:message key="label.teacher.executionCourseManagement.evaluation.project.sendObsByEmail"/>:</p>
<fr:form action="<%= "/projectSubmissionsManagement.do?method=sendCommentThroughEmail&studentGroupID=" + studentGroupID + "&amp;projectOID=" + projectOID +"&amp;executionCourseID=" + executionCourseID + "&amp;edit=false"%>">
	<html:submit><bean:message key="link.coordinator.sendMail"/></html:submit>
</fr:form>

</logic:equal>

<logic:equal name="edit" value="true">
<fr:edit name="projectSubmission" schema="projectSubmission.editObservation">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight thright"/>
        <fr:property name="rowClasses" value="aleft,aleft,aleft,"/>
    </fr:layout>
    <fr:destination name="cancel" path="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=" + studentGroupID + "&projectOID=" + projectOID + "&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
	<fr:destination name="success" path="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=" + studentGroupID + "&projectOID=" + projectOID + "&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
</fr:edit>
</logic:equal>