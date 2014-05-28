<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here --> <bean:write name="message" /> </span>
</html:messages>

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
	        <fr:property name="linkFormat(viewProjectSubmissionsForGroup)" value="<%="/projectSubmissionsManagement.do?method=viewProjectSubmissionsByGroup&studentGroupID=${studentGroup.externalId}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID %>"/>
			<fr:property name="key(viewProjectSubmissionsForGroup)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.viewProjectSubmissionsByGroup"/>
			<fr:property name="bundle(viewProjectSubmissionsForGroup)" value="APPLICATION_RESOURCES"/>
			<fr:property name="linkFormat(groupComment)" value="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=${studentGroup.externalId}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
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
	        <fr:property name="linkFormat(viewProjectSubmissionsForGroup)" value="<%="/projectSubmissionsManagement.do?method=viewProjectSubmissionsByGroup&studentGroupID=${studentGroup.externalId}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID %>"/>
			<fr:property name="key(viewProjectSubmissionsForGroup)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.viewProjectSubmissionsByGroup"/>
			<fr:property name="linkFormat(groupComment)" value="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=${studentGroup.externalId}&projectOID=${project.externalId}&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
			<fr:property name="key(groupComment)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.groupComment"/>
			<fr:property name="visibleIfNot(groupComment)" value="project.submissionPeriodOpen"/>
	    </fr:layout>
		<fr:destination name="groupNumberLink" path="<%= "/studentGroupManagement.do?method=viewDeletedStudentGroupInformation&amp;studentGroupId=${studentGroup.externalId}&amp;executionCourseID=" + executionCourseID + "&amp;projectOID=${project.externalId}"  %>"/>
	</fr:view>

</logic:notEmpty>
</logic:notEmpty>

