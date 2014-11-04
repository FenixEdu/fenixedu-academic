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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<f:view>	
	<script language="JavaScript">
		function check(e,v)
		{	
			var contextPath = '<%= request.getContextPath() %>';	
			if (e.style.display == "none")
			  {
			  e.style.display = "";
			  v.src = contextPath + '/images/toggle_minus10.gif';
			  }
			else
			  {
			  e.style.display = "none";
			  v.src = contextPath + '/images/toggle_plus10.gif';
			  }
		}
	</script>

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.personIDHidden}"/>
				
		<h:outputText value="<h2>#{bundle['label.chooseUnit']}</h2>" escape="false"/>		

	

		<h:outputText value="<p class='mtop2'><b>#{bundle['label.name']}:</b> " escape="false"/>		
		<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.person.name}</p>" escape="false"/>		

						
		<h:outputText value="<p class='mtop15'><strong>#{bundle['label.units']}:</strong></p>" escape="false"/>		
					
		<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.units}" escape="false"/>
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.person']}" styleClass="inputbutton" value="#{bundle['button.choose.new.person']}" action="backToList"/>
						    				
	</h:form>

</f:view>