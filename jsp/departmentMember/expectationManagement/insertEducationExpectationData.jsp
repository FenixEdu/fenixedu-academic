<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources" var="bundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="<h2>" escape="false" />
	<h:outputText value="#{bundle['label.personalExpectationsManagement.title']}" escape="false" />
	<h:outputText value="&nbsp;-&nbsp;" escape="false" />
	<h:outputText value="#{teacherExpectationManagement.selectedExecutionYearName}" escape="false" />
	<h:outputText value="</h2>" escape="false" />
	
	
	<h:form onsubmit="updateRTEs();">
		<h:inputHidden binding="#{teacherExpectationManagement.selectedExecutionYearIdHidden}"  />
		<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearName}" />
				
		
		<!-- Investigation Expectations Transport Data -->
		<h:inputHidden id="researchAndDevProjects" value="#{teacherExpectationManagement.researchAndDevProjects}" />
		<h:inputHidden id="jornalArticlePublications" value="#{teacherExpectationManagement.jornalArticlePublications}" />
		<h:inputHidden id="bookPublications" value="#{teacherExpectationManagement.bookPublications}" />
		<h:inputHidden id="conferencePublications" value="#{teacherExpectationManagement.conferencePublications}" />
		<h:inputHidden id="technicalReportPublications" value="#{teacherExpectationManagement.technicalReportPublications}" />
		<h:inputHidden id="patentPublications" value="#{teacherExpectationManagement.patentPublications}" />
		<h:inputHidden id="otherPublications" value="#{teacherExpectationManagement.otherPublications}" />
		<h:inputHidden id="otherPublicationsDescription" value="#{teacherExpectationManagement.otherPublicationsDescription}" />		
		<h:inputHidden id="researchAndDevMainFocus" value="#{teacherExpectationManagement.researchAndDevMainFocus}" />
	
		<h:inputHidden id="phdOrientations" value="#{teacherExpectationManagement.phdOrientations}" />
		<h:inputHidden id="masterDegreeOrientations" value="#{teacherExpectationManagement.masterDegreeOrientations}"  />
		<h:inputHidden id="finalDegreeWorkOrientations" value="#{teacherExpectationManagement.finalDegreeWorkOrientations}" />
		<h:inputHidden id="orientationsMainFocus" value="#{teacherExpectationManagement.orientationsMainFocus}" />
		
		<!-- Education Expectations -->
		<h:outputText value="#{bundle['label.personalExpectationsManagement.education']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:outputText value="<br/>" escape="false" />
		<h:outputText value="#{bundle['label.common.lecturedDegreeCourses']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty teacherExpectationManagement.lecturedDegreeExecutionCourses)}">
			<h:dataTable value="#{teacherExpectationManagement.lecturedDegreeExecutionCourses}" var="lecturedCourse" columnClasses="listClasses" headerClass="listClasses-header">			
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseName']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.nome}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseAcronym']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.sigla}" />
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
					<h:outputText value="#{teacherExpectationManagement.lecturedDegreeExecutionCourseDegreeNames[lecturedCourse.idInternal]}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty teacherExpectationManagement.lecturedDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:panelGrid columns="7" cellpadding="0" cellspacing="0">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.graduations']}" />
			<h:inputText id="graduations" value="#{teacherExpectationManagement.graduations}" required="true" maxlength="3" size="3" />
			<h:message for="graduations" styleClass="error"/>
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false"/>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:inputTextarea id="graduationsDescription" value="#{teacherExpectationManagement.graduationsDescription}" />
			<h:message for="graduationsDescription" styleClass="error"/>
		</h:panelGrid>
		
		<h:outputText value="#{bundle['label.common.lecturedMasterDegreeCourses']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty teacherExpectationManagement.lecturedMasterDegreeExecutionCourses)}">
			<h:dataTable value="#{teacherExpectationManagement.lecturedMasterDegreeExecutionCourses}" var="lecturedCourse" columnClasses="listClasses" headerClass="listClasses-header">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseName']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.nome}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseAcronym']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.sigla}" />
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
					<h:outputText value="#{teacherExpectationManagement.lecturedMasterDegreeExecutionCourseDegreeNames[lecturedCourse.idInternal]}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty teacherExpectationManagement.lecturedMasterDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:panelGrid border="0" columns="7" cellpadding="0" cellspacing="0">

			<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificPosGraduations']}" />
			<h:inputText id="cientificPosGraduations" value="#{teacherExpectationManagement.cientificPosGraduations}" required="true" maxlength="3" size="3" />
			<h:message for="cientificPosGraduations" styleClass="error"/>
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false"/>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:inputTextarea id="cientificPosGraduationsDescription" value="#{teacherExpectationManagement.cientificPosGraduationsDescription}" />
			<h:message for="cientificPosGraduationsDescription" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalPosGraduations']}" />
			<h:inputText id="professionalPosGraduations" value="#{teacherExpectationManagement.professionalPosGraduations}" required="true" maxlength="3" size="3" />
			<h:message for="professionalPosGraduations" styleClass="error"/>
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false"/>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:inputTextarea id="professionalPosGraduationsDescription" value="#{teacherExpectationManagement.professionalPosGraduationsDescription}" />
			<h:message for="professionalPosGraduationsDescription" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.seminaries']}" />
			<h:inputText id="seminaries" value="#{teacherExpectationManagement.seminaries}" required="true" maxlength="3" size="3" />
			<h:message for="seminaries" styleClass="error"/>
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false"/>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:inputTextarea id="seminariesDescription" value="#{teacherExpectationManagement.seminariesDescription}" />
			<h:message for="seminariesDescription" styleClass="error"/>
		</h:panelGrid>
		
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
			<fc:htmlEditor id="educationMainFocus" value="#{teacherExpectationManagement.educationMainFocus}" height="300" width="300" designMode="true"/>
		</h:panelGrid>
							
		<h:outputText value="<br/><br/>" escape="false" />
		<h:commandButton action="next" value="#{bundle['button.next']}" styleClass="inputbutton" />
	
	</h:form>
</ft:tilesView>