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
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication$ScientificCurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>

	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>"
		escape="false">
		<f:param value="#{scouncilBundle['label.name.specializationArea']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.specializationId' id='specializationId' name='specializationId' type='hidden' value='#{DegreeManagement.officialPublicationBean.specializationIdToDelete}'/>"/>
			<h:outputText escape="false"
		value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />
	<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />

		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<div class='infoop2'/>" escape="false"/>
		<h:outputText value="<p>#{scouncilBundle['label.name.specializationArea.name']}: " escape="false"/>
		<h:outputText value="<b>#{DegreeManagement.officialPublicationBean.specializationAreaToDelete.name}</b></p>" escape="false"/>


		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirm']}"
			styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{DegreeManagement.officialPublicationBean.removeSpecializationAreaToDelete}"
			onclick="return confirm('#{scouncilBundle['confirm.delete.specializationArea']}')" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}"
			immediate="true" styleClass="inputbutton"
			value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement" />
		<h:outputText value="</p>" escape="false"/>

		<h:outputLink
			value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editDegreeOfficialPublication.faces">
			<h:outputFormat value="#{scouncilBundle['return']}" />
			<f:param name="officialPubId" value="#{DegreeManagement.officialPublicationBean.degreeOfficialPublicationGoBack.externalId}" />
			<f:param name="degreeId" value="#{ DegreeManagement.degreeId}" />
		<f:param name="selectedExecutionYearId"
				value="#{ DegreeManagement.selectedExecutionYearId}" />
		</h:outputLink>
	</h:form>


</f:view>
