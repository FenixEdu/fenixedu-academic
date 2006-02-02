<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>

	<h:outputText rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}" value="<em>#{bolonhaBundle['competenceCoursesManagement']}</em>" escape="false"/>
	<h:outputText value="<h2>#{CompetenceCourseManagement.personDepartment.realName}</h2>" escape="false"/>

	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.personDepartment}">

		<h:panelGroup rendered="#{CompetenceCourseManagement.canView}">
	<h:form>
		<h:messages infoClass="success0" errorClass="error0" layout="table"/>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.scientificAreaUnits}">
			<h:outputText style="font-style:italic" value="#{bolonhaBundle['noScientificAreaUnits']}<br/>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
			<fc:dataRepeater value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit">
				<h:outputText value="<p class='mtop3 mbottom0'><strong>#{scientificAreaUnit.name}</strong></p>" escape="false"/>
				<h:panelGroup rendered="#{empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:outputText style="font-style:italic" value="#{bolonhaBundle['noCompetenceCourseGroupUnits']}<br/>" escape="false"/>
				</h:panelGroup>
				<h:panelGroup rendered="#{!empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:outputText value="<ul>" escape="false"/>
					<fc:dataRepeater value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
						<h:outputText value="<li class='tree_label'>#{competenceCourseGroupUnit.name} - " escape="false"/>
						<h:outputLink value="createCompetenceCourse.faces">
							<h:outputFormat value="#{bolonhaBundle['create.param']}<br/>" escape="false">
								<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
							</h:outputFormat>								
							<f:param name="competenceCourseGroupUnitID" value="#{competenceCourseGroupUnit.idInternal}"/>
						</h:outputLink>
						<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
										styleClass="showinfo1 smallmargin mtop05" rowClasses="color2" rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
							<h:column>
								<h:outputText value="#{competenceCourse.name}"/>
								<h:outputText value="  (#{enumerationBundle[competenceCourse.curricularStage]})" style="font-style:italic"/>
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
			<h:outputText style="font-style:italic" value="<br/>#{bolonhaBundle['notMemberInCompetenceCourseManagementGroup']}<br/>" escape="false"/>
		</h:panelGroup>
	
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.groupMembersLabels}">
			<h:outputText value="<br/><b>#{bolonhaBundle['groupMembers']}</b> #{bolonhaBundle['label.group.members.explanation']}:<br/>" escape="false" />
			<h:dataTable value="#{CompetenceCourseManagement.groupMembersLabels}" var="memberLabel">
				<h:column>
					<h:outputText value="#{memberLabel}" escape="false"/>
				</h:column>
			</h:dataTable>
		</h:panelGroup>

	</h:panelGroup>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.personDepartment}">
		<h:outputText style="font-style:italic" value="#{bolonhaBundle['no.current.department.working.place']}<br/>" escape="false"/>
	</h:panelGroup>
</ft:tilesView>