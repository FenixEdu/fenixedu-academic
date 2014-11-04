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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.manager.ManagerApplications$OrganizationalStructurePage" />

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

	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
	<h:form>	
		<h:inputHidden binding="#{organizationalStructureBackingBean.listingTypeValueToUnitsHidden}"/>
		
		<h:outputText value="<h2>#{bundle['title.manager.organizationalStructureManagement']}</h2><br/>" escape="false" />
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
				
		<h:outputText value="<div class=\"alert alert-info\">" escape="false"/>
		<h:outputText value="#{bundle['title.organizationalStructureManagement.information']}" escape="false"/>
		<h:outputText value="</div>" escape="false"/>

		<h:commandLink value="#{bundle['link.new.unit']}" action="prepareCreateNewUnit" />														
			
		<h:outputText value="<br/><br/><h3>#{bundle['title.all.units']}</h3><br/>" escape="false"/>
		
		<h:outputText value="<b>#{bundle['message.unitListingType']}</b>" escape="false"/>
		<fc:selectOneMenu value="#{organizationalStructureBackingBean.listingTypeValueToUnits}" 
			onchange="this.form.submit();">
			<f:selectItems value="#{organizationalStructureBackingBean.listingTypeToUnits}"/>				
		</fc:selectOneMenu>		
		
		<h:outputText value="<br/><br/>" escape="false"/>									
		<h:outputText value="<b>#{bundle['message.viewExternalUnits']}</b>" escape="false"/>	
		<h:selectBooleanCheckbox value="#{organizationalStructureBackingBean.viewExternalUnits}" onclick="this.form.submit()"/>
		
		<h:outputText value="<br/><br/>" escape="false"/>									
		<h:outputText value="<b>#{bundle['message.viewUnitsWithoutParents']}</b>" escape="false"/>	
		<h:selectBooleanCheckbox value="#{organizationalStructureBackingBean.viewUnitsWithoutParents}" onclick="this.form.submit()"/>
			
		<h:outputText value="<br/><br/>#{organizationalStructureBackingBean.units}" escape="false"/>
				
	</h:form>
</f:view>