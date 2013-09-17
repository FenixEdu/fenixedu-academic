<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2>Gestão de curriculos de execução</h2>

<fr:form action="/executionDegreeManagement.do">
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-choose" name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.degree.execution.DegreeFilterBean" bundle="ACADEMIC_OFFICE_RESOURCES">
		
				<fr:slot name="degreeType" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.degree.execution.DegreeTypeProvider" />
					<fr:property name="destination" value="chooseDegreeTypePostback" /> 
				</fr:slot>
			
			<logic:notEmpty name="bean" property="degreeType">
				<fr:slot name="degree" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.degree.execution.DegreeProvider" />
					<fr:property name="destination" value="chooseDegreePostback" />
					<fr:property name="format" value="${nameI18N}" />
				</fr:slot>
			</logic:notEmpty>
			
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tsyle1" />
		</fr:layout>
		
		<fr:destination name="chooseDegreeTypePostback" path="/executionDegreeManagement.do?method=chooseDegreeTypePostback" />
		<fr:destination name="chooseDegreePostback" path="/executionDegreeManagement.do?method=chooseDegreePostback" />
		
	</fr:edit>
</fr:form>

<logic:empty name="bean" property="degree">
	<p><em>Escolha um curso para visualizar os curriculos de execução.</em></p>
</logic:empty>

<logic:notEmpty name="bean" property="degree">
	<bean:define id="degreeId" name="bean" property="degree.externalId" /> 
	
	<p>
		<html:link action="<%= String.format("/executionDegreeManagement.do?method=prepareCreateNewExecutionDegree&degreeId=%s", degreeId) %>">
			Criar currículo de execução
		</html:link>
	</p>


	<bean:define id="executionDegrees" name="bean" property="degree.executionDegrees" />

	<logic:empty name="">
		<em>O curso escolhido não tem curriculos de execução</em>
	</logic:empty>
	
	<logic:notEmpty name="executionDegrees">
	
		<fr:view name="executionDegrees">
		
			<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionDegree" bundle="MANAGER_RESOURCES">
				<fr:slot name="executionYear.name" key="label.manager.executionDegree.executionYear" bundle="MANAGER_RESOURCES" />
				
				<fr:slot name="periodLessonsFirstSemester" layout="format" key="label.manager.executionDegree.executionYear" bundle="MANAGER_RESOURCES" >
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
				
				<fr:slot name="periodLessonsFirstSemester" layout="format" key="label.manager.lessons.first.semester" bundle="MANAGER_RESOURCES" >
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
				
				
				<fr:slot name="periodExamsFirstSemester" layout="format" key="label.manager.exams.first.semester" bundle="MANAGER_RESOURCES" >
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
				
				
				<fr:slot name="periodLessonsSecondSemester" layout="format" key="label.manager.lessons.second.semester" bundle="MANAGER_RESOURCES">
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
	
				<fr:slot name="periodLessonsSecondSemester" layout="format" key="label.manager.exams.second.semester" bundle="MANAGER_RESOURCES">
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
	
				<fr:slot name="periodExamsSecondSemester" layout="format" key="label.manager.exams.second.semester" bundle="MANAGER_RESOURCES">
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
	
				<fr:slot name="periodExamsSpecialSeason" layout="format" key="label.manager.executionDegree.examsSpecialSeason" bundle="MANAGER_RESOURCES">
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
				
				<fr:slot name="periodGradeSubmissionNormalSeasonFirstSemester" layout="format" key="label.manager.executionDegree.gradeSubmissionNormalSeason1" bundle="MANAGER_RESOURCES">
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
	
				<fr:slot name="periodGradeSubmissionNormalSeasonSecondSemester" layout="format" key="label.manager.executionDegree.gradeSubmissionNormalSeason2" bundle="MANAGER_RESOURCES">
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
	
				<fr:slot name="periodGradeSubmissionSpecialSeason" layout="format" key="label.manager.executionDegree.gradeSubmissionSpecialSeason" bundle="MANAGER_RESOURCES">
					<fr:property name="format" value="${startYearMonthDay}<br/> - <br/>${endYearMonthDay}" />
					<fr:property name="escaped" value="false" />
				</fr:slot>
				
				<fr:slot name="temporaryExamMap" key="label.manager.executionDegree.temporaryExamMap" bundle="MANAGER_RESOURCES" />
				
				<fr:slot name="coordinatorsList" layout="null-as-label" key="label.manager.executionDegree.coordinator" bundle="MANAGER_RESOURCES" >
					<fr:property name="subSchema" value="coordinator.name" />
					<fr:property name="subLayout" value="values" />
				</fr:slot>

			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				
				<fr:link 	name="view" 
							label="label.view,APPLICATION_RESOURCES" 
							link="/executionDegreeManagement.do?method=viewExecutionDegree&executionDegreeId=${externalId}" />
				
				
			</fr:layout>
		</fr:view>
	
	</logic:notEmpty>
</logic:notEmpty>
