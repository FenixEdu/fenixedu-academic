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
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication$ScientificCurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>"/>

<%--
		<h:outputText value="#{scouncilBundle['degree.data']}:<br/>" escape="false"/>
--%>	

	<h:outputText value="<p class='mvert2'><strong>#{DegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{DegreeManagement.degree.sigla})</strong></p>" escape="false"/>


		<h:outputText styleClass="error" rendered="#{!empty DegreeManagement.errorMessage}"
			value="#{DegreeManagement.errorMessage}<br/><br/>" escape="false"/>
		
			<h:outputText value="<div class='infoop2'/>" escape="false"/>
			<h:outputText value="<p>#{scouncilBundle['name']} (pt): " escape="false"/>
			<h:outputText id="name" value="#{DegreeManagement.name}</p>" escape="false"/>
			
			<h:outputText value="<p>#{scouncilBundle['name']} (en): " escape="false"/>
			<h:outputText id="nameEn" value="#{DegreeManagement.nameEn}</p>" escape="false"/>
			
			<h:outputText value="<p>#{scouncilBundle['acronym']}: " escape="false"/>
			<h:outputText id="acronym" value="#{DegreeManagement.acronym}</p>" escape="false"/>
			
			<h:outputText value="<p>#{scouncilBundle['degreeType']}: " escape="false"/>
			<h:outputText id="bolonhaDegreeType" value="#{DegreeManagement.degreeType.name.content}</p>" escape="false"/>

			<h:outputText value="<p>#{scouncilBundle['ectsCredits']}: " escape="false"/>
			<h:outputText id="ectsCredits" value="#{DegreeManagement.ectsCredits}</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>

		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirm']}" styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{DegreeManagement.deleteDegree}" onclick="return confirm('#{scouncilBundle['confirm.delete.degree']}')"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</f:view>