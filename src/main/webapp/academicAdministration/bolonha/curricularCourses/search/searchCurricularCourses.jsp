<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId" />
<bean:define id="dcpExternalId" name="degreeCurricularPlan" property="externalId" />
<bean:define id="executionYearId" name="currentExecutionYear" property="externalId" />

<h2><bean:message key="title.search.curricular.courses" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:form action="<%= String.format("/searchCurricularCourses.do?method=search&amp;dcpId=%s", dcpId) %>" >
	<fr:edit id="searchBean" name="searchBean" visible="false" />
	
	<fr:edit id="searchBean-form" name="searchBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.manager.curricularCourses.SearchCurricularCourseBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="name" required="true" bundle="ACADEMIC_OFFICE_RESOURCES"
					 key="label.net.sourceforge.fenixedu.presentationTier.Action.manager.curricularCourses.SearchCurricularCourseBean.name"/>
			<fr:slot name="beginExecutionYear" layout="menu-select" bundle="ACADEMIC_OFFICE_RESOURCES"
					 key="label.net.sourceforge.fenixedu.presentationTier.Action.manager.curricularCourses.SearchCurricularCourseBean.beginExecutionYear">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${name}" />
			</fr:slot>
			<fr:slot name="endExecutionYear" layout="menu-select" bundle="ACADEMIC_OFFICE_RESOURCES"
					 key="label.net.sourceforge.fenixedu.presentationTier.Action.manager.curricularCourses.SearchCurricularCourseBean.endExecutionYear">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${name}" />
			</fr:slot>
		</fr:schema>
	
		<fr:destination name="invalid" path="<%= String.format("/searchCurricularCourses.do?method=searchInvalid&amp;dcpId=%s", dcpId) %>" />

		<fr:layout name="tabular">
		</fr:layout>
	</fr:edit>
	
	
	<p><html:submit><bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit></p>
</fr:form>

<logic:empty name="results">
	<bean:message key="message.search.curricular.courses.results.empty" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>

<logic:notEmpty name="results">
	<fr:view name="results">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.degreeStructure.Context">
			<fr:slot name="childDegreeModule.nameI18N" />
			<fr:slot name="beginExecutionPeriod.executionYear.name" />
			<fr:slot name="beginExecutionPeriod.name" />
		</fr:schema>
		
		<fr:layout name="tabular">

			<fr:link 	name="edit" 
						link="<%= String.format("/bolonha/curricularPlans/editCurricularCourse.faces?degreeCurricularPlanID=%s&contextID=${externalId}&curricularCourseID=${childDegreeModule.externalId}&organizeBy=groups&showRules=false&hideCourses=false&action=build&executionYearID=%s", dcpExternalId, executionYearId) %>"
						label="label.edit,APPLICATION_RESOURCES"/>

		</fr:layout>
	</fr:view>
</logic:notEmpty>
