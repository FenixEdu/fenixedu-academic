<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	<h:form>
		<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
		<h:outputText value="<h2>#{scouncilBundle['competenceCoursesManagement']}</h2>" escape="false"/>

		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:panelGrid columns="2" style="infocell" columnClasses="infocell">
			<h:outputText value="#{scouncilBundle['department']}:" escape="false"/>
			<fc:selectOneMenu value="#{CompetenceCourseManagement.selectedDepartmentUnitID}" onchange="submit()">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</fc:selectOneMenu>
		</h:panelGrid>

		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
			<h:dataTable value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit">
				<h:column>
					<h:outputText value="<p class='mtop3 mbottom0'><strong>#{scientificAreaUnit.name}</strong></p>" escape="false"/>
					<h:panelGroup rendered="#{empty scientificAreaUnit.competenceCourseGroupUnits}">
						<h:outputText style="font-style:italic" value="#{scouncilBundle['noCompetenceCourseGroupUnits']}<br/>" escape="false"/>
					</h:panelGroup>
					<h:panelGroup rendered="#{!empty scientificAreaUnit.competenceCourseGroupUnits}">
						<h:outputText value="<ul>" escape="false"/>
						<h:dataTable value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
								<h:column>
									<h:outputText value="<li class='tree_label'>#{competenceCourseGroupUnit.name}" escape="false"/>
									<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
											styleClass="showinfo1 smallmargin mtop05" rowClasses="color2" rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
											<h:column>
												<h:outputText value="#{competenceCourse.name}"/>
												<h:outputText value="  (#{enumerationBundle[competenceCourse.curricularStage]})" style="font-style:italic"/>
											</h:column>
											<h:column>
												<h:outputLink value="showCompetenceCourse.faces">
													<h:outputText value="#{scouncilBundle['show']}"/>
													<f:param name="action" value="ccm"/>
													<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
													<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
												</h:outputLink>
												<h:panelGroup rendered="#{competenceCourse.curricularStage.name != 'DRAFT'}">
													<h:outputText value=" , "/>
													<fc:commandLink rendered="#{competenceCourse.curricularStage.name == 'PUBLISHED'}" action="#{CompetenceCourseManagement.changeCompetenceCourseState}" value="#{scouncilBundle['approve']}">
														<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
														<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
													</fc:commandLink>	
													<fc:commandLink rendered="#{competenceCourse.curricularStage.name == 'APPROVED'}" action="#{CompetenceCourseManagement.changeCompetenceCourseState}" value="#{scouncilBundle['disapprove']}">
														<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
														<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
													</fc:commandLink>
												</h:panelGroup>												
											</h:column>
									</h:dataTable>
									<h:outputText value="</li>" escape="false"/>
								</h:column>
						</h:dataTable>
						<h:outputText value="</ul>" escape="false"/>
					</h:panelGroup>
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.groupMembersLabels}">
			<h:outputText value="<br/><b>#{scouncilBundle['groupMembers']}</b> #{scouncilBundle['label.group.members.explanation']}:<br/>" escape="false" />
			<h:dataTable value="#{CompetenceCourseManagement.groupMembersLabels}" var="memberLabel">
				<h:column>
					<h:outputText value="#{memberLabel}" escape="false"/>
				</h:column>
			</h:dataTable>
		</h:panelGroup>
	</h:form>
</ft:tilesView>