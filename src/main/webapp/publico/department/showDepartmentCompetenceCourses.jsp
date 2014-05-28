<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="definition.public.mainPageIST" attributeName="body-inline">

	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>

	<h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
		
		<h:outputLink value="#{CompetenceCourseManagement.applicationUrl}" >
			<h:outputText value="#{CompetenceCourseManagement.institutionAcronym}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		
		<h:outputLink target="_blank" value="#{CompetenceCourseManagement.institutionUrl}#{globalBundle['link.institution.structure']}" >
			<h:outputText value="#{publicDegreeInfoBundle['structure']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink value="#{CompetenceCourseManagement.contextPath}/publico/department/showDepartments.faces">
			<h:outputText value="#{publicDepartmentBundle['academic.units']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
				
		<fc:contentLink label="#{CompetenceCourseManagement.selectedDepartmentUnit.department.nameI18n.content}" content="#{CompetenceCourseManagement.selectedDepartmentUnit.site}" />
		
		&nbsp;&gt;&nbsp;
		<h:outputText value="#{publicDepartmentBundle['department.courses']}"/>

	<h:outputText value="</div>" escape="false"/>
	
	<h:messages rendered="#{!empty error0 || !empty success0}" infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:outputText value="<h1>#{publicDepartmentBundle['department.courses']} #{publicDepartmentBundle['from.masculine']} " escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.nameI18n.content}</h1>" escape="false"/>

	<h:form id="SomeId">

		<h:dataTable value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit"
				rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
			<h:column>
				<h:outputText value="<h2 class='mtop1 mbottom0 greytxt'><strong>#{scientificAreaUnit.nameI18n}</strong></h2>" escape="false"/>
				<h:panelGroup rendered="#{empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:outputText style="font-style:italic" value="#{scouncilBundle['noCompetenceCourseGroupUnits']}<br/>" escape="false"/>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{!empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:dataTable value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
							<h:column>
								<h:outputText value="<h2 class='arrow_bullet'>#{competenceCourseGroupUnit.nameI18n}</h2>" escape="false"/>
								<h:panelGroup rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
								<h:outputText value="<table class='showinfo1 smallmargin mtop05' style='width: 50em;'>" escape="false"/>
								<fc:dataRepeater value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse">
										<h:panelGroup rendered="#{competenceCourse.curricularStage.name == 'APPROVED'}">
										<h:outputText value="<tr class='color2'><td>" escape="false"/>	

										<h:outputLink value="#{CompetenceCourseManagement.contextPath}/publico/department/showCompetenceCourse.faces" style="text-decoration:none">
											<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{competenceCourse.name} (#{competenceCourse.acronym})"/>
											<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{competenceCourse.nameEn} (#{competenceCourse.acronym})"/>
											<f:param name="action" value="ccm"/>
											<f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
											<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
										</h:outputLink>

										<h:outputText value="</td></tr>" escape="false"/>
										</h:panelGroup>
								</fc:dataRepeater>
								<h:outputText value="</table>" escape="false"/>
								</h:panelGroup>
							</h:column>
					</h:dataTable>
				</h:panelGroup>
			</h:column>
		</h:dataTable>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.scientificAreaUnits && !empty CompetenceCourseManagement.selectedDepartmentUnitID}">
			<h:outputText  value="<i>#{scouncilBundle['noScientificAreaUnits']}<i><br/>" escape="false"/>
		</h:panelGroup>
		
	</h:form>

</ft:tilesView>
