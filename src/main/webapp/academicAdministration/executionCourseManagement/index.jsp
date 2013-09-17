<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.execution.course.management" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:form action="/executionCourseManagement.do">
	<fr:edit id="searchBean" name="searchBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseSearchBean" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="semester" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.ExecutionSemesterProvider" />
					<fr:property name="destination" value="chooseExecutionSemesterPostback" />
					<fr:property name="format" value="${name} - ${executionYear.name}" />
				</fr:slot>
			
			<logic:notEmpty name="searchBean" property="semester">
				<fr:slot name="degreeCurricularPlan" layout="menu-select-postback">
					<fr:property name="destination" value="chooseDegreeCurricularPlanPostback" />	
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DegreeCurricularPlanProvider" />
					<fr:property name="format" value="${degreeType.localizedName} - ${presentationName}" />
				</fr:slot>
			</logic:notEmpty>

			<logic:notEmpty name="searchBean" property="degreeCurricularPlan">
				<fr:slot name="curricularCourse" layout="menu-select-postback">	
					<fr:property name="destination" value="chooseCurricularCoursePostback" />
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.CurricularCoursesProvider" />
					<fr:property name="format" value="${name}" />
				</fr:slot>
			</logic:notEmpty>

		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear" />
		</fr:layout>
		
		<fr:destination name="chooseExecutionSemesterPostback" path="/executionCourseManagement.do?method=chooseExecutionSemesterPostback" />
		<fr:destination name="chooseDegreeCurricularPlanPostback" path="/executionCourseManagement.do?method=chooseDegreeCurricularPlanPostback" />
		<fr:destination name="chooseCurricularCoursePostback" path="/executionCourseManagement.do?method=chooseCurricularCoursePostback" />
		
	</fr:edit>
</fr:form>


<logic:empty name="searchBean" property="semester">
	<p><em><bean:message key="message.execution.course.management.choose.semester" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="searchBean" property="semester">
	<bean:define id="semesterId" name="searchBean" property="semester.externalId" />
	
	<p>
		<html:link action="<%= "/executionCourseManagement.do?method=prepareCreate&semesterId=" + semesterId %>">
			<bean:message key="link.execution.course.management.create" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</p>
	
	<p><strong><bean:message key="label.execution.course.management.execution.courses.list" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<logic:empty name="result">
		<p><em><bean:message key="message.execution.course.management.courses.result.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	</logic:empty>
	
	<logic:notEmpty name="result">
	
		<fr:view name="result">
			<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionCourse" bundle="ACADEMIC_OFFICE_RESOURCES">	
				<fr:slot name="nome" key="label.net.sourceforge.fenixedu.domain.ExecutionCourse.nome" />
				<fr:slot name="sigla" key="label.net.sourceforge.fenixedu.domain.ExecutionCourse.sigla" />
				<fr:slot name="competenceCourses" layout="null-as-label">
					<fr:property name="subSchema" value="CompetenceCourse.view.name" />
					<fr:property name="subLayout" value="values" />
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				
				<fr:link name="view" link="/executionCourseManagement.do?method=viewExecutionCourse&executionCourseId=${externalId}" label="label.view,APPLICATION_RESOURCES" />
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>
	
	<logic:notEmpty name="unattachedCurricularCourses">
		<p><strong><bean:message key="label.execution.course.management.courses.without.execution" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		
		<fr:view name="unattachedCurricularCourses">
			
			<fr:schema type="net.sourceforge.fenixedu.domain.CurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="nameI18N" />
				<fr:slot name="degreeCurricularPlan.degree.sigla" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				
				<fr:link name="create" label="label.create,APPLICATION_RESOURCES"
					link="<%= "/executionCourseManagement.do?method=createExecutionCourseForCurricularCourse&curricularCourseId=${externalId}&semesterId=" + semesterId %>" />
			</fr:layout>
			
		</fr:view> 
	</logic:notEmpty>

</logic:notEmpty>
