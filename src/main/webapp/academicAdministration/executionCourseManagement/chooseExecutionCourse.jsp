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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionDegreeManagement.announcements.swap"/></h2>

<bean:define id="sourceExecutionCourseId" name="bean" property="sourceExecutionCourse.externalId"/>
<bean:define id="degreeName" name="bean" property="sourceExecutionCourse.degreePresentationString"/>

<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<p class="mvert05">
	<strong>
		<logic:present name="degreeName"><fr:view name="degreeName"/> - </logic:present>
		<fr:view name="bean" property="sourceExecutionCourse.nome"/> (<fr:view name="bean" property="sourceExecutionCourse.sigla"/>)
	</strong>
</p>

<bean:define id="periodId" name="bean" property="sourceExecutionCourse.executionPeriod.externalId"/>
<bean:define id="periodName" name="bean" property="sourceExecutionCourse.executionPeriod.name"/>
<bean:define id="periodYear" name="bean" property="sourceExecutionCourse.executionPeriod.executionYear.year"/>

<bean:define id="labelParameter" value="<%= periodName + " - "  + periodYear + "~" + periodId%>"/>

<style>
.selectWidth600px { width: 600px; }
</style>

<div class="dinline forminline">

	<fr:form action="/announcementSwap.do?method=swap">
		<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
		<fr:edit id="executionCourseBean" name="bean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseBean" bundle="MANAGER_RESOURCES">
				<fr:slot name="executionSemester" layout="menu-select-postback" key="label.manager.executionCourseManagement.executionPeriod">
					<fr:property name="format" value="${qualifiedName}"/>
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider"/>
					<fr:property name="saveOptions" value="true"/>
				</fr:slot>
				<fr:slot name="destinationExecutionCourse" layout="menu-select" key="executionCourse.destination">
					<fr:property name="format" value="${nome} - ${sigla} - (${degreePresentationString})"/>
					<fr:property name="sortBy" value="nome"/>
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.academicAdminOffice.ExecutionCoursesProvider"/>
					<fr:property name="saveOptions" value="true"/>
					<fr:property name="classes" value="selectWidth600px"/>
				</fr:slot>
				<fr:slot name="announcements" layout="option-select" key="manager.announcements.stats.announcementsStats.label">
					<fr:property name="eachSchema" value="announcement.view-with-subject"/>
					<fr:property name="eachLayout" value="values-comma"/>
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.manager.AnnouncementsForBoard"/>
					<fr:property name="saveOptions" value="true"/>
					<fr:property name="classes" value="nobullet ulindent0 mvert0"/>
				</fr:slot>
			</fr:schema>
			<fr:destination name="postBack" path="/announcementSwap.do?method=postBack"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit>
			<bean:message bundle="MANAGER_RESOURCES" key="button.submit"/>
		</html:submit>
	</fr:form>
	
	<fr:form action="/editExecutionCourseChooseExPeriod.do?method=listExecutionCourseActions">
		<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
		<html:submit>
			<bean:message bundle="MANAGER_RESOURCES" key="button.cancel"/>
		</html:submit>
	</fr:form>

</div>