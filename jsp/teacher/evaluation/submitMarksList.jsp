<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

	<script>
		function selectAll(){
			var checkBox = document.forms.form.selectedMarks;
			for(var i = 0; i < checkBox.length; i++){
				checkBox.item(i).checked = true;
			}
		}
		
		function selectNone(){
			var checkBox = document.forms.form.selectedMarks;
			for(var i = 0; i < checkBox.length; i++){
				checkBox.item(i).checked = false;
			}
		}
	</script>
<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['label.submit.listMarks']}</h2>" escape="false"/>
	<h:panelGrid styleClass="infoop" columns="1">
		<h:outputText value="#{bundle['label.submitMarks.introduction']}" escape="false"/>
	</h:panelGrid>	
	<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
				value="#{bundle[evaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />	
	<h:outputFormat value="<p><b>#{bundle['label.submitedMarks']}</b></p>" escape="false"/>
	<h:outputFormat rendered="#{empty evaluationManagementBackingBean.alreadySubmitedMarks}" value="#{bundle['message.noSubmitedMarks']}" />								
	<h:dataTable rendered="#{!empty evaluationManagementBackingBean.alreadySubmitedMarks}" value="#{evaluationManagementBackingBean.alreadySubmitedMarks}" var="mark" headerClass="listClasses-header" columnClasses="listClasses">
		<h:column>
			<f:facet name="header"><h:outputText value="#{bundle['label.number']}"/></f:facet>
			<h:outputText value="#{mark.attend.aluno.number}" />
		</h:column>
		<h:column>
			<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
			<h:outputText value="#{mark.attend.aluno.person.nome}" />
		</h:column>
		<h:column>
			<f:facet name="header"><h:outputText value="#{bundle['label.submitedMark']}"/></f:facet>
			<h:outputText value="#{mark.submitedMark}" />
		</h:column>
		<h:column>
			<f:facet name="header"><h:outputText value="#{bundle['label.submitDate']}"/></f:facet>
			<h:outputFormat value="{0, date, dd/MM/yyyy} " >
				<f:param value="#{mark.submitDate}" />
			</h:outputFormat>
		</h:column>
		<h:column>
			<f:facet name="header"><h:outputText value="#{bundle['label.whenSubmited']}"/></f:facet>
			<h:outputFormat value="{0, date, dd/MM/yyyy} " >
				<f:param value="#{mark.whenSubmited}" />
			</h:outputFormat>
		</h:column>
	</h:dataTable>
	
	<h:outputFormat value="<p><b>#{bundle['label.marksToSubmit']}</b></p>" escape="false"/>	
	<h:outputFormat rendered="#{empty evaluationManagementBackingBean.notSubmitedMarks}" value="#{bundle['message.noMarkToSubmit']}" />			
	<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.notSubmitedMarks}">
		<h:panelGrid styleClass="infoop" columns="1">
			<h:outputText value="#{bundle['label.submitMarks.instructions']}" escape="false"/>
		</h:panelGrid>	
		<h:outputText value="<br/>" escape="false"/>
	
		<h:form id="form">
			<fc:viewState binding="#{evaluationManagementBackingBean.viewState}"/>
			<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
			<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
			<h:outputText escape="false" value='<p><a href="javascript:selectAll()"> #{bundle["button.selectAll"]}</a>'/>
			<h:outputText escape="false" value=' | '/>						
			<h:outputText escape="false" value='<a href="javascript:selectNone()"> #{bundle["button.selectNone"]}</a></p>'/>			
			<h:dataTable value="#{evaluationManagementBackingBean.notSubmitedMarks}" var="attend" headerClass="listClasses-header" columnClasses="listClasses">
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.toSubmit']}"/></f:facet>
					<h:outputText rendered="#{(!empty attend.finalMark) && (!empty attend.finalMark.mark)}" escape="false" value="<input alt='input.selectedMarks' type='checkbox' id='selectedMarks' name='selectedMarks' checked='checked' value='#{attend.idInternal}'/>"/>
					<h:outputText rendered="#{(empty attend.finalMark) || (empty attend.finalMark.mark)}" escape="false" value="<input alt='input.selectedMarks' type='checkbox' id='selectedMarks' name='selectedMarks' value='#{attend.idInternal}'/>"/>				
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.number']}"/></f:facet>
					<h:outputText value="#{attend.aluno.number}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
					<h:outputText value="#{attend.aluno.person.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.markToSubmit']}"/></f:facet>
					<h:outputText rendered="#{(!empty attend.finalMark) && (!empty attend.finalMark.mark)}" value="#{attend.finalMark.mark}"/>
					<h:outputText rendered="#{(empty attend.finalMark) || (empty attend.finalMark.mark)}" value=""/>				
				</h:column>
			</h:dataTable>
			<h:outputText escape="false" value='<p><a href="javascript:selectAll()"> #{bundle["button.selectAll"]}</a>'/>
			<h:outputText escape="false" value=' | '/>						
			<h:outputText escape="false" value='<a href="javascript:selectNone()"> #{bundle["button.selectNone"]}</a></p>'/>			
			<h:outputText value="<br/>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.continue']}" styleClass="inputbutton" action="#{evaluationManagementBackingBean.submitMarks}" value="#{bundle['button.continue']}"/>
		</h:form>
	</h:panelGroup>		 
</ft:tilesView>
