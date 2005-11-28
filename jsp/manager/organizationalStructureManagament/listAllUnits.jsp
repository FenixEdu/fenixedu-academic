<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ManagerResources" var="bundle"/>
	<h:form>	

		<h:outputText value="<h2>#{bundle['title.manager.organizationalStructureManagement']}</h2><br/>" escape="false" />
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
				
		<h:outputText value="<div class=\"infoselected\">" escape="false"/>
		<h:outputText value="#{bundle['title.organizationalStructureManagement.information']}" escape="false"/>
		<h:outputText value="</div>" escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>									
									
		<h:commandLink value="#{bundle['link.new.unit']}" action="prepareCreateNewUnit" />						
			
		<h:outputText value="<br/><br/><h3>#{bundle['title.all.units']}</h3>" escape="false"/>
			
		<h:outputText value="#{organizationalStructureBackingBean.units}" escape="false"/>
				
	</h:form>
</ft:tilesView>