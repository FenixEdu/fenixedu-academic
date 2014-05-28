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

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<i>#{DegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{DegreeManagement.degree.sigla})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['view.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>

	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:outputText value="<div class='simpleblock4'> " escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
	<h:outputText value="<p><label>#{scouncilBundle['name']} (pt):</label> " escape="false"/>
	<h:outputText id="name" value="#{DegreeManagement.name}</p>" escape="false"/>
	
	<h:outputText value="<p><label>#{scouncilBundle['name']} (en):</label> " escape="false"/>
	<h:outputText id="nameEn" value="#{DegreeManagement.nameEn}</p>" escape="false"/>
	
	<h:outputText value="<p><label>#{scouncilBundle['acronym']}:</label> " escape="false"/>
	<h:outputText id="acronym" value="#{DegreeManagement.acronym}</p>" escape="false"/>
	
	<h:outputText value="<p><label>#{scouncilBundle['degreeType']}:</label> " escape="false"/>
	<h:outputText id="bolonhaDegreeType" value="#{enumerationBundle[DegreeManagement.bolonhaDegreeType]}</p>" escape="false"/>

	<h:outputText value="<p><label>#{scouncilBundle['ectsCredits']}:</label> " escape="false"/>
	<h:outputText id="ectsCredits" value="#{DegreeManagement.ectsCredits}</p>" escape="false"/>
	<h:outputText value="</fieldset></div>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>"/>
		<h:outputText value="<p> " escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['return']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p> " escape="false"/>
	</h:form>

</f:view>