<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	<h:form>
		<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/>
		<h2><h:outputText value="#{bolonhaBundle['competenceCoursesManagement']}"/></h2>		
		<h:messages infoClass="infoMsg" errorClass="error" layout="table"/>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.scientificAreaUnits}">
			<h:outputText style="font-style:italic" value="#{bolonhaBundle['noScientificAreas']}<br/>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
			<br/><style>@import url(<%= request.getContextPath() %>/CSS/dotist_degreeStructure.css);</style>
			<fc:dataRepeater value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit">
				<h:outputText value="> #{scientificAreaUnit.name}<br/>" style="font-weight: bold" escape="false"/>
				<fc:dataRepeater value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
					<h:outputText value="<br/>" escape="false"/>
					<h:outputText value="#{competenceCourseGroupUnit.name}<br/>" escape="false" style="margin-left: 3em;"/>
					<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
						styleClass="style2 indent1" footerClass="style2 aright">
						<h:column>
							<h:outputLink value="showCompetenceCourse.faces">
								<h:outputText value="#{competenceCourse.name}"/>
								<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
							</h:outputLink>
							<h:outputText value="  (#{enumerationBundle[competenceCourse.curricularStage]})" style="font-style:italic"/>
						</h:column>
						<h:column>
							<h:panelGroup rendered="#{competenceCourse.curricularStage.name != 'APPROVED'}">
								<h:outputLink value="editCompetenceCourseMainPage.faces">
									<h:outputText value="#{bolonhaBundle['edit']}" />
									<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
								</h:outputLink>
								<h:outputText value=", "/>
							</h:panelGroup>							
							<h:outputLink value="deleteCompetenceCourse.faces">
								<h:outputText value="#{bolonhaBundle['delete']}" />
								<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
							</h:outputLink>
						</h:column>
						<f:facet name="footer">
							<h:outputLink value="createCompetenceCourse.faces">
								<h:outputFormat value="#{bolonhaBundle['create.param']}<br/>" escape="false">
									<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
								</h:outputFormat>								
								<f:param name="competenceCourseGroupUnitID" value="#{competenceCourseGroupUnit.idInternal}"/>
							</h:outputLink>
						</f:facet>
					</h:dataTable>
				</fc:dataRepeater>						
			</fc:dataRepeater>
		</h:panelGroup>
	</h:form>
</ft:tilesView>