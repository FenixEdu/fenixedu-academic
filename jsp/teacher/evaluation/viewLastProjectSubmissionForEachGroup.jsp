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
	key="label.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.title" /></h2>

<fr:view name="project" schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight thright"/>
    </fr:layout>
</fr:view>

<br/>

<bean:define id="executionCourseID" name="executionCourseID" />
<logic:empty name="project" property="projectSubmissions">
	<span class="error"><!-- Error messages go here -->
		<bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.noProjectSubmissions"/>
	</span>
</logic:empty>
<logic:notEmpty name="project" property="projectSubmissions">
	<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle2"/>
	        <fr:property name="columnClasses" value=",,,acenter"/>
	        <fr:property name="linkFormat(download)" value="${projectSubmissionFile.downloadUrl}"/>
			<fr:property name="key(download)" value="link.common.download"/>
			<fr:property name="contextRelative(download)" value="false"/>
			<fr:property name="order(download)" value="1"/>
	        <fr:property name="linkFormat(viewProjectSubmissionsForGroup)" value="<%="/projectSubmissionsManagement.do?method=viewProjectSubmissionsByGroup&studentGroupID=${studentGroup.idInternal}&projectID=${project.idInternal}&executionCourseID=" + executionCourseID %>"/>
			<fr:property name="key(viewProjectSubmissionsForGroup)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.viewProjectSubmissionsByGroup"/>
			<fr:property name="order(viewProjectSubmissionsForGroup)" value="2"/>
	        <fr:property name="linkFormat(viewProjectSubmissionLogsForGroup)" value="<%="/projectSubmissionsManagement.do?method=viewProjectSubmissionLogsByGroup&studentGroupID=${studentGroup.idInternal}&projectID=${project.idInternal}&executionCourseID=" + executionCourseID %>"/>
			<fr:property name="key(viewProjectSubmissionLogsForGroup)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.viewProjectSubmissionLogsByGroup"/>		
			<fr:property name="order(viewProjectSubmissionLogsForGroup)" value="3" />
	    </fr:layout>
	</fr:view>
</logic:notEmpty>




