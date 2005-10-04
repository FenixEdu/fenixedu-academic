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
		
			<!-- Education Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.education']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:outputText value="<br/>" escape="false" />
			<h:panelGrid columns="7">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.graduations']}" />
				<h:inputText id="graduations" value="#{teacherExpectationManagement.graduations}" required="true" maxlength="3" size="3" />
				<h:message for="graduations" styleClass="error"/>
				<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false"/>
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:inputTextarea id="graduationsDescription" value="#{teacherExpectationManagement.graduationsDescription}" />
				<h:message for="graduationsDescription" styleClass="error"/>

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
			
			<h:outputText value="<br/>" escape="false" />
			<h:outputText value="<br/>" escape="false" />
						
			<!-- Investigation Expectations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.investigation']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:outputText value="<br/>" escape="false" />
			<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.researchAndDevProjects']}" />
				<h:inputText id="researchAndDevProjects" value="#{teacherExpectationManagement.researchAndDevProjects}" required="true" maxlength="3" size="3" />
				<h:message for="researchAndDevProjects" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.jornalArticlePublications']}" />
				<h:inputText id="jornalArticlePublications" value="#{teacherExpectationManagement.jornalArticlePublications}" required="true" maxlength="3" size="3" />
				<h:message for="jornalArticlePublications" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.bookPublications']}" />
				<h:inputText id="bookPublications" value="#{teacherExpectationManagement.bookPublications}" required="true" maxlength="3" size="3" />
				<h:message for="bookPublications" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.conferencePublications']}" />
				<h:inputText id="conferencePublications" value="#{teacherExpectationManagement.conferencePublications}" required="true" maxlength="3" size="3" />
				<h:message for="conferencePublications" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.technicalReportPublications']}" />
				<h:inputText id="technicalReportPublications" value="#{teacherExpectationManagement.technicalReportPublications}" required="true" maxlength="3" size="3" />
				<h:message for="technicalReportPublications" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.patentPublications']}" />
				<h:inputText id="patentPublications" value="#{teacherExpectationManagement.patentPublications}" required="true" maxlength="3" size="3" />
				<h:message for="patentPublications" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublications']}" />
				<h:inputText id="otherPublications" value="#{teacherExpectationManagement.otherPublications}" required="true" maxlength="3" size="3" />
				<h:message for="otherPublications" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.description']}" />
				<h:inputTextarea id="otherPublicationsDescription" value="#{teacherExpectationManagement.otherPublicationsDescription}" />
				<h:message for="otherPublicationsDescription" styleClass="error"/>
		</h:panelGrid>
			
		<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<fc:htmlEditor id="researchAndDevMainFocus" value="#{teacherExpectationManagement.researchAndDevMainFocus}" height="300" width="300" designMode="true" />
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
								
		<h:outputText value="#{bundle['label.personalExpectationsManagement.orientation']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:outputText value="<br/>" escape="false" />
		<h:panelGrid columns="3">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.phdOrientations']}" />
				<h:inputText id="phdOrientations" value="#{teacherExpectationManagement.phdOrientations}" required="true" maxlength="3" size="3" />
				<h:message for="phdOrientations" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.masterDegreeOrientations']}" />
				<h:inputText id="masterDegreeOrientations" value="#{teacherExpectationManagement.masterDegreeOrientations}" required="true" maxlength="3" size="3" />
				<h:message for="masterDegreeOrientations" styleClass="error"/>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.finalDegreeWorkOrientations']}" />
				<h:inputText id="finalDegreeWorkOrientations" value="#{teacherExpectationManagement.finalDegreeWorkOrientations}" required="true" maxlength="3" size="3" />
				<h:message for="finalDegreeWorkOrientations" styleClass="error"/>
		</h:panelGrid>
		
		<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
				<fc:htmlEditor value="#{teacherExpectationManagement.orientationsMainFocus}" height="300" width="300" designMode="true" />
		</h:panelGrid>
			
		<h:outputText value="<br/>" escape="false" />
		<h:outputText value="<br/>" escape="false" />
			
		<!-- University Service Expectations -->
		<h:outputText value="#{bundle['label.personalExpectationsManagement.universityService']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:outputText value="<br/>" escape="false" />
		<h:panelGrid columns="3">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.departmentOrgans']}" />
			<h:inputTextarea id="departmentOrgans" value="#{teacherExpectationManagement.departmentOrgans}" required="true">
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="departmentOrgans" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.istOrgans']}" />
			<h:inputTextarea id="istOrgans" value="#{teacherExpectationManagement.istOrgans}" required="true">
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="istOrgans" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.utlOrgans']}" />
			<h:inputTextarea id="utlOrgans" value="#{teacherExpectationManagement.utlOrgans}" required="true">
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="utlOrgans" styleClass="error"/>
		</h:panelGrid>
					
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
			<fc:htmlEditor value="#{teacherExpectationManagement.universityServiceMainFocus}" height="300" width="300" designMode="true"/>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		<h:outputText value="<br/>" escape="false" />
			
		<!-- Professional Activity Expectations -->
		<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalActivity']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:outputText value="<br/>" escape="false" />
		<h:panelGrid columns="3">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificComunityService']}" />
			<h:inputTextarea id="cientificComunityService" value="#{teacherExpectationManagement.cientificComunityService}" required="true">
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="cientificComunityService" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.societyService']}" />
			<h:inputTextarea id="societyService" value="#{teacherExpectationManagement.societyService}" required="true" >
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="societyService" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.consulting']}" />
			<h:inputTextarea id="consulting" value="#{teacherExpectationManagement.consulting}" required="true" >
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="consulting" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.companySocialOrgans']}" />
			<h:inputTextarea id="companySocialOrgans" value="#{teacherExpectationManagement.companySocialOrgans}" required="true" >
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="companySocialOrgans" styleClass="error"/>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.companyPositions']}" />
			<h:inputTextarea id="companyPositions" value="#{teacherExpectationManagement.companyPositions}" required="true" >
				<f:validateLength minimum="0" maximum="250" />
			</h:inputTextarea>
			<h:message for="companyPositions" styleClass="error"/>
			
		</h:panelGrid>
		
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" />
			<fc:htmlEditor value="#{teacherExpectationManagement.professionalActivityMainFocus}" height="300" width="300" designMode="true"/>
		</h:panelGrid>
		
						
		<h:outputText value="<br/>" escape="false" />
		<h:outputText value="<br/>" escape="false" />
		<h:commandButton action="#{teacherExpectationManagement.savePersonalExpectation}" value="#{bundle['button.save']}" styleClass="inputbutton" />
				
	</h:form>
</ft:tilesView>