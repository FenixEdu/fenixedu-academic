<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ManagerResources" var="bundle"/>
	<h:form>	

		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.chooseUnitIDHidden}"/>	
		
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
										
		<h:outputText value="<h2>#{bundle['title.chooseFunction']}</h2><br/>" escape="false" />		
		
		<h:panelGrid styleClass="infoselected" columns="2">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.chooseUnit.name}" escape="false"/>												
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />				
		
		<h:outputText value="<br/>" escape="false" />	
		<h:commandButton action="#{organizationalStructureBackingBean.associateParentUnit}" immediate="true" 
			value="#{bundle['link.functions.management.new']}" styleClass="inputbutton"/>
		<h:commandButton action="listAllUnitsToChooseParentUnit" immediate="true" 
			value="#{bundle['label.return']}" styleClass="inputbutton"/>								
				
	</h:form>
</ft:tilesView>