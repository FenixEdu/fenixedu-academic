<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.projectSubmissions.viewProjectsWithOnlineSubmission.title" /></h2>

<logic:iterate id="attend" name="attendsForCurrentExecutionPeriod">
	<bean:define id="executionCourse" name="attend" property="disciplinaExecucao" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
	<bean:define id="attendsId" name="attend" property="idInternal" />
	<bean:define id="projectsWithOnlineSubmission" name="executionCourse" property="projectsWithOnlineSubmission" />
	<p class="mtop2 mbottom0"><strong><bean:write name="executionCourse" property="nome"/></strong></p>
	<logic:notEmpty name="projectsWithOnlineSubmission">
		<table>
			<logic:iterate id="projectWithOnlineSubmission" name="projectsWithOnlineSubmission" type="net.sourceforge.fenixedu.domain.Project">
				<bean:define id="projectId" name="projectWithOnlineSubmission" property="idInternal" />
				<tr>
					<td>
						<bean:write name="projectWithOnlineSubmission" property="name"/>
						<html:link action="<%="/projectSubmission.do?method=viewProjectSubmissions&attendsId=" + attendsId + "&projectId=" + projectId%>">
							<bean:message key="link.projectSubmissions.viewProjectsWithOnlineSubmission.viewProjectSubmissions"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="projectsWithOnlineSubmission">
		<span class="error"><!-- Error messages go here -->
			<bean:message key="label.projectSubmissions.viewProjectsWithOnlineSubmission.noProjectsWithOnlineSubmissionForExecutionCourse"/>
		</span>
	</logic:empty>
</logic:iterate>

