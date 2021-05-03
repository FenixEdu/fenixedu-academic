<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.projectSubmissions.viewProjectsWithOnlineSubmission.title" /></h2>

<fr:form action="/projectSubmission.do?method=viewProjectsWithOnlineSubmission">
	<fr:edit id="studentBean" name="studentBean" schema="list.student.execution.periods">
		<fr:destination name="postBack" path="/projectSubmission.do?method=viewProjectsWithOnlineSubmission"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<logic:notEmpty name="attends">
	<logic:iterate id="attend" name="attends">
		<bean:define id="executionCourse" name="attend" property="disciplinaExecucao" type="org.fenixedu.academic.domain.ExecutionCourse"/>
		<bean:define id="attendsId" name="attend" property="externalId" />
		<bean:define id="projectsWithOnlineSubmission" name="executionCourse" property="projectsWithOnlineSubmission" />
		<p class="mtop2 mbottom0"><strong><bean:write name="executionCourse" property="nome"/></strong></p>
		<logic:notEmpty name="projectsWithOnlineSubmission">
			<ul>
				<logic:iterate id="projectWithOnlineSubmission" name="projectsWithOnlineSubmission" type="org.fenixedu.academic.domain.Project">
					<bean:define id="projectId" name="projectWithOnlineSubmission" property="externalId" />
					<logic:equal value="false" name="projectWithOnlineSubmission" property="pastSubmission">
						<li>
							<bean:write name="projectWithOnlineSubmission" property="name"/> ,
							<html:link action="<%="/projectSubmission.do?method=viewProjectSubmissions&amp;attendsId=" + attendsId + "&amp;projectId=" + projectId%>">
								<bean:message key="link.projectSubmissions.viewProjectsWithOnlineSubmission.viewProjectSubmissions"/>
							</html:link>
						</li>
					</logic:equal>
				</logic:iterate>
			</ul>
			<bean:define id="pastProjectsWithOnlineSubmission" name="executionCourse" property="pastProjectsWithOnlineSubmission" />
			<logic:notEmpty name="pastProjectsWithOnlineSubmission">
				<details>
					<!-- Add list-item display property, otherwise the toggle arrow doesn't show up -->
					<summary  style="display: list-item;"><bean:message key="link.projectSubmissions.viewProjectsWithOnlineSubmission.viewPastProjects"/></summary>
					<ul>
						<logic:iterate id="pastProjectWithOnlineSubmission" name="pastProjectsWithOnlineSubmission" type="org.fenixedu.academic.domain.Project">
							<bean:define id="projectId" name="pastProjectWithOnlineSubmission" property="externalId" />
							<li>
								<bean:write name="pastProjectWithOnlineSubmission" property="name"/> ,
								<html:link action="<%="/projectSubmission.do?method=viewProjectSubmissions&amp;attendsId=" + attendsId + "&amp;projectId=" + projectId%>">
									<bean:message key="link.projectSubmissions.viewProjectsWithOnlineSubmission.viewProjectSubmissions"/>
								</html:link>
							</li>
						</logic:iterate>
					</ul>
				</details>
			</logic:notEmpty>
		</logic:notEmpty>
		<logic:empty name="projectsWithOnlineSubmission">
			<p class="mtop05">
				<em><!-- Error messages go here -->
					<bean:message key="label.projectSubmissions.viewProjectsWithOnlineSubmission.noProjectsWithOnlineSubmissionForExecutionCourse"/>.
				</em>
			</p>
		</logic:empty>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="attends">
	<p class="mtop15">
		<em><bean:message  key="label.projectSubmissions.viewProjectsWithOnlineSubmission.noAttendsForExecutionPeriod" bundle="STUDENT_RESOURCES"/></em>
	</p>
	
</logic:empty>
