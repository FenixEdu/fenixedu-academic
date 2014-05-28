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

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.BolonhaManagerApplication$CompetenceCoursesManagement"/>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
		<h:outputText value="<em>#{bolonhaBundle['competenceCourse']}</em>" escape="false"/>
		<h:outputText value="<h2>#{bolonhaBundle['delete']}: #{CompetenceCourseManagement.competenceCourse.name} " style="font-weight: bold" escape="false"/>
		<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.acronym}" value="(#{CompetenceCourseManagement.competenceCourse.acronym})" style="font-weight: bold" escape="false"/>
		<h:outputText value="</h2>" style="font-weight: bold" escape="false"/>		
	<h:form>

		<h:outputText value="<p class='mtop15'><span class='bold'>#{bolonhaBundle['department']}: </span>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}</p>" escape="false"/>

		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
			<h:outputText value="<p><span class='bold'>#{bolonhaBundle['area']}: </span>" escape="false"/>
			<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</p>" escape="false"/>
		</fc:dataRepeater>		

		<h:outputText value="<p class='mtop15'><span class='bold'>#{bolonhaBundle['name']} (pt):</span>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}</p>" escape="false"/>
		<h:outputText value="<p><span class='bold'>#{bolonhaBundle['nameEn']} (en):</span>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}</p>" escape="false"/>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.acronym}">
			<h:outputText value="<p><span class='bold'>#{bolonhaBundle['acronym']}:</span>" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}</p>" escape="false"/>	
		</h:panelGroup>
		
		<h:messages infoClass="success0" errorClass="error0" layout="table"/>
		
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>

		<h:outputText value="<p class='mtop15'>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['confirmDeleteMessage']}" styleClass="warning0" escape="false"/>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p>" escape="false"/>	
			<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" styleClass="inputbutton" value="#{bolonhaBundle['yes']}" action="#{CompetenceCourseManagement.deleteCompetenceCourse}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}" action="competenceCoursesManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</f:view>