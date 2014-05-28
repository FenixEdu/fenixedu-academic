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

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications$CreateExecutionDegree" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>

	<h:outputText value="<h2>Criar #{managerResources['label.manager.executionDegreeManagement']}</h2>" escape="false"/>

	<h:form>

		<div class='simpleblock4'>
		<fieldset class='lfloat'>
		<p>
			<label>
				<strong><h:outputText value="#{managerResources['label.manager.degree.tipoCurso']}"/></strong>:
			</label>
			<h:selectOneMenu value="#{createExecutionDegrees.chosenDegreeType}" >
				<f:selectItems value="#{createExecutionDegrees.degreeTypes}" />			
			</h:selectOneMenu>
		</p>
		</fieldset>
		</div>
		<p>
			<h:commandButton alt="#{htmlAltBundle['commandButton.Continuar']}" action="choose" value="Continuar" styleClass="inputbutton" />
		</p>			
	</h:form>

</f:view>



