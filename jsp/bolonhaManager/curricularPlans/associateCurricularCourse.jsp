<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CurricularCourseManagement.personDepartmentName}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['associateCurricularCourse']}"/></h2>		
	<h:outputText value="ROOT_PATH_HERE" /><br/>
	<h:form>	
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['curricularCourse']}: " />
			<h:selectOneMenu value="">
				<%-- <f:selectItems value="#{CurricularCourseManagement.regimeTypes}" /> --%>
			</h:selectOneMenu>
		</h:panelGrid>
		<br/><br/>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['context']}: "/><br/>
		<h:outputText value="ROOT_PATH_HERE" /><br/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
			<h:selectOneMenu value="">
				<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
			</h:selectOneMenu>
			
			<h:outputText value="#{bolonhaBundle['semester']}: " />
			<h:selectOneMenu value="">
				<f:selectItems value="#{CurricularCourseManagement.semesters}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<br/><br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
			action="#{CurricularCourseManagement.associateCurricularCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>