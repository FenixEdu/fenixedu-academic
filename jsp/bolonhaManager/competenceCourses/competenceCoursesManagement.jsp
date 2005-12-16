<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<h:form>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}" style="font-style:italic"/>
		<h2><h:outputText value="#{bolonhaBundle['competenceCoursesManagement']}"/></h2>		
		<h:outputText styleClass="error" rendered="#{!empty CompetenceCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CompetenceCourseManagement.errorMessage]}"/>
		<br/><style>@import url(<%= request.getContextPath() %>/CSS/dotist_degreeStructure.css);</style>
		<fc:dataRepeater value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit">
			<h:outputText value="> #{scientificAreaUnit.name}<br/>" style="font-weight: bold" escape="false"/>
			<fc:dataRepeater value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
				<h:outputText value="<br/>" escape="false"/>
				<h:outputText value="#{competenceCourseGroupUnit.name}<br/>" escape="false" style="margin-left: 3em;"/>
				<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
					styleClass="style2 indent1" footerClass="style2 alignRight">
					<h:column>
						<h:outputLink value="showCompetenceCourse.faces">
							<h:outputText value="#{competenceCourse.name}"/>
							<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
						</h:outputLink>
						<h:outputText value="  (#{bolonhaBundle[competenceCourse.curricularStage]})" style="font-style:italic"/>
					</h:column>
					<h:column>
						<h:outputLink value="editCompetenceCourse.faces">
							<h:outputText value="#{bolonhaBundle['edit']}" />
							<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
						</h:outputLink>
						<h:outputText value=", "/>
						<h:outputLink value="deleteCompetenceCourse.faces">
							<h:outputText value="#{bolonhaBundle['delete']}" />
							<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
						</h:outputLink>
					</h:column>
					<f:facet name="footer">
						<h:outputLink value="createCompetenceCourse.faces">
							<h:outputText value="#{bolonhaBundle['createCompetenceCourse']}<br/>" escape="false"/>
							<f:param name="scientificAreaUnitID" value="#{scientificAreaUnit.idInternal}"/>
							<f:param name="competenceCourseGroupUnitID" value="#{competenceCourseGroupUnit.idInternal}"/>
						</h:outputLink>
					</f:facet>
				</h:dataTable>
			</fc:dataRepeater>						
		</fc:dataRepeater>
	</h:form>
</ft:tilesView>