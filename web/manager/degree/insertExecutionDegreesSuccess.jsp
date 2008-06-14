<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.executionDegreeManagement.default" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>

	<h:outputText value="<h2>Criar #{managerResources['label.manager.executionDegreeManagement']}</h2>" escape="false"/>

	<h:outputText styleClass="success0" rendered="#{!empty createExecutionDegrees.createdDegreeCurricularPlans}" value="Os seguintes planos curriculares foram criados correctamente:"/>
	<fc:dataRepeater value="#{createExecutionDegrees.createdDegreeCurricularPlans}" var="degreeCurricularPlan">
		<h:outputText value="<p>#{degreeCurricularPlan.name}</p>" escape="false"/>
	</fc:dataRepeater>	

	<p>
	<h:messages errorClass="error0" infoClass="success0"/>
	</p>
	
	<h:form>
	<p>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="back" value="#{managerResources['label.return']}" styleClass="inputbutton" />
	</p>	
	</h:form>

</ft:tilesView>



