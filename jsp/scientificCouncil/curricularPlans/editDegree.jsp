<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputText value="<i>#{ScientificCouncilDegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{ScientificCouncilDegreeManagement.degree.acronym})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeId' name='degreeId' type='hidden' value='#{ScientificCouncilDegreeManagement.degreeId}'/>"/>

		<h:outputText styleClass="error0" rendered="#{!empty ScientificCouncilDegreeManagement.errorMessage}"
			value="#{ScientificCouncilDegreeManagement.errorMessage}<br/>" escape="false"/>
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>			

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{scouncilBundle['degree.data']}:</h4><br/>" escape="false"/>

		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>

		<h:outputText value="<p><label><span class='required'>*</span> #{scouncilBundle['name']} (pt):</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="name" value="#{ScientificCouncilDegreeManagement.name}" maxlength="100" size="60"/>
			<h:message for="name" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label><span class='required'>*</span> #{scouncilBundle['name']} (en):</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="nameEn" value="#{ScientificCouncilDegreeManagement.nameEn}" maxlength="100" size="60"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label><span class='required'>*</span> #{scouncilBundle['acronym']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="acronym" value="#{ScientificCouncilDegreeManagement.acronym}" maxlength="6" size="6"/>
			<h:message for="acronym" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label><span class='required'>*</span> #{scouncilBundle['degreeType']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:selectOneMenu id="bolonhaDegreeType" value="#{ScientificCouncilDegreeManagement.bolonhaDegreeType}" onchange="this.form.submit();" disabled="#{ScientificCouncilDegreeManagement.degree.degreeCurricularPlansCount > 0}">
				<f:selectItems value="#{ScientificCouncilDegreeManagement.bolonhaDegreeTypes}" />
			</h:selectOneMenu>
			<h:message for="bolonhaDegreeType" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['ectsCredits']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="ectsCredits" value="#{ScientificCouncilDegreeManagement.ectsCredits}" disabled="true" maxlength="3" size="2"/>
			<h:message for="ectsCredits" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['prevailingScientificArea']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="prevailingScientificArea" value="#{ScientificCouncilDegreeManagement.prevailingScientificArea}" maxlength="100" size="60"/>
			<h:message for="prevailingScientificArea" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p class='mtop2'><label class='lempty'>.</label><span class='smalltxt'>#{scouncilBundle['mandatory.fields']}</span>" escape="false"/>

		<h:outputText value="</fieldset></div>" escape="false"/>

		<h:outputText value="<p>" escape="false"/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{ScientificCouncilDegreeManagement.editDegree}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="<p>" escape="false"/>
	</h:form>

</ft:tilesView>
