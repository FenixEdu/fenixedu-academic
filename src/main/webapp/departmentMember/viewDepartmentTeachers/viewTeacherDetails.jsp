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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
		
	<h:form>
		<fc:viewState binding="#{viewDepartmentTeachers.viewState}"/>
				
		<h:outputText value="<h2>#{bundle['label.teacher.details.title']}</h2>" escape="false" />
				
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}:" styleClass="leftColumn"/>
			<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{viewDepartmentTeachers.selectedExecutionYearID}" valueChangeListener="#{viewDepartmentTeachers.onSelectedExecutionYearChanged}" onchange="this.form.submit();">
				<f:selectItems value="#{viewDepartmentTeachers.executionYears}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:outputText value="<ul>" escape="false"/>
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#personalInformation">
					<h:outputText escape="false" value="#{bundle['label.teacher.details.personalInformation']}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#lecturedCoursesInformation">
					<h:outputText escape="false" value="#{bundle['label.teacher.details.lecturedCoursesInformation']}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#orientationInformation">
					<h:outputText escape="false" value="#{bundle['label.teacher.details.orientationInformation']}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#functionsInformation">
					<h:outputText escape="false" value="#{bundle['label.teacher.details.functionsInformation']}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
				
		<!-- Personal Information -->
		<h:outputText value="<h2 id='personalInformation' class='cd_heading'><span>#{bundle['label.teacher.details.personalInformation']}</span></h2>" escape="false" />
		<h:outputText value="<ul style=\"list-style: none;\">" escape="false" />
			<h:outputText value="<li>" escape="false"/>				
				<h:outputText value="<strong>#{bundle['label.teacher.name']}:</strong>"  escape="false"/>
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText
					value="#{viewDepartmentTeachers.selectedTeacher.infoPerson.nome}" />
			<h:outputText value="</li>" escape="false"/>
			<h:outputText value="<li>" escape="false" />
				<h:outputText value="<strong>#{bundle['label.teacher.number']}:</strong>" escape="false"/>
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText
					value="#{viewDepartmentTeachers.selectedTeacher.teacherId}" />
			<h:outputText value="</li>" escape="false"/>			
			<h:outputText value="<li>" escape="false"/>
				<h:outputText value="<strong>#{bundle['label.teacher.category']}:</strong>"  escape="false"/>
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText
					value="#{viewDepartmentTeachers.selectedTeacher.infoCategory.name}" />
			<h:outputText value="</li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
		
		<!-- Lectured Courses Information -->
		<h:outputText value="<h2 id='lecturedCoursesInformation' class='cd_heading'><span>#{bundle['label.teacher.details.lecturedCoursesInformation']}</span></h2>" escape="false" />
	
		<!-- Lectured Degree Courses -->
		<h:outputText value="#{bundle['label.common.degree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.lecturedDegreeExecutionCourses)}">
			<h:dataTable value="#{viewDepartmentTeachers.lecturedDegreeExecutionCourses}" var="lecturedCourse" styleClass="cd">
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
					<h:outputText value="#{viewDepartmentTeachers.lecturedDegreeExecutionCourseDegreeNames[lecturedCourse.externalId]}" />
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.lecturedDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Lectured Master Degree Courses -->
		<h:outputText value="#{bundle['label.common.masterDegree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses)}">
			<h:dataTable value="#{viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses}" var="lecturedCourse" styleClass="cd">
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
					<h:outputText value="#{viewDepartmentTeachers.lecturedMasterDegreeExecutionCourseDegreeNames[lecturedCourse.externalId]}" />
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
						
		<!-- Orientations Information -->
		<h:outputText value="<h2 id='orientationInformation' class='cd_heading'><span>#{bundle['label.teacher.details.orientationInformation']}</span></h2>" escape="false" />
		
		<!-- Final Degree Work Orientations -->
		<h:outputText value="#{bundle['label.common.finalDegreeWorks']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.finalDegreeWorkAdvises)}">
			<h:dataTable value="#{viewDepartmentTeachers.finalDegreeWorkAdvises}" var="finalDegreeWorkAdvise" styleClass="cd">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.finalDegreeWorkStudentNumber']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWorkAdvise.student.number}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.finalDegreeWorkStudentName']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWorkAdvise.student.person.name}" />
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.finalDegreeWorkAdvises}">
			<h:outputText value="#{bundle['label.teacher.details.orientationInformation.noFinalDegreeWorks']}" />
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
			
		<!-- Master Degree Orientations -->
		<h:outputText value="#{bundle['label.common.masterDegree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.guidedMasterDegreeThesisList)}">
			<h:dataTable value="#{viewDepartmentTeachers.guidedMasterDegreeThesisList}" var="masterDegreeThesisDataVersion" styleClass="cd">
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
					<h:outputText value="#{masterDegreeThesisDataVersion.masterDegreeThesis.studentCurricularPlan.student.person.name}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.masterDegreeProofDate']}" />
					</f:facet>
					<h:panelGroup rendered="#{(masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion != null) && (masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion.proofDate != null)}">
						<h:outputFormat value="{0, date, dd / MM / yyyy}" escape="false">
							<f:param value="#{masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion.proofDate}" />
						</h:outputFormat>
					</h:panelGroup>
					<h:panelGroup rendered="#{(masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion == null) || (masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion.proofDate == null)}">
						<h:outputText value="#{bundle['label.common.notAvailable']}"></h:outputText>
					</h:panelGroup>
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.guidedMasterDegreeThesisList}">
			<h:outputText value="#{bundle['label.teacher.details.orientationInformation.noMasterDegreeThesis']}" />
		</h:panelGrid>
				
		<!-- Functions -->
		<h:outputText value="<h2 id='functionsInformation' class='cd_heading'><span>#{bundle['label.teacher.details.functionsInformation']}</span></h2>" escape="false" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.teacherFunctions)}">
			<h:dataTable value="#{viewDepartmentTeachers.teacherFunctions}" var="teacherFunction" styleClass="cd">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.functionsInformation.functionName']}" />
					</f:facet>
					<h:outputText value="#{teacherFunction.function.name}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.functionsInformation.startDate']}" />
					</f:facet>
					<h:panelGroup rendered="#{teacherFunction.beginDateInDateType != null}">
						<h:outputFormat value="{0, date, dd / MM / yyyy}" escape="false">
							<f:param value="#{teacherFunction.beginDateInDateType}" />
						</h:outputFormat>
					</h:panelGroup>
					<h:panelGroup rendered="#{teacherFunction.beginDateInDateType == null}">
						<h:outputText value="#{bundle['label.common.notAvailable']}" />
					</h:panelGroup>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.functionsInformation.endDate']}" />
					</f:facet>
					<h:panelGroup rendered="#{teacherFunction.endDateInDateType != null}">
						<h:outputFormat value="{0, date, dd / MM / yyyy}" escape="false">
							<f:param value="#{teacherFunction.endDateInDateType}" />
						</h:outputFormat>
					</h:panelGroup>
					<h:panelGroup rendered="#{teacherFunction.endDateInDateType == null}">
						<h:outputText value="#{bundle['label.common.notAvailable']}" />
					</h:panelGroup>
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.teacherFunctions}">
			<h:outputText value="#{bundle['label.teacher.details.functionsInformation.noFunctions']}" />
		</h:panelGrid>
		
	</h:form>
</f:view>
