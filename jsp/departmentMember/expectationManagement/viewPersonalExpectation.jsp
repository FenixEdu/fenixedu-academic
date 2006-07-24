<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	
	<f:verbatim>
		<style>
			.header {
			background-color: #eee;
			padding: 0.5em;
			font-size: 1.4em;
			}
			.block {
			padding: 0 0.5em;
			}
			.indent {
			padding: 0 2em;
			}
			.limbottom li {
			padding-bottom: 8px;			
			
			}
		</style>
		
	</f:verbatim>
		
	<h:outputText value="<h2 class=\"invisible\">#{bundle['label.personalExpectationsManagement.title']}</h2>" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />

	<h:form>
		<fc:viewState binding="#{teacherExpectationManagement.viewState}"/>

		<h:panelGroup styleClass="invisible" rendered="#{!(empty teacherExpectationManagement.executionYears)}">
			<h:panelGrid columns="2">
				<h:outputText value="#{bundle['label.common.executionYear']}:" />
				<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{teacherExpectationManagement.selectedExecutionYearID}" onchange="this.form.submit();" valueChangeListener="#{teacherExpectationManagement.onExecutionYearChanged}">
					<f:selectItems value="#{teacherExpectationManagement.executionYears}" />
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			</h:panelGrid>
		</h:panelGroup>
		<h:panelGroup rendered="#{empty teacherExpectationManagement.executionYears}">
			<h:outputFormat styleClass="error" value="#{bundle['label.personalExpectationsManagement.noPeriodsDefinedByDeparment']}" />
		</h:panelGroup>
				
		<h:panelGrid rendered="#{teacherExpectationManagement.needsToCreateExpectation && teacherExpectationManagement.selectedExecutionYearID != null}">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.noExpectationsDefined']}" />
			<h:panelGroup rendered="#{teacherExpectationManagement.expectationDefinitionPeriodOpen}">
				<fc:commandLink value="#{bundle['link.personalExpectationsManagement.definePersonalExpectation']}" action="#{teacherExpectationManagement.prepareCreatePersonalExpectation}" />
			</h:panelGroup>
		</h:panelGrid>
		<h:panelGroup rendered="#{!teacherExpectationManagement.needsToCreateExpectation}">
			<!-- Education Expectations -->
			<h:outputText value="<p class='header'><strong>#{bundle['label.personalExpectationsManagement.education']}</strong></p>" escape="false" />
			
			<!-- Graduations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.graduations']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:outputText value="<ul>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.numberOfGraduations']}</i>" escape="false"/>
					<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />							
					<h:outputText value="#{teacherExpectationManagement.graduations}" escape="false"/>
				<h:outputText value="</li>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.description']}</i>" escape="false" />
					<h:outputText value="<br/>" escape="false" />
					<h:outputText value="#{teacherExpectationManagement.graduationsDescription}" escape="false" />
				<h:outputText value="</li>"  escape="false"/>
			<h:outputText value="</ul>" escape="false"/>

			<!-- Cientific Pos-Graduations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.cientificPosGraduations']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:outputText value="<ul>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.numberOfCientificPosGraduations']}</i>" escape="false" />
					<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
					<h:outputText value="#{teacherExpectationManagement.cientificPosGraduations}" />
				<h:outputText value="</li>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.description']}</i>" escape="false"/>
					<h:outputText value="<br/>" escape="false" />
					<h:outputText value="#{teacherExpectationManagement.cientificPosGraduationsDescription}" escape="false" />
				<h:outputText value="</li>"  escape="false"/>
			<h:outputText value="</ul>" escape="false"/>
			
			<!-- Professional Pos-Graduations -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.professionalPosGraduations']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:outputText value="<ul>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.numberOfProfessionalPosGraduations']}</i>" escape="false" />
					<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
					<h:outputText value="#{teacherExpectationManagement.professionalPosGraduations}" />
				<h:outputText value="</li>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.description']}</i>" escape="false" />
					<h:outputText value="<br/>" escape="false" />
					<h:outputText value="#{teacherExpectationManagement.professionalPosGraduationsDescription}" escape="false" />
				<h:outputText value="</li>"  escape="false"/>
			<h:outputText value="</ul>" escape="false"/>
			
			<!-- Seminaries -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.seminaries']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:outputText value="<ul>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.numberOfSeminaries']}</i>" escape="false" />
					<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
					<h:outputText value="#{teacherExpectationManagement.seminaries}" />
				<h:outputText value="</li>" escape="false"/>
				<h:outputText value="<li>" escape="false"/>
					<h:outputText value="<i>#{bundle['label.personalExpectationsManagement.description']}</i>" escape="false" />
					<h:outputText value="<br/>" escape="false" />
					<h:outputText value="#{teacherExpectationManagement.seminariesDescription}" escape="false" />
				<h:outputText value="</li>"  escape="false"/>
			<h:outputText value="</ul>" escape="false"/>
											
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="<div style=\"padding: 6px 0; padding-left: 40px;\">#{teacherExpectationManagement.educationMainFocus}</div>" escape="false" />
			</h:panelGrid>
			
			<h:panelGrid columns="1" rendered="#{teacherExpectationManagement.expectationDefinitionPeriodOpen}">
				<h:outputText value="<br/>" escape="false" />
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editEducationExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
					
			<!-- Investigation Expectations -->
			<!-- Investigation -->
			<h:outputText value="<p class='header'><strong>#{bundle['label.personalExpectationsManagement.investigation']}</strong></p>" escape="false" />
			<h:outputText value="<ul>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.researchAndDevProjects']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.researchAndDevProjects}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.jornalArticlePublications']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.jornalArticlePublications}</li>" escape="false" />								
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.bookPublications']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.bookPublications}</li>" escape="false"/>							
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.conferencePublications']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.conferencePublications}</li>" escape="false"/>
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.technicalReportPublications']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.technicalReportPublications}</li>" escape="false"/>				
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.patentPublications']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.patentPublications}</li>" escape="false"/>
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.otherPublications']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.otherPublications}</li>" escape="false"/>
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.otherPublicationsDescription']}</i><br/>#{teacherExpectationManagement.otherPublicationsDescription}</li>" escape="false"/>
			<h:outputText value="</ul>" escape="false" />
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="<div style=\"padding: 6px 0; padding-left: 40px;\">#{teacherExpectationManagement.researchAndDevMainFocus}</div>" escape="false" />
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />
			
			<!-- Orientation  -->
			<h:outputText value="#{bundle['label.personalExpectationsManagement.orientation']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
			<h:outputText value="<ul>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.phdOrientations']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.phdOrientations}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.masterDegreeOrientations']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.masterDegreeOrientations}" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.finalDegreeWorkOrientations']}</i>&nbsp;&nbsp;&nbsp;#{teacherExpectationManagement.finalDegreeWorkOrientations}" escape="false" />
			<h:outputText value="</ul>" escape="false"/>
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="<div style=\"padding: 6px 0; padding-left: 40px;\">#{teacherExpectationManagement.orientationsMainFocus}</div>" escape="false" />
			</h:panelGrid>			
		
			<h:panelGrid columns="1" rendered="#{teacherExpectationManagement.expectationDefinitionPeriodOpen}">
				<h:outputText value="<br/>" escape="false" />
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editInvestigationExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>

			
			<h:outputText value="<br/>" escape="false" />
			
			<!-- University Service Expectations -->
			<h:outputText value="<p class='header'><strong>#{bundle['label.personalExpectationsManagement.universityService']}</strong></p>" escape="false" />
			<h:outputText value="<ul class=\"limbottom\">" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.departmentOrgans']}</i><br/>#{teacherExpectationManagement.departmentOrgans}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.istOrgans']}</i><br/>#{teacherExpectationManagement.istOrgans}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.utlOrgans']}</i><br/>#{teacherExpectationManagement.utlOrgans}</li>" escape="false"/>
			<h:outputText value="</ul>" escape="false" />

			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="<div style=\"padding: 6px 0; padding-left: 40px;\">#{teacherExpectationManagement.universityServiceMainFocus}</div>" escape="false" />				
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1" rendered="#{teacherExpectationManagement.expectationDefinitionPeriodOpen}">
				<h:outputText value="<br/>" escape="false" />
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editUniversityServiceExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<!-- Professional Activity Expectations -->
			<h:outputText value="<p class='header'><strong>#{bundle['label.personalExpectationsManagement.professionalActivity']}</strong></p>" escape="false" />
			<h:outputText value="<ul class=\"limbottom\">" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.cientificComunityService']}</i><br/>#{teacherExpectationManagement.cientificComunityService}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.societyService']}</i><br/>#{teacherExpectationManagement.societyService}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.consulting']}</i><br/>#{teacherExpectationManagement.consulting}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.companySocialOrgans']}</i><br/>#{teacherExpectationManagement.companySocialOrgans}</li>" escape="false" />
				<h:outputText value="<li><i>#{bundle['label.personalExpectationsManagement.companyPositions']}</i><br/>#{teacherExpectationManagement.companyPositions}</li>" escape="false" />
			<h:outputText value="</ul>" escape="false" />
			
			<h:panelGrid columns="1">
				<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				<h:outputText value="<div style=\"padding: 6px 0; padding-left: 40px;\">#{teacherExpectationManagement.professionalActivityMainFocus}</div>" escape="false" />				
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<h:panelGrid columns="1" rendered="#{teacherExpectationManagement.expectationDefinitionPeriodOpen}">
				<h:outputText value="<br/>" escape="false" />
				<fc:commandLink actionListener="#{teacherExpectationManagement.preparePersonalExpectationForEdit}" action="editProfessionalActivityExpectationData" value="#{bundle['link.edit']}" />
			</h:panelGrid>
			
		</h:panelGroup>
	</h:form>

</ft:tilesView>