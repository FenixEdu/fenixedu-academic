<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error"><!-- Error messages go here -->
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img src="<%= request.getContextPath() %>/images/title_adminDisc.gif" alt="<bean:message key="title_adminDisc" bundle="IMAGE_RESOURCES" />" />
	<div class="mvert1">
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
	</div>
</logic:messagesNotPresent>