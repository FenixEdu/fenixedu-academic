<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

		<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
		<h:outputText value="<h2>#{scouncilBundle['competenceCourses']}</h2>" escape="false"/>

		<h:form>
	
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:panelGrid columns="2" style="infocell" columnClasses="infocell">
			<h:outputText value="#{scouncilBundle['department']}:" escape="false"/>
			<fc:selectOneMenu value="#{CompetenceCourseManagement.selectedDepartmentUnitID}" onchange="submit()">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</fc:selectOneMenu>
			<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
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
										styleClass="showinfo1 smallmargin mtop05" style="width: 60em;" rowClasses="color2" columnClasses=",aright" rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
										
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
												<h:outputText value="#{scouncilBundle['show']}"/>
												<f:param name="action" value="ccm"/>
												<f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
												<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
											</h:outputLink>
											<h:outputText value=" , "/>
											<h:outputLink value="editCompetenceCourseMainPage.faces">
												<h:outputText value="#{scouncilBundle['edit']}"/>
												<f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
												<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
											</h:outputLink>
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