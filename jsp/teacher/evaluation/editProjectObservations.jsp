<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
 

<h2><bean:message key="label.teacher.executionCourseManagement.evaluation.project.editProjectObservation.title" /></h2>

<bean:define id="executionCourseID" value="<%= request.getParameter("executionCourseID")%>"/>
<bean:define id="studentGroupID" value="<%= request.getParameter("studentGroupID")%>"/>
<bean:define id="projectID" value="<%= request.getParameter("projectID")%>"/>
<bean:define id="edit" value="<%= request.getParameter("edit") %>"/>

<logic:equal name="edit" value="false">
<ul>
<li>
<html:link page="<%= "/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&executionCourseID=" + executionCourseID + "&projectID=" + projectID%>">
			<bean:message key="label.return"/>
</html:link>
</li>
<li>
<html:link page="<%= "/projectSubmissionsManagement.do?method=sendCommentThroughEmail&studentGroupID=" + studentGroupID + "&amp;projectID=" + projectID +"&amp;executionCourseID=" + executionCourseID + "&amp;edit=false"%>">
			<bean:message key="label.teacher.executionCourseManagement.evaluation.project.sendObsByEmail"/>
</html:link>
</li>

<li>
<html:link page="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&amp;edit=true&amp;studentGroupID=" + studentGroupID + "&amp;projectID=" +  projectID + "&executionCourseID=" + executionCourseID %>">
			<bean:message key="label.teacher.executionCourseManagement.evaluation.project.editObservation"/>
</html:link>
</li>
</logic:equal>
</ul>

<logic:equal name="edit" value="false">
<fr:view name="projectSubmission" schema="projectSubmission.viewObservation">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 "/>
        <fr:property name="rowClasses" value="aleft,aleft,aleft,"/>
    </fr:layout>
</fr:view>
</logic:equal>

<logic:equal name="edit" value="true">
<fr:edit name="projectSubmission" schema="projectSubmission.editObservation">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 "/>
        <fr:property name="rowClasses" value="aleft,aleft,aleft,"/>
    </fr:layout>
    <fr:destination name="cancel" path="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=" + studentGroupID + "&projectID=" + projectID + "&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
</fr:edit>
</logic:equal>