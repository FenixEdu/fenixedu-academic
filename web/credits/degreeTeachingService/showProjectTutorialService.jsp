<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.credits.projectsAndTutorials" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<logic:present name="projectTutorialService">
	<fr:view name="projectTutorialService" property="professorship" layout="values">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.Professorship">
			<fr:slot name="executionCourse.nome"/>
			<fr:slot name="degreeSiglas"/>
		</fr:schema>
	</fr:view>

	<h3 class="separator2 mtop2"><bean:message key="label.availableOrientations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<fr:edit id="projectTutorialService" name="projectTutorialService" action="/degreeProjectTutorialService.do?method=updateProjectTutorialService">
		<fr:schema type="net.sourceforge.fenixedu.domain.credits.util.ProjectTutorialServiceBean" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
			<fr:slot name="orientations" layout="option-select" key="label.availableOrientations">
		       	<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse.AvailableAttendsForProjectTutorialServiceProvider"/>
	       		<fr:property name="eachSchema" value="edit.projectTutorialService"/>
	       		<fr:property name="eachLayout" value="values"/>
	       		<fr:property name="selectAllShown" value="true"/>
	       	
					<fr:property name="listItemClasses" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					<fr:property name="listItemStyle" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					<fr:property name="eachClasses" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					<fr:property name="eachStyle" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					
					
			</fr:slot>
		</fr:schema>
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
		</fr:layout>
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
