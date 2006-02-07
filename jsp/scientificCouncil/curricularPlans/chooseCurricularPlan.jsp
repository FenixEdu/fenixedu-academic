<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>


<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:form>
		
		<h:selectOneMenu value="#{CurricularPlansMembersManagementBackingBean.selectedCurricularPlanID}">
			<f:selectItems value="#{CurricularPlansMembersManagementBackingBean.degreeCurricularPlans}" />
		</h:selectOneMenu>	
		
		<h:commandButton value=":p" action="curricularPlanChoosed" />
			
		
	</h:form>
	

	
</ft:tilesView>