<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

		<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
		<h:outputText value="<h2>#{scouncilBundle['competenceCoursesManagement']}</h2>" escape="false"/>

		<h:form>
	
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:panelGrid columns="2" style="infocell" columnClasses="infocell">
			<h:outputText value="#{scouncilBundle['department']}:" escape="false"/>
			<fc:selectOneMenu value="#{CompetenceCourseManagement.selectedDepartmentUnitID}" onchange="submit()">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</fc:selectOneMenu>
		</h:panelGrid>

<%--
		<h:outputText value="<p class='mtop2 mbottom2'><a href='#members' title='#{scouncilBundle['view.group.members.description']}'>#{scouncilBundle['view.group.members']}</a></p>" escape="false"/>
--%>

		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.groupMembersLabels}">
			<h:outputText value="<br/><b id='members' class='highlight1'>#{scouncilBundle['groupMembers']}</b> #{scouncilBundle['label.group.members.explanation']}:<br/>" escape="false" />
			<h:outputText value="<ul>" escape="false"/>
			<fc:dataRepeater value="#{CompetenceCourseManagement.groupMembersLabels}" var="memberLabel">
				<h:outputText value="<li>#{memberLabel}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.groupMembersLabels && !empty CompetenceCourseManagement.selectedDepartmentUnitID}">
			<h:outputText value="<br/><i>#{scouncilBundle['label.empty.group.members']}</i><br/>" escape="false" />
		</h:panelGroup>
		

		<h:outputLink rendered="#{!empty CompetenceCourseManagement.departmentDraftCompetenceCourses}" value="showAllCompetenceCourses.faces" target="_blank">
			<h:outputText value="<br/>#{scouncilBundle['showDraftCompetenceCourses']} (#{scouncilBundle['newPage']})" escape="false"/>
			<f:param name="competenceCoursesToList" value="DRAFT"/>
			<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
		</h:outputLink>
		<h:outputLink rendered="#{!empty CompetenceCourseManagement.departmentPublishedCompetenceCourses}" value="showAllCompetenceCourses.faces" target="_blank">
			<h:outputText value="<br/>#{scouncilBundle['showPublishedCompetenceCourses']} (#{scouncilBundle['newPage']})" escape="false"/>
			<f:param name="competenceCoursesToList" value="PUBLISHED"/>
			<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
		</h:outputLink>
		<h:outputLink rendered="#{!empty CompetenceCourseManagement.departmentApprovedCompetenceCourses}" value="showAllCompetenceCourses.faces" target="_blank">
			<h:outputText value="<br/>#{scouncilBundle['showApprovedCompetenceCourses']} (#{scouncilBundle['newPage']})" escape="false"/>
			<f:param name="competenceCoursesToList" value="APPROVED"/>
			<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
		</h:outputLink>

		<h:dataTable value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit"
				rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
			<h:column>
				<h:outputText value="<p class='mtop2 mbottom0'><strong>#{scientificAreaUnit.name}</strong></p>" escape="false"/>
				<h:panelGroup rendered="#{empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:outputText style="font-style:italic" value="#{scouncilBundle['noCompetenceCourseGroupUnits']}<br/>" escape="false"/>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{!empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:outputText value="<ul class='list3'>" escape="false"/>
					<h:dataTable value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
							<h:column>
								<h:outputText value="<li class='tree_label' style='background-position: 0em 0.5em;'>#{competenceCourseGroupUnit.name}" escape="false"/>
								<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
										styleClass="showinfo1 smallmargin mtop05" style="width: 50em;" rowClasses="color2" columnClasses=",aright" rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
										
										<h:column>
											<h:outputText value="#{competenceCourse.name}"/>
											<h:outputText value=" <em>(#{enumerationBundle[competenceCourse.curricularStage]})</em>" escape="false"/>
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
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.scientificAreaUnits && !empty CompetenceCourseManagement.selectedDepartmentUnitID}">
			<h:outputText  value="<i>#{scouncilBundle['noScientificAreaUnits']}<i><br/>" escape="false"/>
		</h:panelGroup>
		
	</h:form>
</ft:tilesView>