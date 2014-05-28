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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.projectSubmissions.submitProject.title" /></h2>
	
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<fr:view name="project"	schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright" />
	</fr:layout>
</fr:view>

<bean:define id="attendsId" name="attends" property="externalId" />
<bean:define id="projectId" name="project" property="externalId" />

<fr:edit id="createProjectSubmission" name="projectSubmission" action="<%="/projectSubmission.do?method=submitProject&amp;attendsId=" + attendsId + "&amp;projectId=" + projectId%>">
	<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.projectSubmission.CreateProjectSubmissionBean" bundle="STUDENT_RESOURCES">
		<fr:slot name="inputStream" key="label.projectSubmission.projectFile" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:property name="fileNameSlot" value="filename"/>
			<fr:property name="maxSize" value="63mb"/>
			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
				<fr:property name="maxSize" value="63mb"/>
			</fr:validator>	
		</fr:slot>
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thmiddle mtop1 mbottom11" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>

	<fr:hidden slot="project" name="project" />
	<fr:hidden slot="attends" name="attends" />
	<fr:hidden slot="studentGroup" name="studentGroup" />
	<fr:hidden slot="person" name="person" />
	
    <fr:destination name="cancel" path="<%= "/projectSubmission.do?method=viewProjectSubmissions&amp;attendsId=" + attendsId  + "&amp;projectId="+ projectId%>"/>
</fr:edit>
