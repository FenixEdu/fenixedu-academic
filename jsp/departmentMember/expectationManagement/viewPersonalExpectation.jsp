<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources" var="bundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.title']}</h2>" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
	

	<h:form>
		<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearName}" />
				
		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['label.common.executionYear']}:" />
			<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{teacherExpectationManagement.selectedExecutionYearID}" onchange="this.form.submit();">
				<f:selectItems value="#{teacherExpectationManagement.executionYears}" />
			</fc:selectOneMenu>
		</h:panelGrid>
				
		<h:panelGrid rendered="#{teacherExpectationManagement.needsToCreateExpectation}">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.noExpectationsDefined']}" />
			<fc:commandLink value="#{bundle['link.personalExpectationsManagement.definePersonalExpectation']}" action="#{teacherExpectationManagement.prepareCreatePersonalExpectation}" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!teacherExpectationManagement.needsToCreateExpectation}">
			<!-- Education Expectations -->
			<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.education']}</h2>" escape="false" />
			
			<!-- Graduations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.graduations']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfGraduations']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />							
				<h:outputText value="#{teacherExpectationManagement.graduations}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.graduationsDescription}" />
			</h:panelGrid>

			<!-- Cientific Pos-Graduations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificPosGraduations']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfCientificPosGraduations']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.cientificPosGraduations}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.cientificPosGraduationsDescription}" />
			</h:panelGrid>
			
			<!-- Professional Pos-Graduations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalPosGraduations']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfProfessionalPosGraduations']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.professionalPosGraduations}" />			
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.professionalPosGraduationsDescription}" />
			</h:panelGrid>
			
			<!-- Seminaries -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.seminaries']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.numberOfSeminaries']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.seminaries}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.seminariesDescription}" />
								
			</h:panelGrid>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.educationMainFocus}" escape="false" />
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editEducationExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
					
			<!-- Investigation Expectations -->
			<!-- Investigation -->
			<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.investigation']}</h2>" escape="false" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.researchAndDevProjects']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.researchAndDevProjects}" />

				<h:outputText value="#{bundle['label.personalExpectationsManagement.jornalArticlePublications']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.jornalArticlePublications}"  />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.bookPublications']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.bookPublications}" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.conferencePublications']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.conferencePublications}" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.technicalReportPublications']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.technicalReportPublications}" />
			
				<h:outputText value="#{bundle['label.personalExpectationsManagement.patentPublications']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.patentPublications}" />
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublications']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.otherPublications}" />

				<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublicationsDescription']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.otherPublicationsDescription}" />
			</h:panelGrid>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.researchAndDevMainFocus}" escape="false" />	
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />
			
			<!-- Orientation  -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.orientation']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">

				<h:outputText value="#{bundle['label.personalExpectationsManagement.phdOrientations']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.phdOrientations}" />

				<h:outputText value="#{bundle['label.personalExpectationsManagement.masterDegreeOrientations']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.masterDegreeOrientations}" />

				<h:outputText value="#{bundle['label.personalExpectationsManagement.finalDegreeWorkOrientations']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.finalDegreeWorkOrientations}" />

			</h:panelGrid>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.orientationsMainFocus}" escape="false" />
			</h:panelGrid>			

			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editInvestigationExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>

			
			<h:outputText value="<br/>" escape="false" />
			
			<!-- University Service Expectations -->
			<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.universityService']}</h2>" escape="false" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.departmentOrgans']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.departmentOrgans}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.istOrgans']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.istOrgans}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.utlOrgans']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.utlOrgans}" />
			</h:panelGrid>

			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText id="universityServiceMainFocusText" value="#{teacherExpectationManagement.universityServiceMainFocus}" escape="false" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editUniversityServiceExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<!-- Professional Activity Expectations -->
			<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.professionalActivity']}</h2>" escape="false" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificComunityService']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.cientificComunityService}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.societyService']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.societyService}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.consulting']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.consulting}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.companySocialOrgans']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.companySocialOrgans}" />
				<h:outputText value="#{bundle['label.personalExpectationsManagement.companyPositions']}" />
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.companyPositions}" />
			</h:panelGrid>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="#{teacherExpectationManagement.professionalActivityMainFocus}" escape="false" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1">
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editProfessionalActivityExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
		</h:panelGrid>			
	</h:form>

</ft:tilesView>