<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources" var="bundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="<h2>" escape="false" />
	<h:outputText value="#{bundle['label.personalExpectationsManagement.title']}" escape="false" />
	<h:outputText value="&nbsp;-&nbsp;" escape="false" />
	<h:outputText value="#{teacherExpectationManagement.selectedExecutionYearName}" escape="false" />
	<h:outputText value="</h2>" escape="false" />

	<h:form>
		<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearID}" />
		<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearName}" />
		
		<h:panelGrid rendered="#{teacherExpectationManagement.needsToCreateExpectation}">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.noExpectationsDefined']}" />
			<h:commandLink value="#{bundle['link.personalExpectationsManagement.definePersonalExpectation']}" action="#{teacherExpectationManagement.prepareCreatePersonalExpectation}" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!teacherExpectationManagement.needsToCreateExpectation}">
			<!-- Education Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.education']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="5">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.graduations']}" />
				<h:outputText value="#{teacherExpectationManagement.graduations}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />							
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="#{teacherExpectationManagement.graduationsDescription}" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificPosGraduations']}" />
				<h:outputText value="#{teacherExpectationManagement.cientificPosGraduations}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />							
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="#{teacherExpectationManagement.cientificPosGraduationsDescription}" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalPosGraduations']}" />
				<h:outputText value="#{teacherExpectationManagement.professionalPosGraduations}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="#{teacherExpectationManagement.professionalPosGraduationsDescription}" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.seminaries']}" />
				<h:outputText value="#{teacherExpectationManagement.seminaries}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="#{teacherExpectationManagement.seminariesDescription}" />
								
			</h:panelGrid>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:outputText value="#{teacherExpectationManagement.educationMainFocus}" escape="false" />
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<h:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editEducationExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
			<h:outputText value="<br/><br/>" escape="false" />
					
			<!-- Investigation Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.investigation']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="5">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.researchAndDevProjects']}" />
				<h:outputText value="#{teacherExpectationManagement.researchAndDevProjects}" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />

				<h:outputText value="#{bundle['label.personalExpectationsManagement.jornalArticlePublications']}" />
				<h:outputText value="#{teacherExpectationManagement.jornalArticlePublications}"  />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.bookPublications']}" />
				<h:outputText value="#{teacherExpectationManagement.bookPublications}" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.conferencePublications']}" />
				<h:outputText value="#{teacherExpectationManagement.conferencePublications}" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.technicalReportPublications']}" />
				<h:outputText value="#{teacherExpectationManagement.technicalReportPublications}" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.patentPublications']}" />
				<h:outputText value="#{teacherExpectationManagement.patentPublications}" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				<h:outputText value="" escape="false" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublications']}" />
				<h:outputText value="#{teacherExpectationManagement.otherPublications}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="#{teacherExpectationManagement.otherPublicationsDescription}" />
				
			</h:panelGrid>
	
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:outputText value="#{teacherExpectationManagement.researchAndDevMainFocus}" escape="false" />	
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />

			<h:outputText value="#{bundle['label.personalExpectationsManagement.orientation']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="2">

				<h:outputText value="#{bundle['label.personalExpectationsManagement.phdOrientations']}" />
				<h:outputText value="#{teacherExpectationManagement.phdOrientations}" />

				<h:outputText value="#{bundle['label.personalExpectationsManagement.masterDegreeOrientations']}" />
				<h:outputText value="#{teacherExpectationManagement.masterDegreeOrientations}" />

				<h:outputText value="#{bundle['label.personalExpectationsManagement.finalDegreeWorkOrientations']}" />
				<h:outputText value="#{teacherExpectationManagement.finalDegreeWorkOrientations}" />

			</h:panelGrid>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:outputText value="#{teacherExpectationManagement.orientationsMainFocus}" escape="false" />
			</h:panelGrid>			

			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<h:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editInvestigationExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>

			
			<h:outputText value="<br/><br/>" escape="false" />
			
			<!-- University Service Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.universityService']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="2">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.departmentOrgans']}" />
				<h:outputText value="#{teacherExpectationManagement.departmentOrgans}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.istOrgans']}" />
				<h:outputText value="#{teacherExpectationManagement.istOrgans}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.utlOrgans']}" />
				<h:outputText value="#{teacherExpectationManagement.utlOrgans}" />
			</h:panelGrid>

			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:outputText id="universityServiceMainFocusText" value="#{teacherExpectationManagement.universityServiceMainFocus}" escape="false" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<h:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editUniversityServiceExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
			<h:outputText value="<br/><br/>" escape="false" />
			
			<!-- Professional Activity Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalActivity']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="2">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificComunityService']}" />
				<h:outputText value="#{teacherExpectationManagement.cientificComunityService}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.societyService']}" />
				<h:outputText value="#{teacherExpectationManagement.societyService}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.consulting']}" />
				<h:outputText value="#{teacherExpectationManagement.consulting}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.companySocialOrgans']}" />
				<h:outputText value="#{teacherExpectationManagement.companySocialOrgans}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.companyPositions']}" />
				<h:outputText value="#{teacherExpectationManagement.companyPositions}" />
			</h:panelGrid>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:outputText value="#{teacherExpectationManagement.professionalActivityMainFocus}" escape="false" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<h:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editProfessionalActivityExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
		</h:panelGrid>			
	</h:form>

</ft:tilesView>