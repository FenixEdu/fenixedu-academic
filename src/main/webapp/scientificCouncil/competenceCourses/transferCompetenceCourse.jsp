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
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication$ScientificCompetenceCoursesManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<h2>#{CompetenceCourseManagement.name}</h2>" escape="false"/>

	<h:outputText value="<ul class='nobullet padding1 indent0 mtop15'>" escape="false"/>
	
	<h:form>
		<fc:viewState binding="#{CompetenceCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'/>"/>
		
		<h:outputText value="<p>" escape="false"/>
		<fc:selectOneMenu value="#{CompetenceCourseManagement.executionSemesterID}"
			onchange="submit()">
			<f:selectItems binding="#{CompetenceCourseManagement.executionSemesterItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="<p/>" escape="false"/>
	
		<h:outputText value="<li><strong>#{scouncilBundle['department']}: </strong>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.departmentRealName}</li>" escape="false"/>
		<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.scientificAreaUnitName} > #{CompetenceCourseManagement.competenceCourseGroupUnitName}</li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	
		<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>
		<h:outputText value="#{scouncilBundle['transfer.to']}:<p>" escape="false"/>
		
		
		
		<fc:selectOneMenu value="#{CompetenceCourseManagement.transferToDepartmentUnitID}"
			onchange="submit()"
			valueChangeListener="#{CompetenceCourseManagement.onChangeDepartmentUnit}">
			<f:selectItems binding="#{CompetenceCourseManagement.departmentUnitItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="<p/><p>" escape="false"/>
		
		
		
  		<fc:selectOneMenu value="#{CompetenceCourseManagement.transferToScientificAreaUnitID}" 
			onchange="submit()"
			valueChangeListener="#{CompetenceCourseManagement.onChangeScientificAreaUnit}">
			<f:selectItems binding="#{CompetenceCourseManagement.scientificAreaUnitItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="<p/><p>" escape="false"/>
		
		
		
		
		<fc:selectOneMenu value="#{CompetenceCourseManagement.transferToCompetenceCourseGroupUnitID}">
			<f:selectItems binding="#{CompetenceCourseManagement.competenceCourseGroupUnitItems}"/>
		</fc:selectOneMenu>
		
		<h:outputText value="<p class='mtop15'>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.transfer']}" styleClass="inputbutton" action="#{CompetenceCourseManagement.transferCompetenceCourse}" value="#{scouncilBundle['transfer']}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" action="competenceCoursesManagement" value="#{scouncilBundle['back']}" />
		<h:outputText value="<p/>" escape="false"/>
	</h:form>
</f:view>
