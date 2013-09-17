<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="title.student.portalTitle" /></em>
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
		<bean:define id="executionCourse" name="attend" property="disciplinaExecucao" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
		<bean:define id="attendsId" name="attend" property="externalId" />
		<bean:define id="projectsWithOnlineSubmission" name="executionCourse" property="projectsWithOnlineSubmission" />
		<p class="mtop2 mbottom0"><strong><bean:write name="executionCourse" property="nome"/></strong></p>
		<logic:notEmpty name="projectsWithOnlineSubmission">
			<ul>
				<logic:iterate id="projectWithOnlineSubmission" name="projectsWithOnlineSubmission" type="net.sourceforge.fenixedu.domain.Project">
					<bean:define id="projectId" name="projectWithOnlineSubmission" property="externalId" />
					<li>
						<bean:write name="projectWithOnlineSubmission" property="name"/> , 
						<html:link action="<%="/projectSubmission.do?method=viewProjectSubmissions&amp;attendsId=" + attendsId + "&amp;projectId=" + projectId%>">
							<bean:message key="link.projectSubmissions.viewProjectsWithOnlineSubmission.viewProjectSubmissions"/>
						</html:link>
					</li>
				</logic:iterate>
			</ul>
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
