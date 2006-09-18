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
						
		<!-- Investigation Expectations -->
		<!-- Research And Dev -->
		<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.investigation']}</h2>" escape="false" />
		<h:panelGrid columns="2" columnClasses="aright valigntop infocell,infocell">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.researchAndDevProjects']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.researchAndDevProjects']}" id="researchAndDevProjects" value="#{teacherExpectationManagement.researchAndDevProjects}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="researchAndDevProjects" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.jornalArticlePublications']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.jornalArticlePublications']}" id="jornalArticlePublications" value="#{teacherExpectationManagement.jornalArticlePublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="jornalArticlePublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.bookPublications']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.bookPublications']}" id="bookPublications" value="#{teacherExpectationManagement.bookPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="bookPublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.conferencePublications']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.conferencePublications']}" id="conferencePublications" value="#{teacherExpectationManagement.conferencePublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="conferencePublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.technicalReportPublications']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.technicalReportPublications']}" id="technicalReportPublications" value="#{teacherExpectationManagement.technicalReportPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="technicalReportPublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.patentPublications']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.patentPublications']}" id="patentPublications" value="#{teacherExpectationManagement.patentPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="patentPublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublications']}" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.otherPublications']}" id="otherPublications" value="#{teacherExpectationManagement.otherPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="otherPublications" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublicationsDescription']}" />
			<fc:htmlEditor width="400" height="95" id="otherPublicationsDescription" value="#{teacherExpectationManagement.otherPublicationsDescription}" required="false" showButtons="false"/>
			<h:message for="otherPublicationsDescription" styleClass="error"/>
		</h:panelGrid>
			
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
			<fc:htmlEditor id="researchAndDevMainFocus" value="#{teacherExpectationManagement.researchAndDevMainFocus}" height="300" width="300" showButtons="true" />
		</h:panelGrid>
	
		<h:outputText value="<br/>" escape="false" />
								
		<h:outputText value="<p>#{bundle['label.personalExpectationsManagement.orientation']}</p>" style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
		<h:panelGrid columns="2" columnClasses="aright valigntop infocell,infocell" >
				<h:outputText value="#{bundle['label.personalExpectationsManagement.phdOrientations']}" />
				<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.phdOrientations']}" id="phdOrientations" value="#{teacherExpectationManagement.phdOrientations}" required="true" maxlength="3" size="3" />
					<h:outputText value="&nbsp;&nbsp;" escape="false"/>
				<h:message for="phdOrientations" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.masterDegreeOrientations']}" />
				<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.masterDegreeOrientations']}" id="masterDegreeOrientations" value="#{teacherExpectationManagement.masterDegreeOrientations}" required="true" maxlength="3" size="3" />
					<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="masterDegreeOrientations" styleClass="error"/>
				</h:panelGroup>
				
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Orientation -->
		<h:outputText value="#{bundle['label.common.finalDegreeWorks']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty teacherExpectationManagement.finalDegreeWorks)}">
			<h:dataTable value="#{teacherExpectationManagement.finalDegreeWorks}" var="finalDegreeWork" columnClasses="listClasses" headerClass="listClasses-header" style="width: 70%;">
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
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty teacherExpectationManagement.finalDegreeWorks}">
			<h:outputText value="#{bundle['label.common.noFinalDegreeWorks']}" />
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="4" columnClasses="aright valigntop infocell">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.finalDegreeWorkOrientations']}" />
			<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.finalDegreeWorkOrientations']}" id="finalDegreeWorkOrientations" value="#{teacherExpectationManagement.finalDegreeWorkOrientations}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
			<h:message for="finalDegreeWorkOrientations" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>
		
		
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
			<fc:htmlEditor value="#{teacherExpectationManagement.orientationsMainFocus}" height="300" width="300" showButtons="true" />
		</h:panelGrid>
		
		<!-- Education Expectations Transport Data -->
		<h:inputHidden id="graduations" value="#{teacherExpectationManagement.graduations}" />
		<h:inputHidden id="graduationsDescription" value="#{teacherExpectationManagement.graduationsDescription}" />
		<h:inputHidden id="cientificPosGraduations" value="#{teacherExpectationManagement.cientificPosGraduations}" />
		<h:inputHidden id="cientificPosGraduationsDescription" value="#{teacherExpectationManagement.cientificPosGraduationsDescription}" />
		<h:inputHidden id="professionalPosGraduations" value="#{teacherExpectationManagement.professionalPosGraduations}" />
		<h:inputHidden id="professionalPosGraduationsDescription" value="#{teacherExpectationManagement.professionalPosGraduationsDescription}" />
		<h:inputHidden id="seminaries" value="#{teacherExpectationManagement.seminaries}"  />
		<h:inputHidden id="seminariesDescription" value="#{teacherExpectationManagement.seminariesDescription}" />
		<h:inputHidden id="educationMainFocus" value="#{teacherExpectationManagement.educationMainFocus}" />

	
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
						
		<h:outputText value="<br/><br/>" escape="false" />
		<fc:commandButton action="#{teacherExpectationManagement.editPersonalExpectation}" value="#{bundle['button.update']}" styleClass="inputbutton" />
		
	</h:form>
</ft:tilesView>
