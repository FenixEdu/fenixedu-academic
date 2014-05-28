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


<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here --> <bean:write name="message" /> </span>
</html:messages>

<em><bean:message key="message.evaluationElements" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissionsByGroup.title" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="executionCourseID" value="<%= request.getParameter("executionCourseID")%>"/>
<bean:define id="studentGroupID" value="<%= request.getParameter("studentGroupID")%>"/>
<bean:define id="projectOID" value="<%= (String)request.getParameter("projectOID")%>" type="java.lang.String"/>

<p>
	<html:link page="<%= "/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&executionCourseID=" + executionCourseID + "&projectOID=" + projectOID %>">
		<bean:message key="label.return" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>


<fr:view name="project" schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright"/>
		<fr:property name="rowClasses" value="bold,,,,,,"/>
    </fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissionLogsByGroup.LastSubmission" bundle="APPLICATION_RESOURCES"/>:</strong></p>
<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight mtop05"/>
        <fr:property name="columnClasses" value="acenter,nowrap acenter,nowrap,acenter,smalltxt,"/>
    </fr:layout>
</fr:view>

<p class="mbottom05"><strong><bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissionLogsByGroup.title" bundle="APPLICATION_RESOURCES"/>:</strong></p>
<fr:view name="projectSubmissionLogs" schema="projectSubmissionLog.view-full">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight mtop05"/>
        <fr:property name="columnClasses" value="acenter,nowrap acenter,nowrap,acenter,smalltxt,"/>
    </fr:layout>
</fr:view>


