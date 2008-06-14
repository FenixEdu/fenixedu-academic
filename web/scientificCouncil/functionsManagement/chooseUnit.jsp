<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">	
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

</ft:tilesView>