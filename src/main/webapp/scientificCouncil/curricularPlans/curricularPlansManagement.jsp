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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication$ScientificCurricularPlansManagement" />


<f:view>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<h2>#{scouncilBundle['curricularPlansManagement']}</h2>" escape="false"/>

	<h:outputText value="<ul>" escape="false" />
	<h:outputText value="<li>" escape="false" />
	<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/createDegree.faces">
		<h:outputFormat value="#{scouncilBundle['create.param']}">
			<f:param value="#{scouncilBundle['degree']}"/>
		</h:outputFormat>
	</h:outputLink>
	<h:outputText value="</li>" escape="false" />
	<h:outputText value="</ul>" escape="false" />

	<h:messages errorClass="error0" infoClass="success0"/>
	
	<h:form>
		<h:panelGrid columns="2" style="infocell" columnClasses="infocell">
				<h:outputText value="#{bolonhaBundle['degreeType']}:" escape="false"/>
				<fc:selectOneMenu value="#{DegreeManagement.bolonhaDegreeType}" onchange="submit()">
					<f:selectItems value="#{DegreeManagement.bolonhaDegreeTypes}"/>
				</fc:selectOneMenu>
		</h:panelGrid>
	</h:form>

	<h:outputText value="<i>#{bolonhaBundle['no.curricularPlans.for.degreeType']}</i>" escape="false" rendered="#{empty DegreeManagement.bolonhaDegrees}"/>
	
	
	<fc:dataRepeater value="#{DegreeManagement.bolonhaDegrees}" var="degree" rendered="#{!empty DegreeManagement.bolonhaDegrees}">
		<h:outputText value="<table style='width: 100%;' class='showinfo1'>" escape="false"/>
		<h:outputText value="<tr class='bgcolor1'><th style='width: 80px'><strong>#{scouncilBundle['degree']}:</strong></th>" escape="false"/>

		<h:outputText value="<td><em>#{degree.presentationName} (#{degree.sigla})" escape="false"/>
			<h:outputText value=" [#{degree.code}]" rendered="#{!empty degree.code}" escape="false"/>
		<h:outputText value="</em></td>" escape="false"/>
		
		<h:outputText value="<td style='width: 230px'>" escape="false"/>
		<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editDegree.faces">
			<h:outputFormat value="#{scouncilBundle['edit']}"/>
			<f:param name="degreeId" value="#{degree.externalId}"/>
		</h:outputLink>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/deleteDegree.faces">
			<h:outputFormat value="#{scouncilBundle['delete']}"/>
			<f:param name="degreeId" value="#{degree.externalId}"/>
		</h:outputLink>
		<h:outputText value=" , " escape="false"/>		
		<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/createCurricularPlan.faces">
			<h:outputFormat value="#{scouncilBundle['create.param']}">
				<f:param value="#{scouncilBundle['plan']}"/>
			</h:outputFormat>
			<f:param name="degreeId" value="#{degree.externalId}"/>
		</h:outputLink>
		<h:outputText value="</td></tr>" escape="false"/>
		
		<h:outputText value="<tr><td colspan='3' align='center'><em>#{scouncilBundle['no.curricularPlan']}.</em></td></tr>" escape="false" rendered="#{empty degree.degreeCurricularPlans}"/>


		<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan" rendered="#{!empty degree.degreeCurricularPlans}" rowIndexVar="index">
			<h:outputText value="<tr class='bgcolor1'>" escape="false"/>
			<h:outputText rendered="#{index == 0}" value="<th><strong>#{scouncilBundle['plans']}:</strong></th>" escape="false"/>
			<h:outputText rendered="#{index > 0}" value="<th></th>" escape="false"/>

			<h:outputText value="<td>" escape="false" />
			<h:outputText rendered="#{degreeCurricularPlan.curricularStage.name == 'DRAFT'}" value="<em class='highlight1' style='color: #555;'>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}</em>" escape="false" />
			<h:outputText rendered="#{degreeCurricularPlan.curricularStage.name == 'PUBLISHED'}" value="<em class='highlight3' style='color: #555;'>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}</em>" escape="false" />
			<h:outputText rendered="#{degreeCurricularPlan.curricularStage.name == 'APPROVED'}" value="<em class='highlight4' style='color: #555;'>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}</em>" escape="false" />
			<h:outputText value=" #{degreeCurricularPlan.name}</td>" escape="false" />

			<h:outputText value="<td>" escape="false"/>
			<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/viewCurricularPlan.faces">
				<h:outputText value="#{scouncilBundle['view']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.externalId}"/>
				<f:param name="action" value="view"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editCurricularPlan.faces">
				<h:outputText value="#{scouncilBundle['edit']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.externalId}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/deleteCurricularPlan.faces">
				<h:outputFormat value="#{scouncilBundle['delete']}"/>
				<f:param name="dcpId" value="#{degreeCurricularPlan.externalId}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editCurricularPlanMembersGroup.faces">
				<h:outputText value="#{scouncilBundle['group']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.externalId}"/>
			</h:outputLink>
			<h:outputText value="</td></tr>" escape="false"/>
		</fc:dataRepeater>

		<h:outputText value="</table>" escape="false"/>
	</fc:dataRepeater>

</f:view>
