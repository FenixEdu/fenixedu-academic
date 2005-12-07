<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h2><h:outputText value="#{bolonhaBundle['manageCurricularPlans']}"/></h2>
	
	<h:outputLink value="createCurricularPlan.faces">
		<h:outputText />
	</h:outputLink>
	
</ft:tilesView>