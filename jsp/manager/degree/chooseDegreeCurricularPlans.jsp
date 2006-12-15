<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.executionDegreeManagement.default" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>
	
	<h:outputText styleClass="success0" rendered="#{!empty createExecutionDegrees.createdDegreeCurricularPlans}" value="Os seguintes planos curriculares foram criados correctamente:"/>
	<fc:dataRepeater value="#{createExecutionDegrees.createdDegreeCurricularPlans}" var="degreeCurricularPlan">
		<h:outputText value="<p>#{degreeCurricularPlan.name}</p>" escape="false"/>
	</fc:dataRepeater>

	<p>
	<h:messages errorClass="error0" infoClass="success0"/>
	</p>
	
	<h:form>
		<h:inputHidden value="#{createExecutionDegrees.chosenDegreeType}" />
		<fc:viewState binding="#{createExecutionDegrees.viewState}"/>
						
		<h:outputText value="<strong>Planos Curriculares Prï¿½-Bolonha:</strong>" escape="false" />

		<h:panelGroup rendered="#{!empty createExecutionDegrees.degreeCurricularPlansSelectItems.value}">
			<h:selectManyCheckbox value="#{createExecutionDegrees.choosenDegreeCurricularPlansIDs}" layout="pageDirection" >
				<f:selectItems binding="#{createExecutionDegrees.degreeCurricularPlansSelectItems}" />
			</h:selectManyCheckbox>
		</h:panelGroup>		
		<h:panelGroup rendered="#{empty createExecutionDegrees.degreeCurricularPlansSelectItems.value}">
			<h:outputText value="<p><em>Não existem planos curriculares activos</em></p>" escape="false" />
		</h:panelGroup>

		<br/>
		
		<h:outputText value="<strong>Planos Curriculares de Bolonha:</strong>" escape="false" />
		<h:panelGroup rendered="#{!empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:selectManyCheckbox value="#{createExecutionDegrees.choosenBolonhaDegreeCurricularPlansIDs}" layout="pageDirection" >
				<f:selectItems binding="#{createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems}" />
			</h:selectManyCheckbox>
		</h:panelGroup>
		<h:panelGroup rendered="#{empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:outputText value="<p><em>Não existem planos curriculares activos e aprovados ou publicados</em></p>" escape="false" />
		</h:panelGroup>

		<br/>
		
		<h:panelGroup rendered="#{!empty createExecutionDegrees.degreeCurricularPlansSelectItems.value || !empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:outputText value="<fieldset class='lfloat3'>" escape="false"/>

				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
				<h:outputText value="<p><label>Ano de execução:</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.choosenExecutionYearID}" onchange="this.form.submit();" valueChangeListener="#{createExecutionDegrees.onChoosenExecutionYearChanged}">
						<f:selectItems value="#{createExecutionDegrees.executionYears}" />
					</h:selectOneMenu>
					<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Campus:</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.campus}" >
						<f:selectItems value="#{createExecutionDegrees.allCampus}" />
					</h:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Mapa de exames temporï¿½rio:</label>" escape="false"/>
					<h:selectBooleanCheckbox value="#{createExecutionDegrees.temporaryExamMap}" />		
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="</div>" escape="false"/>

				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
				<h:outputText value="<p><label><strong>1ï¿½ Semestre</strong></label></p><br/>" escape="false"/>
				<h:outputText value="<p><label>Aulas</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.lessonSeason1BeginYear}"/>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.lessonSeason1EndYear}"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Exames</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.examsSeason1BeginYear}"/>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.examsSeason1EndYear}"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Prazo Lanï¿½amento Notas</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason1EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason1EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.gradeSubmissionNormalSeason1EndYear}"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="</div>" escape="false"/>
					
				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
				<h:outputText value="<p><label><strong>2ï¿½ Semestre</strong></label></p><br/>" escape="false"/>
				<h:outputText value="<p><label>Aulas</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.lessonSeason2BeginYear}"/>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.lessonSeason2EndYear}"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Exames</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.examsSeason2BeginYear}"/>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.examsSeason2EndYear}"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Prazo Lanï¿½amento Notas</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason2EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason2EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.gradeSubmissionNormalSeason2EndYear}"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="</div>" escape="false"/>

				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
				<h:outputText value="<p><label><strong>Época Especial</strong></label></p><br/>" escape="false"/>
				<h:outputText value="<p><label>Exames</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonBeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonBeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.examsSpecialSeasonBeginYear}"/>					
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonEndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonEndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.examsSpecialSeasonEndYear}"/>					
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Prazo Lanï¿½amento Notas</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionSpecialSeasonEndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionSpecialSeasonEndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value=" / " />
					<h:outputText value="#{createExecutionDegrees.gradeSubmissionSpecialSeasonEndYear}"/>										
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="</div>" escape="false"/>

			<h:outputText value="</fieldset>" escape="false"/>
			
			<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" action="#{createExecutionDegrees.createExecutionDegrees}" value="#{managerResources['label.create']}" styleClass="inputbutton"/>

		</h:panelGroup>
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="back" value="#{managerResources['label.return']}" immediate="true" styleClass="inputbutton"/>
	</h:form>

</ft:tilesView>



