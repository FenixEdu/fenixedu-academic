<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="label.credits.projectsAndTutorials" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<logic:present name="projectTutorialService">
	<jsp:include page="../teacherCreditsStyles.jsp"/>
	
	<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="projectTutorialService" property="professorship.teacher.person.username"/></bean:define>
	<table class="headerTable"><tr>
	<td><img src="<%= request.getContextPath() + url %>"/></td>
	<td >
		<fr:view name="projectTutorialService" property="professorship">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.Professorship">
				<fr:slot name="teacher.person.presentationName" key="label.name"/>
				<fr:slot name="this" key="label.course" layout="format">
					<fr:property name="format" value="${executionCourse.nome}  (${degreeSiglas})" />
				</fr:slot>
				<fr:slot name="executionCourse.executionPeriod" key="label.period" layout="format">
					<fr:property name="format" value="${name}  ${executionYear.year}" />
				</fr:slot>
				<fr:slot name="executionCourse.executionPeriod.executionYear.nextExecutionYear" key="label.credits" layout="format">
					<fr:property name="format" value="${year}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="creditsStyle"/>
			</fr:layout>
		</fr:view>
		</td>
	</tr></table>

	<bean:define id="executionYearOid" name="projectTutorialService" property="professorship.executionCourse.executionPeriod.executionYear.nextExecutionYear.externalId"/>
	<bean:define id="teacherOid" name="projectTutorialService" property="professorship.teacher.externalId"/>
	
	<h3 class="separator2 mtop2"><bean:message key="label.availableOrientations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
	<fr:edit id="projectTutorialService" name="projectTutorialService" action="/degreeProjectTutorialService.do?method=updateProjectTutorialService" schema="show.degreeProjectTutorialServiceOrientations">
		<fr:layout name="tabular-row">
			<fr:property name="displayHeaders" value="false"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"/>
	</fr:edit>

	
	<logic:notEmpty name="projectTutorialService" property="assignedOrientations">
		<h3 class="separator2 mtop2"><bean:message key="label.assignedOrientations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
		<fr:view name="projectTutorialService" property="assignedOrientations" schema="edit.projectTutorialService">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
