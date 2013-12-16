<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="df.executionDegreeManagement.default" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>

	<h:outputText value="<h2>Criar #{managerResources['label.manager.executionDegreeManagement']}</h2>" escape="false"/>

	<h:form>

		<div class='simpleblock4'>
		<fieldset class='lfloat'>
		<p>
			<label>
				<strong><h:outputText value="#{managerResources['label.manager.degree.tipoCurso']}"/></strong>:
			</label>
			<h:selectOneMenu value="#{createExecutionDegrees.chosenDegreeType}" >
				<f:selectItems value="#{createExecutionDegrees.degreeTypes}" />			
			</h:selectOneMenu>
		</p>
		</fieldset>
		</div>
		<p>
			<h:commandButton alt="#{htmlAltBundle['commandButton.Continuar']}" action="choose" value="Continuar" styleClass="inputbutton" />
		</p>			
	</h:form>

</ft:tilesView>



