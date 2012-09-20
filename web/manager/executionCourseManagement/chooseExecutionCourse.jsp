<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2>Mover Anúncios</h2>

<bean:define id="sourceExecutionCourseId" name="bean" property="sourceExecutionCourse.idInternal"/>

<p class="mvert05"><strong><fr:view name="sourceExecutionCourseId"/> - <fr:view name="bean" property="sourceExecutionCourse.nome"/> (<fr:view name="bean" property="sourceExecutionCourse.sigla"/>)</strong></p>

<bean:define id="periodId" name="bean" property="sourceExecutionCourse.executionPeriod.idInternal"/>
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
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.ExecutionCourseBean" bundle="MANAGER_RESOURCES">
				<fr:slot name="executionSemester" layout="menu-select-postback" key="label.manager.executionCourseManagement.executionPeriod">
					<fr:property name="format" value="${qualifiedName}"/>
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider"/>
					<fr:property name="saveOptions" value="true"/>
				</fr:slot>
				<fr:slot name="destinationExecutionCourse" layout="menu-select" key="executionCourse.destination">
					<fr:property name="format" value="${nome} - ${sigla} - (${degreePresentationString})"/>
					<fr:property name="sortBy" value="nome"/>
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.manager.ExecutionCourseProvider"/>
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