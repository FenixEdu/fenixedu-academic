<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
		
	<h:form>	

		<h:outputText value="<h2>#{bundle['link.new.unit2']}</h2><br/>" escape="false" />
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
		
		<h:panelGrid columns="2" styleClass="infoop">		
		
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="name" required="true" size="30" value="#{organizationalStructureBackingBean.unitName}"/>
				<h:message for="name" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.costCenter']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="costCenter" size="10" value="#{organizationalStructureBackingBean.unitCostCenter}">
					<fc:regexValidator regex="[0-9]*"/>
				</h:inputText>
				<h:message for="costCenter" styleClass="error"/>
			</h:panelGroup>	
			
			<h:outputText value="<b>#{bundle['message.acronym']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText size="10" value="#{organizationalStructureBackingBean.unitAcronym}"/>				
			</h:panelGroup>									
			
			<h:outputText value="<b>#{bundle['message.webAddress']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText size="30" value="#{organizationalStructureBackingBean.unitWebAddress}"/>				
			</h:panelGroup>		
			
			<h:outputText value="<b>#{bundle['message.initialDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="beginDate" required="true" size="10" value="#{organizationalStructureBackingBean.unitBeginDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>
				<h:outputText value="#{bundle['date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.endDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="endDate" size="10" value="#{organizationalStructureBackingBean.unitEndDate}"/>
				<h:outputText value="#{bundle['date.format']}"/>				
			</h:panelGroup>
		
			<h:outputText value="<b>#{bundle['message.uniType']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitTypeName}">
				<f:selectItems value="#{organizationalStructureBackingBean.validUnitType}"/>				
			</fc:selectOneMenu>
			
			<h:outputText value="<b>#{bundle['message.department']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.departmentID}">
				<f:selectItems value="#{organizationalStructureBackingBean.departments}"/>				
			</fc:selectOneMenu>
			
			<h:outputText value="<b>#{bundle['message.degree']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.degreeID}">
				<f:selectItems value="#{organizationalStructureBackingBean.degrees}"/>				
			</fc:selectOneMenu>
			
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton action="#{organizationalStructureBackingBean.createTopUnit}" value="#{bundle['link.new.unit2']}" styleClass="inputbutton"/>				
			<h:commandButton action="listAllUnits" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
		</h:panelGrid>		
				
	</h:form>
	
</ft:tilesView>