<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>


<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">

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
	
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
	
	<h:form>	

		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
																	
		<h:outputText value="<h2>#{bundle['title.chooseUnit']}</h2><br/>" escape="false"/>
			
		<h:outputText value="#{organizationalStructureBackingBean.allUnitsToChooseParentUnit}" escape="false"/>
		
		<h:outputText value="<br/>" escape="false" />		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="backToUnitDetails" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>
				
	</h:form>
</ft:tilesView>