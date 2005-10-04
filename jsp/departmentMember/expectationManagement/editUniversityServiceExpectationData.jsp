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
		<h:inputHidden value="#{teacherExpectationManagement.teacherPersonalExpectationID}" />

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
		
		<!-- Professional Activity Expectations -->
		<h:inputHidden id="cientificComunityService" value="#{teacherExpectationManagement.cientificComunityService}" />
		<h:inputHidden id="societyService" value="#{teacherExpectationManagement.societyService}" />
		<h:inputHidden id="consulting" value="#{teacherExpectationManagement.consulting}" />
		<h:inputHidden id="companySocialOrgans" value="#{teacherExpectationManagement.companySocialOrgans}"  />
		<h:inputHidden id="companyPositions" value="#{teacherExpectationManagement.companyPositions}"  />
		<h:inputHidden value="#{teacherExpectationManagement.professionalActivityMainFocus}" />
						
		<h:outputText value="<br/>" escape="false" />
		<h:outputText value="<br/>" escape="false" />
		<h:commandButton action="#{teacherExpectationManagement.editPersonalExpectation}" value="#{bundle['button.update']}" styleClass="inputbutton" />
		
	</h:form>
</ft:tilesView>
