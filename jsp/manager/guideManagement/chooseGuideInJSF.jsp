<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>


<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<h:form>
		<h:panelGrid columns="3">
		
			<h:outputLabel for="guideYearInput">
				<h:outputText value="Year" />
			</h:outputLabel>
			<h:inputText id="guideYearInput" value="#{guideEdition.guideYear}" required="true" size="6">
				<f:validateLength minimum="4" maximum="4"/>
			</h:inputText>
			<h:message for="guideYearInput"/>
			
			<h:outputLabel for="guideNumberInput">
				<h:outputText value="Number" />
			</h:outputLabel>
			<h:inputText id="guideNumberInput" value="#{guideEdition.guideNumber}" required="true" size="6"/>
			<h:message for="guideNumberInput"/>

			<h:outputLabel for="guideVersionInput">
				<h:outputText value="Version" />
			</h:outputLabel>
			<h:inputText id="guideVersionInput" value="#{guideEdition.guideVersion}" size="6"/>

			<h:panelGroup/>
			<h:commandButton action="#{guideEdition.chooseGuide}" value="Choose"/>
			<h:panelGroup/>

		</h:panelGrid>
	</h:form>
</ft:tilesView>

