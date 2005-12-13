<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<h:form>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}" style="font-style:italic"/>	
		<h2><h:outputText value="#{bolonhaBundle['competenceCoursesManagement']}"/></h2>
		<h:outputText value="* "/>
		<h:outputLink value="createCompetenceCourse.faces">
			<h:outputText value="#{bolonhaBundle['createCompetenceCourse']}" />
		</h:outputLink>		
		<br/>
		<h:outputText value="--== Temporary Links ==--"/>
		<br/>
		<h:outputText value="* "/>
		<h:outputLink value="showCompetenceCourse.faces">
			<h:outputText value="Ver Competencia -> Colocar Nome da Competência" />
			<%-- <f:param id="competenceCourseID" value="ID" /> --%>
		</h:outputLink>
		<br/>
		<h:outputText value="* "/>
		<h:outputLink value="editCompetenceCourse.faces">
			<h:outputText value="#{bolonhaBundle['edit']}" />
			<%-- <f:param id="competenceCourseID" value="ID" /> --%>
		</h:outputLink>
		<br/>
		<h:outputText value="* "/>
		<h:commandLink action="" value="#{bolonhaBundle['delete']}">
			<%-- <f:param id="competenceCourseID" value="ID" /> --%>
		</h:commandLink>
		<br/>
		<h:outputText value="--== Temporary Links ==--"/>
		<br/>
		<br/>

		<h:dataTable value="#{CompetenceCourseManagement.scientificAreas}" var="scientificArea">
			<h:column>
				<h:outputText value="#{scientificArea.name}" style="font-weight: bold"/>
				<h:dataTable value="#{scientificArea.competenceCourseGroups}" var="competenceCourseGroup">
					<h:column>
						<h:outputLink value="createCompetenceCourse.faces">
							<h:outputText value="#{bolonhaBundle['createCompetenceCourse']}<br/>" escape="false"/>
							<f:param name="scientificAreaID" value="#{scientificArea.idInternal}"/>
							<f:param name="competenceCourseGroupID" value="#{competenceCourseGroup.idInternal}"/>
						</h:outputLink>	
						<h:outputText value="-> #{competenceCourseGroup.name}" style="font-weight: bold"/>
						<h:dataTable value="#{competenceCourseGroup.competenceCourses}" var="competenceCourse">
							<h:column>
								<h:outputText value="--> "/>
								<h:outputLink value="showCompetenceCourse.faces">
									<h:outputText value="#{competenceCourse.name}"/>
									<f:param name="scientificAreaID" value="#{scientificArea.idInternal}"/>
									<f:param name="competenceCourseGroupID" value="#{competenceCourseGroup.idInternal}"/>
									<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
								</h:outputLink>
								<h:outputText value=" ---> "/>
								<h:outputLink value="editCompetenceCourse.faces">
									<h:outputText value="#{bolonhaBundle['edit']}" />
									<f:param name="scientificAreaID" value="#{scientificArea.idInternal}"/>
									<f:param name="competenceCourseGroupID" value="#{competenceCourseGroup.idInternal}"/>
									<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
								</h:outputLink>
								<h:outputText value=" ----> "/>
								<h:commandLink action="#{CompetenceCourseManagement.deleteCompetenceCourse}"
									value="#{bolonhaBundle['delete']}">
									<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
								</h:commandLink>
							</h:column>
						</h:dataTable>	
					</h:column>
				</h:dataTable>				
			</h:column>
		</h:dataTable>
	</h:form>
</ft:tilesView>