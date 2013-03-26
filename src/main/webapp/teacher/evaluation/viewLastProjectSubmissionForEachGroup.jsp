<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here --> <bean:write name="message" /> </span>
</html:messages>

<em><bean:message key="message.evaluationElements"  bundle="APPLICATION_RESOURCES" /></em>
<h2><bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.title" bundle="APPLICATION_RESOURCES"/></h2>

<fr:view name="project" schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright"/>
        <fr:property name="rowClasses" value="bold,,,,,,"/>
    </fr:layout>
</fr:view>


<bean:define id="executionCourseID" name="executionCourseID" />
<logic:empty name="projectSubmissions">
	<p>
		<span class="warning0"><!-- Error messages go here -->
			<bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.noProjectSubmissions"  bundle="APPLICATION_RESOURCES"/>
		</span>
	</p>
</logic:empty>
<logic:notEmpty name="projectSubmissions">
	<bean:define id="projectOID" name="project" property="externalId"/>
	
	<p class="mtop1 mbottom05">
		<bean:message key="label.teacher.executionCourseManagement.evaluation.project.downloadProjectsInZipFormat"   bundle="APPLICATION_RESOURCES"/>:
		<html:link page="<%="/projectSubmissionsManagement.do?method=downloadProjectsInZipFormat&amp;projectOID=" + projectOID %>">
			<bean:message key="link.common.download"   bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</p>
	<p class="mtop05">
		<bean:message key="label.teacher.executionCourseManagement.evaluation.project.partsDownload"   bundle="APPLICATION_RESOURCES"/>:
		<html:link page="<%= "/projectSubmissionsManagement.do?method=prepareSelectiveDownload&executionCourseID=" + executionCourseID + "&projectOID=" + projectOID %>">
			<bean:message key="link.common.download"   bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</p>
	
	<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle2 thlight"/>
	        <fr:property name="columnClasses" value="acenter,acenter,nowrap,nowrap acenter,smalltxt,aright,nowrap"/>
	        <fr:property name="linkFormat(viewProjectSubmissionsForGroup)" value="<%="/projectSubmissionsManagement.do?method=viewProjectSubmissionsByGroup&studentGroupID=${studentGroup.idInternal}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID %>"/>
			<fr:property name="key(viewProjectSubmissionsForGroup)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.viewProjectSubmissionsByGroup"/>
			<fr:property name="bundle(viewProjectSubmissionsForGroup)" value="APPLICATION_RESOURCES"/>
			<fr:property name="linkFormat(groupComment)" value="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=${studentGroup.idInternal}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
			<fr:property name="key(groupComment)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.groupComment"/>
			<fr:property name="bundle(groupComment)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIfNot(groupComment)" value="project.submissionPeriodOpen"/>
			<fr:property name="visibleIf(groupComment)" value="project.canComment"/>
	    </fr:layout>
	</fr:view>

<logic:notEmpty name="deletedStudentGroupProjectSubmissions">


<p class="mtop15 mbottom05"><b>Existem grupos apagados com submissões de projectos:</b></p>

	<fr:view name="deletedStudentGroupProjectSubmissions" schema="deletedProjectSubmission.view-full">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle2 thlight mtop05"/>
	        <fr:property name="columnClasses" value="acenter,acenter,nowrap,nowrap acenter,smalltxt,aright,nowrap"/>
	        <fr:property name="linkFormat(viewProjectSubmissionsForGroup)" value="<%="/projectSubmissionsManagement.do?method=viewProjectSubmissionsByGroup&studentGroupID=${studentGroup.idInternal}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID %>"/>
			<fr:property name="key(viewProjectSubmissionsForGroup)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.viewProjectSubmissionsByGroup"/>
			<fr:property name="linkFormat(groupComment)" value="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=${studentGroup.idInternal}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
			<fr:property name="key(groupComment)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.groupComment"/>
			<fr:property name="visibleIfNot(groupComment)" value="project.submissionPeriodOpen"/>
	    </fr:layout>
		<fr:destination name="groupNumberLink" path="<%= "/viewStudentGroupInformation.do?method=viewDeletedStudentGroupInformation&amp;studentGroupId=${studentGroup.idInternal}&amp;executionCourseID=" + executionCourseID + "&amp;projectOID=${project.externalId}"  %>"/>
	</fr:view>

</logic:notEmpty>
</logic:notEmpty>

