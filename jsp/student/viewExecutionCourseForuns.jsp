<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="label.viewExecutionCourseForuns.title" /></h2>


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
					<html:link action="/viewExecutionCourseForuns.do?method=viewForum" paramId="forumId" paramName="executionCourseForum" paramProperty="idInternal">
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
			<td><span class="error"><!-- Error messages go here --><bean:message key="label.viewExecutionCourseForuns.noForumsForExecutionCourse" /></span></td>
		</tr>
	</logic:empty>
	
</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="attendsForExecutionPeriod">
	<p class="mtop15">
		<em><bean:message  key="label.viewExecutionCourseForuns.noAttendsForExecutionPeriod" bundle="STUDENT_RESOURCES"/></em>
	</p>
</logic:empty>
