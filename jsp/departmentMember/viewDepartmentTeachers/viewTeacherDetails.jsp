<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentMember.masterPage"
	attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources"
		var="bundleEnumeration" />

	<h:form>
		<h:inputHidden binding="#{viewDepartmentTeachers.selectedExecutionYearIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['label.teacher.details.title']}</h2>" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
		
		<!-- Personal Information -->
		<h:panelGrid columns="1" width="100%" cellpadding="0" cellspacing="0" columnClasses="infoop">
			<h:panelGrid columns="3">
				<h:outputText value="&nbsp;1&nbsp;" styleClass="emphasis-box" escape="false" />
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText value="<strong>#{bundle['label.teacher.details.personalInformation']}</strong>" escape="false" />
			</h:panelGrid>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />
		<h:panelGrid columns="3">
			<h:outputText value="#{bundle['label.teacher.number']}" />
			<h:outputText value="&nbsp;" escape="false" />
			<h:outputText
				value="#{viewDepartmentTeachers.selectedTeacher.teacherNumber}" />					
				
			<h:outputText value="#{bundle['label.teacher.name']}" />
			<h:outputText value="&nbsp;" escape="false" />
			<h:outputText
				value="#{viewDepartmentTeachers.selectedTeacher.infoPerson.nome}" />
				
			<h:outputText value="#{bundle['label.teacher.category']}" />
			<h:outputText value="&nbsp;" escape="false" />
			<h:outputText
				value="#{viewDepartmentTeachers.selectedTeacher.infoCategory.shortName}" />
		</h:panelGrid>
		
		<h:outputText value="<br/><br/>" escape="false" />

		<!-- Teaching Information -->
		<h:panelGrid columns="1" width="100%" cellpadding="0" cellspacing="0" columnClasses="infoop">
			<h:panelGrid columns="3">
				<h:outputText value="&nbsp;2&nbsp;" styleClass="emphasis-box" escape="false" />
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText value="<strong>#{bundle['label.teacher.details.lecturedCoursesInformation']}</strong>" escape="false" />
			</h:panelGrid>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Lectured Degree Courses -->
		<h:outputText value="#{bundle['label.common.degree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty viewDepartmentTeachers.lecturedDegreeExecutionCourses)}">
			<h:dataTable value="#{viewDepartmentTeachers.lecturedDegreeExecutionCourses}" var="lecturedCourse" columnClasses="listClasses" headerClass="listClasses-header">			
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseName']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseYear']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.executionYear.year}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseSemester']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.semester}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseDegrees']}" />
					</f:facet>
					<h:outputText value="#{viewDepartmentTeachers.lecturedDegreeExecutionCourseDegreeNames[lecturedCourse.idInternal]}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.lecturedDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Lectured Master Degree Courses -->
		<h:outputText value="#{bundle['label.common.masterDegree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses)}">
			<h:dataTable value="#{viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses}" var="lecturedCourse" columnClasses="listClasses" headerClass="listClasses-header">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseName']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseYear']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.executionYear.year}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseSemester']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.semester}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseDegrees']}" />
					</f:facet>
					<h:outputText value="#{viewDepartmentTeachers.lecturedMasterDegreeExecutionCourseDegreeNames[lecturedCourse.idInternal]}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:outputText value="<br/><br/>" escape="false" />
				
		<!-- Orientations Information -->
		<h:panelGrid columns="1" width="100%" cellpadding="0" cellspacing="0" columnClasses="infoop">
			<h:panelGrid columns="3">
				<h:outputText value="&nbsp;3&nbsp;" styleClass="emphasis-box" escape="false" />
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText value="<strong>#{bundle['label.teacher.details.orientationInformation']}</strong>" escape="false" />
			</h:panelGrid>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Final Degree Works -->
		<h:outputText value="#{bundle['label.common.finalDegreeWorks']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty viewDepartmentTeachers.finalDegreeWorks)}">
			<h:dataTable value="#{viewDepartmentTeachers.finalDegreeWorks}" var="finalDegreeWork" columnClasses="listClasses" headerClass="listClasses-header">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkTitle']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.title}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkOrientationPercentage']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.orientatorsCreditsPercentage}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkYear']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.executionDegree.executionYear.year}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkDegree']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.executionDegree.degreeCurricularPlan.degree.sigla}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.finalDegreeWorks}">
			<h:outputText value="#{bundle['label.common.noFinalDegreeWorks']}" />
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Master Degree Orientations -->
		<h:outputText value="#{bundle['label.common.masterDegree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty viewDepartmentTeachers.guidedMasterDegreeThesisList)}">
			<h:dataTable value="#{viewDepartmentTeachers.guidedMasterDegreeThesisList}" var="masterDegreeThesisDataVersion" columnClasses="listClasses" headerClass="listClasses-header">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.masterDegreeThesisTitle']}" />
					</f:facet>
					<h:outputText value="#{masterDegreeThesisDataVersion.dissertationTitle}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.masterDegreeThesisStudentName']}" />
					</f:facet>
					<h:outputText value="#{masterDegreeThesisDataVersion.masterDegreeThesis.studentCurricularPlan.student.person.nome}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.guidedMasterDegreeThesisList}">
			<h:outputText value="#{bundle['label.teacher.details.orientationInformation.noMasterDegreeThesis']}" />
		</h:panelGrid>
		
	</h:form>
</ft:tilesView>
