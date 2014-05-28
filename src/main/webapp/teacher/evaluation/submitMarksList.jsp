<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
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
	
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
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
			<h:outputText value="#{mark.attend.registration.number}" />
		</h:column>
		<h:column>
			<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
			<h:outputText value="#{mark.attend.registration.person.name}" />
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
					<h:outputText rendered="#{(!empty attend.finalMark) && (!empty attend.finalMark.mark)}" escape="false" value="<input alt='input.selectedMarks' type='checkbox' id='selectedMarks' name='selectedMarks' checked='checked' value='#{attend.externalId}'/>"/>
					<h:outputText rendered="#{(empty attend.finalMark) || (empty attend.finalMark.mark)}" escape="false" value="<input alt='input.selectedMarks' type='checkbox' id='selectedMarks' name='selectedMarks' value='#{attend.externalId}'/>"/>				
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.number']}"/></f:facet>
					<h:outputText value="#{attend.registration.number}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
					<h:outputText value="#{attend.registration.person.name}" />
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
</f:view>
