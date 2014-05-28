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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
	<h3>
		<bean:message key="label.executionCourse.instructions.title"/>
	</h3>
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