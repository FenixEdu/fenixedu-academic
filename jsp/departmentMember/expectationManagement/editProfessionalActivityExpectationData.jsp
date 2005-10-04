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
		
		<!-- University Service Expectations Transport Data -->
		<h:inputHidden id="departmentOrgans" value="#{teacherExpectationManagement.departmentOrgans}" />
		<h:inputHidden id="istOrgans" value="#{teacherExpectationManagement.istOrgans}" />
		<h:inputHidden id="utlOrgans" value="#{teacherExpectationManagement.utlOrgans}" />
		<h:inputHidden id="universityServiceMainFocus" value="#{teacherExpectationManagement.universityServiceMainFocus}" />
					
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
		<h:commandButton action="#{teacherExpectationManagement.editPersonalExpectation}" value="#{bundle['button.update']}" styleClass="inputbutton" />
		
	</h:form>
</ft:tilesView>
