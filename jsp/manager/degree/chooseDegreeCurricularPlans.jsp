<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.executionDegreeManagement.firstPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>
	<h:form>
		<h:inputHidden value="#{createExecutionDegrees.chosenDegreeType}" />
						
		<h:outputText value="<strong>Escolha o(s) Plano(s) Curricular(es) Antigos:</strong>" escape="false" />
		<h:selectManyCheckbox value="#{createExecutionDegrees.choosenDegreeCurricularPlansIDs}" layout="pageDirection" >
			<f:selectItems binding="#{createExecutionDegrees.degreeCurricularPlansSelectItems}" />
		</h:selectManyCheckbox>
		
		<br/>
		
		<h:outputText value="<strong>Escolha o(s) Plano(s) Curricular(es) Novos:</strong>" escape="false" />
		<h:selectManyCheckbox value="#{createExecutionDegrees.choosenBolonhaDegreeCurricularPlansIDs}" layout="pageDirection" >
			<f:selectItems binding="#{createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems}" />
		</h:selectManyCheckbox>

		<br/>
		
		<p>
		<h:panelGrid columns="2">
			<h:outputText value="<strong>Ano lectivo de execução:</strong>" escape="false" />
			<h:selectOneMenu value="#{createExecutionDegrees.choosenExecutionYearID}">
				<f:selectItems value="#{createExecutionDegrees.executionYears}" />
			</h:selectOneMenu>
						
			<h:outputText value="<strong>Campus:</strong>" escape="false" />
			<h:selectOneMenu value="#{createExecutionDegrees.campus}" >
				<f:selectItems value="#{createExecutionDegrees.allCampus}" />
			</h:selectOneMenu>
				
			<h:outputText value="<strong>Mapa de exames temporário:<strong>" escape="false" />
			<h:selectBooleanCheckbox value="#{createExecutionDegrees.temporaryExamMap}" />		
		</h:panelGrid>
		<p/>
		
		<br/>
		<p>
		<h:panelGrid columns="12">
		 	<h:outputText value="<strong>Periodo Aulas 1º Semestre:</strong>" escape="false"/>
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>
			<h:outputText value=" a " />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>
			
		 	<h:outputText value="<strong>Periodo Exames 1º Semestre:</strong>" escape="false"/>
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>
			<h:outputText value=" a " />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>					
		
		 	<h:outputText value="<strong>Periodo Aulas 2º Semestre:<strong>" escape="false" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>
			<h:outputText value=" a " />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>
		
		 	<h:outputText value="<strong>Periodo Exames 2º Semestre:</strong>" escape="false" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>
			<h:outputText value=" a " />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndDay}">
				<f:selectItems value="#{createExecutionDegrees.days}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndMonth}">
				<f:selectItems value="#{createExecutionDegrees.months}" />
			</h:selectOneMenu>
			<h:outputText value="/" />
			<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndYear}">
				<f:selectItems value="#{createExecutionDegrees.years}" />
			</h:selectOneMenu>	
		</h:panelGrid>
		<p/>
			
		<h:commandButton action="#{createExecutionDegrees.createExecutionDegrees}"
			value="#{managerResources['label.create']}" styleClass="inputbutton"/>
				
		<h:commandButton action="back" value="#{managerResources['label.return']}" immediate="true" styleClass="inputbutton"/>
	</h:form>

</ft:tilesView>



