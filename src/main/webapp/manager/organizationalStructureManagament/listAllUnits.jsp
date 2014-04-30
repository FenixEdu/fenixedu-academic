<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications$OrganizationalStructurePage" />

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