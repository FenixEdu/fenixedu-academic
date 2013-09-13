<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

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

		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.functionIDHidden}"/>
		
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
				
		<h:outputText value="<h2>#{bundle['title.chooseUnit']}</h2><br/>" escape="false" />			
		<h:outputText value="#{organizationalStructureBackingBean.unitsToChoosePrincipalFunction}" escape="false"/>
		
		<h:outputText value="<br/>" escape="false" />	
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="backToUnitDetails" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
				
	</h:form>
</ft:tilesView>