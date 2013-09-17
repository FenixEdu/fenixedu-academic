<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="label.viewExecutionCourseForuns.title" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:notEmpty name="attendsForExecutionPeriod">
<table>
<logic:iterate id="attend" name="attendsForExecutionPeriod">
	<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>

	<tr>
		<td style="padding: 16px 0px 4px 0px;"><strong><bean:write name="executionCourse" property="nome"/></strong></td>
		<td></td>
	</tr>
	
	<logic:notEmpty name="executionCourse" property="foruns">
		<logic:iterate id="executionCourseForum" name="executionCourse" property="foruns">
			<bean:size id="threadsCount" name="executionCourseForum" property="conversationThreads"/>
			<tr>
				<td>
					<html:link action="/viewExecutionCourseForuns.do?method=viewForum" paramId="forumId" paramName="executionCourseForum" paramProperty="externalId">
						<bean:write name="executionCourseForum" property="name"/>
					</html:link>
				</td>
				<td>
					<span class="color888">Tópicos: <bean:write name="threadsCount"/></span>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
		
	<logic:empty name="executionCourse" property="foruns">
		<tr>
			<td><em><!-- Error messages go here --><bean:message key="label.viewExecutionCourseForuns.noForumsForExecutionCourse" />.</em></td>
		</tr>
	</logic:empty>
	
</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="attendsForExecutionPeriod">
	<p class="mtop15">
		<em><bean:message key="label.viewExecutionCourseForuns.noAttendsForExecutionPeriod" bundle="STUDENT_RESOURCES"/></em>
	</p>
</logic:empty>
