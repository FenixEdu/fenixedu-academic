<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.teacher.executionCourseManagement.viewForuns.title" /></h2>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:notEmpty name="foruns">
	<bean:define id="executionCourseId" name="executionCourse" property="externalId" />
	
	<logic:notEmpty name="executionCourse" property="foruns">
		<logic:iterate id="executionCourseForum" name="executionCourse" property="foruns">
			<bean:size id="threadsCount" name="executionCourseForum" property="conversationThreads"/>
			<p>
				<html:link action="<%="/executionCourseForumManagement.do?method=viewForum&executionCourseID=" + executionCourseId%>" paramId="forumId" paramName="executionCourseForum" paramProperty="externalId">
					<bean:write name="executionCourseForum" property="name"/>
				</html:link>
				<span class="color888"> (<bean:write name="threadsCount"/> tópicos)</span>
			</p>
		</logic:iterate>
	</logic:notEmpty>

	<%--
	<fr:view name="foruns" layout="tabular-list">
		<fr:layout>
			<fr:property name="subLayout" value="values"/>
			<fr:property name="subSchema" value="executionCourseForum.view.nameOnly"/>
			<fr:property name="link(view)" value="<%="/executionCourseForumManagement.do?method=viewForum&executionCourseID=" + executionCourseId%>"/>
			<fr:property name="key(view)" value="link.teacher.executionCourseManagement.foruns.viewForum"/>
			<fr:property name="param(view)" value="externalId/forumId"/>
		</fr:layout>
	</fr:view>
	--%>
</logic:notEmpty>

<logic:empty name="foruns">
	<p>
		<em><!-- Error messages go here --><bean:message key="label.teacher.executionCourseManagement.viewForuns.noForuns"/>.</em>
	</p>
</logic:empty>
  
