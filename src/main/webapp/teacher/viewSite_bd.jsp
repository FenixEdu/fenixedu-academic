<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error"><!-- Error messages go here -->
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img src="<%= request.getContextPath() %>/images/title_adminDisc.gif" alt="<bean:message key="title_adminDisc" bundle="IMAGE_RESOURCES" />" />
	<p><bean:message key="label.executionCourse.instructions00.intro" /></p>
	<ul class="list4">
		<li><bean:message key="label.executionCourse.instructions01.customization"/></li>
		<li><bean:message key="label.executionCourse.instructions02.announcements"/></li>
		<li><bean:message key="label.executionCourse.instructions03.sections"/></li>
		<li><bean:message key="label.executionCourse.instructions04.summaries"/></li>
		<li><bean:message key="label.executionCourse.instructions05.teachers"/></li>
		<li><bean:message key="label.executionCourse.instructions06.students"/></li>
		<li><bean:message key="label.executionCourse.instructions07.planning"/></li>
		<li><bean:message key="label.executionCourse.instructions08.evaluation"/></li>
		<li><bean:message key="label.executionCourse.instructions09.objectives"/></li>
		<li><bean:message key="label.executionCourse.instructions10.groups"/></li>
	</ul>
</logic:messagesNotPresent>