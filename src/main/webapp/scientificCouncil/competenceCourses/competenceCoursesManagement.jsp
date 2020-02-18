<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<fp:select actionClass="org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication$ScientificCompetenceCoursesManagement" />
${portal.toolkit()}
<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

		<h:outputText value="<h2>#{scouncilBundle['competenceCoursesManagement']}</h2>" escape="false"/>

		<h:form>
	
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>


		<h:panelGrid columns="2" style="infocell" columnClasses="infocell">
			<h:outputText value="#{scouncilBundle['department']}:" escape="false"/>
			<fc:selectOneMenu value="#{CompetenceCourseManagement.selectedDepartmentUnitID}" onchange="submit()">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</fc:selectOneMenu>
		</h:panelGrid>


		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.groupMembers}">

			<h:outputText value="<p class='mtop15 mbottom05'><b id='members' class='highlight1'>#{scouncilBundle['groupMembers']}</b> #{scouncilBundle['label.group.members.explanation']}:</p>" escape="false" />
			<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
			
			<h:panelGroup rendered="#{CompetenceCourseManagement.groupEditMode}">
				<h:selectManyCheckbox value="#{CompetenceCourseManagement.selectedGroupMembersToDelete}" layout="pageDirection">
					<f:selectItems value="#{CompetenceCourseManagement.groupMembers}"  />
				</h:selectManyCheckbox>
				<h:outputText value="<p>" escape="false" />	
				<h:commandLink value="#{scouncilBundle['removeMembers']}" actionListener="#{CompetenceCourseManagement.removeUsersFromGroup}" />
				<h:outputText value="</p>" escape="false" />
				<h:panelGroup rendered="#{empty CompetenceCourseManagement.groupMembers}">
					<h:outputText value="<br/><i>#{scouncilBundle['label.empty.curricularPlanGroup.members']}</i><br/>" escape="false" />
				</h:panelGroup>
				
				<h:outputText value="<p><b>#{scouncilBundle['addNewMembers']}</b>:<p/>" escape="false" />
				<div class="form-group">
					<h:outputText value="<input bennu-user-autocomplete id='newGroupMember' name='newGroupMember' type='text' size='60' placeHolder='#{htmlAltBundle[\'placeholder.user.autocomplete\']}'/>" escape="false"/>
				</div>
				
				<h:outputText escape="false" value="<input alt='input.groupEditMode' id='groupEditMode' name='groupEditMode' type='hidden' value='#{CompetenceCourseManagement.groupEditMode}'/>"/>
				<h:commandButton alt="#{scouncilBundle['addPerson']}" styleClass="inputbutton" value="#{scouncilBundle['addPerson']}"  action="#{CompetenceCourseManagement.addUserToGroup}" />
				
			</h:panelGroup>
			
			<h:panelGroup rendered="#{!CompetenceCourseManagement.groupEditMode}">
				<h:outputText value="<ul>" escape="false"/>
				<fc:dataRepeater value="#{CompetenceCourseManagement.groupMembersLabels}" var="memberLabel">
					<h:outputText value="<li>#{memberLabel}</li>" escape="false"/>
				</fc:dataRepeater>
				<h:outputText value="</ul>" escape="false"/>
				
				<h:commandButton alt="#{scouncilBundle['accessGroupManagement']}" styleClass="inputbutton" value="#{scouncilBundle['accessGroupManagement']}"
					action="#{CompetenceCourseManagement.toggleGroupEditMode}" />
			</h:panelGroup>
			
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.groupMembersLabels && !empty CompetenceCourseManagement.selectedDepartmentUnitID}">
			<h:outputText value="<br/><i>#{scouncilBundle['label.empty.group.members']}</i><br/>" escape="false" />
		</h:panelGroup>
		
		
			
		<!--  Scientific Area Units -->

		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">	
		<h:outputText value="<p class='mtop2 mbottom05'><b>Opções de listagem:</b></p>" escape="false"/>
		<h:outputText value="<ul>" escape="false"/>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.departmentDraftCompetenceCourses}" >
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#{facesContext.externalContext.requestContextPath}/scientificCouncil/competenceCourses/showAllCompetenceCourses.faces" target="_blank">
					<h:outputText value="#{scouncilBundle['showDraftCompetenceCourses']} (#{scouncilBundle['newPage']})" escape="false"/>
					<f:param name="competenceCoursesToList" value="DRAFT"/>
					<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</h:panelGroup>

		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.departmentPublishedCompetenceCourses}">		
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/scientificCouncil/competenceCourses/showAllCompetenceCourses.faces" target="_blank">
				<h:outputText value="#{scouncilBundle['showPublishedCompetenceCourses']} (#{scouncilBundle['newPage']})" escape="false"/>
				<f:param name="competenceCoursesToList" value="PUBLISHED"/>
				<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.departmentApprovedCompetenceCourses}">
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/scientificCouncil/competenceCourses/showAllCompetenceCourses.faces" target="_blank">
				<h:outputText value="#{scouncilBundle['showApprovedCompetenceCourses']} (#{scouncilBundle['newPage']})" escape="false"/>
				<f:param name="competenceCoursesToList" value="APPROVED"/>
				<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</h:panelGroup>

				<h:outputText value="<li>" escape="false"/>
					<h:outputLink value="#{facesContext.externalContext.requestContextPath}/competence-management/department/#{CompetenceCourseManagement.selectedDepartmentUnitID}/downloadCompetenceCourseInformation">
						<h:outputText value="#{scouncilBundle['downloadCompetenceCourseInformation']}" escape="false"/>
					</h:outputLink>
				<h:outputText value="</li>" escape="false"/>


			<h:outputText value="</ul>" escape="false"/>
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
					<h:dataTable value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit" style="width: 100%">
							<h:column>
								<h:outputText value="<li class='tree_label' style='background-position: 0em 0.5em;'>#{competenceCourseGroupUnit.name}" escape="false"/>
								<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
										styleClass="showinfo1 smallmargin mtop05" style="width: 100%;" rowClasses="color2" columnClasses=",aright nowrap" rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
										
										<h:column>
											<h:outputText value="#{competenceCourse.code} - " rendered="#{!empty competenceCourse.code}"/><h:outputText value="#{competenceCourse.name} "/>
											<h:outputText rendered="#{!empty competenceCourse.acronym}" value="(#{competenceCourse.acronym}) "/>
											<h:outputText value="<span style='color: #aaa;'>" escape="false"/>
											<h:outputText rendered="#{competenceCourse.curricularStage.name == 'DRAFT'}" value="<em style='color: #bb5;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>" escape="false"/>
											<h:outputText rendered="#{competenceCourse.curricularStage.name == 'PUBLISHED'}" value="<em style='color: #569;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>" escape="false"/>
											<h:outputText rendered="#{competenceCourse.curricularStage.name == 'APPROVED'}" value="<em style='color: #595;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>" escape="false"/>
											<h:outputText value="</span>" escape="false"/>
										</h:column>
									
										<h:column>
											<h:outputLink value="#{facesContext.externalContext.requestContextPath}/scientificCouncil/competenceCourses/showCompetenceCourse.faces">
												<h:outputText value="#{scouncilBundle['show']}"/>
												<f:param name="action" value="ccm"/>
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
</f:view>