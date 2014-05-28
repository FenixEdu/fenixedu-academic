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

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications$CreateExecutionDegree" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>

	<h:outputText value="<h2>Criar #{managerResources['label.manager.executionDegreeManagement']}</h2>" escape="false"/>

	<h:outputText styleClass="success0" rendered="#{!empty createExecutionDegrees.createdDegreeCurricularPlans}" value="Os seguintes planos curriculares foram criados correctamente:"/>
	<fc:dataRepeater value="#{createExecutionDegrees.createdDegreeCurricularPlans}" var="degreeCurricularPlan">
		<h:outputText value="<p>#{degreeCurricularPlan.name}</p>" escape="false"/>
	</fc:dataRepeater>	

	<p>
	<h:messages errorClass="error0" infoClass="success0"/>
	</p>
	
	<h:form>
	<p>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="back" value="#{managerResources['label.return']}" styleClass="inputbutton" />
	</p>	
	</h:form>

</f:view>



