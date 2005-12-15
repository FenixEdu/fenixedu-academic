<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CurricularPlanManagement.personDepartmentName}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['createCurricularPlan']}"/></h2>	
	<h:form>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['degree']}: " />
			<h:selectOneMenu value="">
				<%-- <f:selectItems value="#{CompetenceCourseManagement.regimeTypes}" /> --%>
			</h:selectOneMenu>
		
			<h:outputText value="#{bolonhaBundle['name']}: " />
			<h:inputText required="true" maxlength="100" size="60"/>
				
			<h:outputText value="#{bolonhaBundle['degreeType']}: " />
			<h:selectOneMenu value="">
				<%-- <f:selectItems value="#{CompetenceCourseManagement.regimeTypes}" /> --%>
			</h:selectOneMenu>
			
			<h:outputText value="#{bolonhaBundle['ectsCredits']}: " />
			<h:inputText required="true" maxlength="5" size="5"/>
		</h:panelGrid>
		<br/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
			action="#{CurricularPlanManagement.createCurricularPlan}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>