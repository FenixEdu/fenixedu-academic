<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<h2><bean:message key="label.viewExecutionCourseForuns.title" /></h2>

<logic:iterate id="attend" name="attendsForExecutionPeriod">
<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>

	<p class="mtop2 mbottom0"><strong><bean:write name="executionCourse" property="nome"/></strong></p>
	<table>
		<logic:notEmpty name="executionCourse" property="foruns">
			<logic:iterate id="executionCourseForum" name="executionCourse" property="foruns">
				<tr>
					<td><bean:write name="executionCourseForum" property="name"/></td>
					<td><html:link action="/viewExecutionCourseForuns.do?method=viewForum" paramId="forumId" paramName="executionCourseForum" paramProperty="idInternal">
 					<bean:message key="link.viewExecutionCourseForuns.viewForum"/>
					</html:link>
					</td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="executionCourse" property="foruns">
			<tr>
			<td><span class="error"><!-- Error messages go here --><bean:message key="label.viewExecutionCourseForuns.noForumsForExecutionCourse" /></span></td>
			</tr>
		</logic:empty>
	</table>		
</logic:iterate>


  
