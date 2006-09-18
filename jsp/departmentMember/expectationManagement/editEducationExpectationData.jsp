<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentMemberResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="#{bundle['label.personalExpectationsManagement.title']}" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
	<h:outputText value="&nbsp;-&nbsp;" escape="false" />
	<h:outputText value="#{teacherExpectationManagement.selectedExecutionYearName}" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />

	<h:outputText value="<div class='simpleblock1'>" escape="false" />
 	   <h:outputText value="<h4 class='first'>#{bundle['label.personalExpectationsManagement.message.generalInformationTitle']}</h4>" escape="false"/>
 	   <h:outputText value="#{bundle['label.personalExpectationsManagement.message.generalInformationDescription']}" escape="false"/>
	<h:outputText value="</div>" escape="false"/>
	
	
	<h:form onsubmit="updateRTEs();">
		<h:inputHidden binding="#{teacherExpectationManagement.selectedExecutionYearIdHidden}"  />
		<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearName}" />
		<h:inputHidden value="#{teacherExpectationManagement.teacherPersonalExpectationID}" />
	
		<!-- Education Expectations -->
		<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.education']}</h2>" escape="false" />
		
		<!-- Graduations -->
		<h:outputText value="#{bundle['label.common.lecturedDegreeCourses']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty teacherExpectationManagement.lecturedDegreeExecutionCourses)}">
			<h:dataTable value="#{teacherExpectationManagement.lecturedDegreeExecutionCourses}" var="lecturedCourse" columnClasses="listClasses" headerClass="listClasses-header" style="width: 70%;">
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
		</h:panelGroup>
		<h:panelGrid rendered="#{empty teacherExpectationManagement.lecturedDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2" columnClasses="aright valigntop infocell,infocell,errorColumn" border="0" style="padding: 0.5em; ">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfGraduations']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.graduations']}" id="graduations" value="#{teacherExpectationManagement.graduations}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false"/>
				<h:message for="graduations" styleClass="error"/>
			</h:panelGroup>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:panelGroup>
				<fc:htmlEditor width="400" height="95" id="graduationsDescription" value="#{teacherExpectationManagement.graduationsDescription}" required="false" showButtons="false"/>
				<h:message for="graduationsDescription" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>	
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Cientific Pos-Graduations -->
		<h:outputText value="#{bundle['label.common.lecturedMasterDegreeCourses']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty teacherExpectationManagement.lecturedMasterDegreeExecutionCourses)}">
			<h:dataTable value="#{teacherExpectationManagement.lecturedMasterDegreeExecutionCourses}" var="lecturedCourse" columnClasses="listClasses" headerClass="listClasses-header" style="width: 70%;">
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
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty teacherExpectationManagement.lecturedMasterDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2" columnClasses="aright valigntop infocell,infocell,errorColumn" border="0" style="padding: 0.5em; ">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfCientificPosGraduations']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.cientificPosGraduations']}" id="cientificPosGraduations" value="#{teacherExpectationManagement.cientificPosGraduations}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false"/>
				<h:message for="cientificPosGraduations" styleClass="error"/>
			</h:panelGroup>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:panelGroup>
				<fc:htmlEditor width="400" height="95" id="cientificPosGraduationsDescription" value="#{teacherExpectationManagement.cientificPosGraduationsDescription}" required="false" showButtons="false"/>
				<h:message for="cientificPosGraduationsDescription" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Professional Pos-Graduations -->
		<h:outputText value="<p>#{bundle['label.personalExpectationsManagement.professionalPosGraduations']}</p>" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
		<h:panelGrid columns="2" columnClasses="aright valigntop infocell,infocell,errorColumn" border="0" style="padding: 0.5em; ">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfProfessionalPosGraduations']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.professionalPosGraduations']}" id="professionalPosGraduations" value="#{teacherExpectationManagement.professionalPosGraduations}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false"/>
				<h:message for="professionalPosGraduations" styleClass="error"/>
			</h:panelGroup>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:panelGroup>
				<fc:htmlEditor width="400" height="95" id="professionalPosGraduationsDescription" value="#{teacherExpectationManagement.professionalPosGraduationsDescription}" required="false" showButtons="false"/>
				<h:message for="professionalPosGraduationsDescription" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Seminaries -->
		<h:outputText value="<p>#{bundle['label.personalExpectationsManagement.seminaries']}</p>" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
		<h:panelGrid columns="2" columnClasses="aright valigntop infocell,infocell,errorColumn" border="0" style="padding: 0.5em; ">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfSeminaries']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.seminaries']}" id="seminaries" value="#{teacherExpectationManagement.seminaries}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false"/>
				<h:message for="seminaries" styleClass="error"/>
			</h:panelGroup>
			<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
			<h:panelGroup>
				<fc:htmlEditor width="400" height="95" id="seminariesDescription" value="#{teacherExpectationManagement.seminariesDescription}" required="false" showButtons="false"/>
				<h:message for="seminariesDescription" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
			<fc:htmlEditor id="educationMainFocus" value="#{teacherExpectationManagement.educationMainFocus}" height="300" width="300" showButtons="true"/>
		</h:panelGrid>
		
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
		
		<!-- University Service Expectations Transport Data -->
		<h:inputHidden id="departmentOrgans" value="#{teacherExpectationManagement.departmentOrgans}" />
		<h:inputHidden id="istOrgans" value="#{teacherExpectationManagement.istOrgans}" />
		<h:inputHidden id="utlOrgans" value="#{teacherExpectationManagement.utlOrgans}" />
		<h:inputHidden id="universityServiceMainFocus" value="#{teacherExpectationManagement.universityServiceMainFocus}" />
																
		<!-- Professional Activity Expectations -->
		<h:inputHidden id="cientificComunityService" value="#{teacherExpectationManagement.cientificComunityService}" />
		<h:inputHidden id="societyService" value="#{teacherExpectationManagement.societyService}" />
		<h:inputHidden id="consulting" value="#{teacherExpectationManagement.consulting}" />
		<h:inputHidden id="companySocialOrgans" value="#{teacherExpectationManagement.companySocialOrgans}"  />
		<h:inputHidden id="companyPositions" value="#{teacherExpectationManagement.companyPositions}"  />
		<h:inputHidden value="#{teacherExpectationManagement.professionalActivityMainFocus}" />
								
		<h:outputText value="<br/>" escape="false" />
		<h:outputText value="<br/>" escape="false" />
		
		<fc:commandButton action="#{teacherExpectationManagement.editPersonalExpectation}" value="#{bundle['button.update']}" styleClass="inputbutton">
			<f:param value="1" name="xpto" />
		</fc:commandButton>
			
	</h:form>
</ft:tilesView>
