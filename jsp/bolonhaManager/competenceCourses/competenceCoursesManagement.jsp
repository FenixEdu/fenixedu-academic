<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}" value="<em>#{bolonhaBundle['bolonhaManager']}</em>" escape="false"/>
	<h:outputText value="<h2>#{CompetenceCourseManagement.personDepartment.realName}</h2>" escape="false"/>

	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.groupMembersLabels}">
		<h:outputText value="<p class='mtop15 mbottom05'><b id='members' class='highlight1'>#{bolonhaBundle['groupMembers']}</b> #{bolonhaBundle['label.group.members.explanation']}:</p>" escape="false" />
		<h:outputText value="<ul>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.groupMembersLabels}" var="memberLabel">
			<h:outputText value="<li>#{memberLabel}</li>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.groupMembersLabels}">
		<h:outputText value="<i>#{bolonhaBundle['label.empty.group.members']}</i><br/>" escape="false" />
	</h:panelGroup>

	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.personDepartment}">
		<h:panelGroup rendered="#{CompetenceCourseManagement.canView}">
			<h:form>
				<h:messages infoClass="success0" errorClass="error0" layout="table"/>
				<h:panelGroup rendered="#{empty CompetenceCourseManagement.scientificAreaUnits}">
					<h:outputText  value="<i>#{bolonhaBundle['noScientificAreaUnits']}<i><br/>" escape="false"/>
				</h:panelGroup>
				<h:panelGroup rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
					<fc:dataRepeater value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit">
						<h:outputText value="<p class='mtop2 mbottom0'><strong>#{scientificAreaUnit.name}</strong></p>" escape="false"/>
						<h:panelGroup rendered="#{empty scientificAreaUnit.competenceCourseGroupUnits}">
							<h:outputText value="#{bolonhaBundle['noCompetenceCourseGroupUnits']}><br/>" escape="false"/>
						</h:panelGroup>
						
						
						<h:panelGroup rendered="#{!empty scientificAreaUnit.competenceCourseGroupUnits}">
							<h:outputText value="<ul class='list3' style='padding-left: 2em;'>" escape="false"/>
							<fc:dataRepeater value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
								<h:outputText value="<li class='tree_label' style='background-position: 0em 0.75em;'>" escape="false"/>
								<h:outputText value="<table style='width: 100%; background-color: #fff;'><tr>" escape="false"/>
								<h:outputText value="<td>#{competenceCourseGroupUnit.name}</td> " escape="false"/>
								<h:outputText value="<td class='aright'>" escape="false"/>
									<h:outputLink value="createCompetenceCourse.faces">
										<h:outputFormat value="#{bolonhaBundle['create.param']}" escape="false">
											<f:param value=" #{bolonhaBundle['course']}"/>
										</h:outputFormat>								
										<f:param name="competenceCourseGroupUnitID" value="#{competenceCourseGroupUnit.idInternal}"/>
									</h:outputLink>
								<h:outputText value="</td></tr></table>" escape="false"/>
								

								<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse" 
												styleClass="showinfo1 smallmargin mtop05" style="width: 100%;" rowClasses="color2" columnClasses=",aright nowrap" rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
									<h:column>
										<h:outputText value="#{competenceCourse.name} "/>
										<h:outputText rendered="#{!empty competenceCourse.acronym}" value="(#{competenceCourse.acronym}) "/>
										<h:outputText value="<span style='color: #aaa;'>" escape="false"/>
										<h:outputText rendered="#{competenceCourse.curricularStage.name == 'DRAFT'}" value="<em style='color: #bb5;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>" escape="false"/>
										<h:outputText rendered="#{competenceCourse.curricularStage.name == 'PUBLISHED'}" value="<em style='color: #569;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>" escape="false"/>
										<h:outputText rendered="#{competenceCourse.curricularStage.name == 'APPROVED'}" value="<em style='color: #595;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>" escape="false"/>
										<h:outputText value="</span>" escape="false"/>
									</h:column>

									<h:column>
										<h:outputLink value="showCompetenceCourse.faces">
											<h:outputText value="#{bolonhaBundle['show']}"/>
											<f:param name="action" value="ccm"/>
											<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
										</h:outputLink>
										<h:panelGroup rendered="#{competenceCourse.curricularStage.name != 'APPROVED'}">
											<h:outputText value=", "/>
												<h:outputLink value="editCompetenceCourseMainPage.faces">
													<h:outputText value="#{bolonhaBundle['edit']}" />
													<f:param name="action" value="ccm"/>
													<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
												</h:outputLink>
												<h:outputText value=", "/>
														<h:outputLink rendered="#{competenceCourse.curricularStage.name != 'APPROVED'}" value="deleteCompetenceCourse.faces">
												<h:outputText value="#{bolonhaBundle['delete']}" />
												<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
											</h:outputLink>
											<h:outputText value=", "/>
											<h:outputLink value="setCompetenceCourseBibliographicReference.faces">
												<h:outputText value="#{bolonhaBundle['bibliographicReference']}" />
												<f:param name="action" value="add"/>
												<f:param name="bibliographicReferenceID" value="-1"/>
												<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
											</h:outputLink>									
										</h:panelGroup>
									</h:column>
								</h:dataTable>
								
								<h:outputText value="</li>" escape="false"/>
							</fc:dataRepeater>
							<h:outputText value="</ul>" escape="false"/>
						</h:panelGroup>
					</fc:dataRepeater>
				</h:panelGroup>
			</h:form>
		</h:panelGroup>		
	
		<h:panelGroup rendered="#{!CompetenceCourseManagement.canView}">
			<h:outputText value="<br/><em>#{bolonhaBundle['notMemberInCompetenceCourseManagementGroup']}</em><br/>" escape="false"/>
		</h:panelGroup>
	
	</h:panelGroup>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.personDepartment}">
		<h:outputText value="<i>#{bolonhaBundle['no.current.department.working.place']}</i><br/>" escape="false"/>
	</h:panelGroup>
	

		
</ft:tilesView>