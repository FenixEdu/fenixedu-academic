<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.executionDegreeManagement.firstPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>

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
			<h:commandButton action="choose" value="Continuar" styleClass="inputbutton" />
		</p>			
	</h:form>

</ft:tilesView>



