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
			<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearID}" />
			<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearName}" />
			<h:inputHidden value="#{teacherExpectationManagement.teacherPersonalExpectationID}" />
			
			<!-- Education Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.education']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.graduations']}" />
				<h:inputText id="graduationsText" value="#{teacherExpectationManagement.graduations}" required="true" maxlength="3" size="3" />
				<h:message for="graduationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificPosGraduations']}" />
				<h:inputText id="cientificPosGraduationsText" value="#{teacherExpectationManagement.cientificPosGraduations}" required="true" maxlength="3" size="3" />
				<h:message for="cientificPosGraduationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalPosGraduations']}" />
				<h:inputText id="professionalPosGraduationsText" value="#{teacherExpectationManagement.professionalPosGraduations}" required="true" maxlength="3" size="3" />
				<h:message for="professionalPosGraduationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.seminaries']}" />
				<h:inputText id="seminariesText" value="#{teacherExpectationManagement.seminaries}" required="true" maxlength="3" size="3" />
				<h:message for="seminariesText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:inputTextarea id="educationMainFocusText" value="#{teacherExpectationManagement.educationMainFocus}">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="educationMainFocusText" styleClass="error"/>
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />
			
			<!-- Investigation Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.investigation']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.researchAndDevProjects']}" />
				<h:inputText id="researchAndDevProjectsText" value="#{teacherExpectationManagement.researchAndDevProjects}" required="true" maxlength="3" size="3" />
				<h:message for="researchAndDevProjectsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.jornalArticlePublications']}" />
				<h:inputText id="jornalArticlePublicationsText" value="#{teacherExpectationManagement.jornalArticlePublications}" required="true" maxlength="3" size="3" />
				<h:message for="jornalArticlePublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.bookPublications']}" />
				<h:inputText id="bookPublicationsText" value="#{teacherExpectationManagement.bookPublications}" required="true" maxlength="3" size="3" />
				<h:message for="bookPublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.thesisPublications']}" />
				<h:inputText id="thesisPublicationsText" value="#{teacherExpectationManagement.thesisPublications}" required="true" maxlength="3" size="3" />
				<h:message for="thesisPublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.conferencePublications']}" />
				<h:inputText id="conferencePublicationsText" value="#{teacherExpectationManagement.conferencePublications}" required="true" maxlength="3" size="3" />
				<h:message for="conferencePublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.technicalReportPublications']}" />
				<h:inputText id="technicalReportPublicationsText" value="#{teacherExpectationManagement.technicalReportPublications}" required="true" maxlength="3" size="3" />
				<h:message for="technicalReportPublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.patentPublications']}" />
				<h:inputText id="patentPublicationsText" value="#{teacherExpectationManagement.patentPublications}" required="true" maxlength="3" size="3" />
				<h:message for="patentPublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.translationPublications']}" />
				<h:inputText id="translationPublicationsText" value="#{teacherExpectationManagement.translationPublications}" required="true" maxlength="3" size="3" />
				<h:message for="translationPublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.critiquePublications']}" />
				<h:inputText id="critiquePublicationsText" value="#{teacherExpectationManagement.critiquePublications}" required="true" maxlength="3" size="3" />
				<h:message for="critiquePublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.unstructuredPublications']}" />
				<h:inputText id="unstructuredPublicationsText" value="#{teacherExpectationManagement.unstructuredPublications}" required="true" maxlength="3" size="3" />
				<h:message for="unstructuredPublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublications']}" />
				<h:inputText id="otherPublicationsText" value="#{teacherExpectationManagement.otherPublications}" required="true" maxlength="3" size="3" />
				<h:message for="otherPublicationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.phdOrientations']}" />
				<h:inputText id="phdOrientationsText" value="#{teacherExpectationManagement.phdOrientations}" required="true" maxlength="3" size="3" />
				<h:message for="phdOrientationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.masterDegreeOrientations']}" />
				<h:inputText id="masterDegreeOrientationsText" value="#{teacherExpectationManagement.masterDegreeOrientations}" required="true" maxlength="3" size="3" />
				<h:message for="masterDegreeOrientationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.finalDegreeWorkOrientations']}" />
				<h:inputText id="finalDegreeWorkOrientationsText" value="#{teacherExpectationManagement.finalDegreeWorkOrientations}" required="true" maxlength="3" size="3" />
				<h:message for="finalDegreeWorkOrientationsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:inputTextarea id="investigationMainFocusText" value="#{teacherExpectationManagement.investigationMainFocus}">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="investigationMainFocusText" styleClass="error"/>
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<!-- University Service Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.universityService']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.departmentOrgans']}" />
				<h:inputTextarea id="departmentOrgansText" value="#{teacherExpectationManagement.departmentOrgans}" required="true">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="departmentOrgansText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.istOrgans']}" />
				<h:inputTextarea id="istOrgansText" value="#{teacherExpectationManagement.istOrgans}" required="true">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="istOrgansText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.utlOrgans']}" />
				<h:inputTextarea id="utlOrgansText" value="#{teacherExpectationManagement.utlOrgans}" required="true">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="utlOrgansText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:inputTextarea id="universityServiceMainFocusText" value="#{teacherExpectationManagement.universityServiceMainFocus}">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="universityServiceMainFocusText" styleClass="error"/>
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<!-- Professional Activity Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalActivity']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificComunityService']}" />
				<h:inputTextarea id="cientificComunityServiceText" value="#{teacherExpectationManagement.cientificComunityService}" required="true">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="cientificComunityServiceText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.societyService']}" />
				<h:inputTextarea id="societyServiceText" value="#{teacherExpectationManagement.societyService}" required="true" >
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="societyServiceText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.consulting']}" />
				<h:inputTextarea id="consultingText" value="#{teacherExpectationManagement.consulting}" required="true" >
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="consultingText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.companySocialOrgans']}" />
				<h:inputTextarea id="companySocialOrgansText" value="#{teacherExpectationManagement.companySocialOrgans}" required="true" >
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="companySocialOrgansText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.companyPositions']}" />
				<h:inputTextarea id="companyPositionsText" value="#{teacherExpectationManagement.companyPositions}" required="true" >
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="companyPositionsText" styleClass="error"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<h:inputTextarea id="professionalActivityMainFocusText" value="#{teacherExpectationManagement.professionalActivityMainFocus}">
					<f:validateLength minimum="0" maximum="250" />
				</h:inputTextarea>
				<h:message for="professionalActivityMainFocusText" styleClass="error"/>
			</h:panelGrid>
			
			<fc:htmlEditor id="cool" width="300" height="300" value="#{teacherExpectationManagement.investigationMainFocus}" required="true" />
			
			<h:outputText value="<br/>" escape="false" />
			<h:outputText value="<br/>" escape="false" />
			<h:commandButton action="#{teacherExpectationManagement.editPersonalExpectation}" value="#{bundle['button.update']}" styleClass="inputbutton" />
			
	</h:form>

</ft:tilesView>