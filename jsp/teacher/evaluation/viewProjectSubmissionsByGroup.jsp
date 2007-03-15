<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here --> <bean:write name="message" /> </span>
</html:messages>
<h2><bean:message
	key="label.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissionsByGroup.title" /></h2>

<bean:define id="executionCourseID" value="<%= request.getParameter("executionCourseID")%>"/>
<bean:define id="studentGroupID" value="<%= request.getParameter("studentGroupID")%>"/>
<bean:define id="projectID" value="<%= request.getParameter("projectID")%>"/>

<ul>
<li>
<html:link page="<%= "/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&executionCourseID=" + executionCourseID + "&projectID=" + projectID%>">
			<bean:message key="label.return"/>
</html:link>

</li>
</ul>

<fr:view name="project" schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight thright"/>
    </fr:layout>
</fr:view>

<br/>

<strong><bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissionLogsByGroup.LastSubmission"/></strong>:
<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2"/>
        <fr:property name="columnClasses" value="acenter,nowrap acenter,nowrap,acenter,smalltxt,"/>
    </fr:layout>
</fr:view>

<p>

<strong><bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissionLogsByGroup.title"/></strong>:

<fr:view name="projectSubmissionLogs" schema="projectSubmissionLog.view-full">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2"/>
        <fr:property name="columnClasses" value="acenter,nowrap acenter,nowrap,acenter,smalltxt,"/>
    </fr:layout>
</fr:view>
</p>

