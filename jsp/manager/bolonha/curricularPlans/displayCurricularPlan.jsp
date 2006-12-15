<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">

	<f:loadBundle basename="resources/DegreeAdministrativeOfficeResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>

	<h:dataTable value="#{displayCurricularPlan.scopes}" var="degreeCurricularPlans" styleClass="fullWidthClass">
		<h:column>
			<h:dataTable value="#{degreeCurricularPlans}" var="degreeCurricularPlan" styleClass="fullWidthClass">
				<h:column>
					<h:panelGrid columns="2" columnClasses="dcpName" >
						<h:outputText value="#{degreeCurricularPlan.degreeName}" /><h:outputText value="#{displayCurricularPlan.choosenExecutionYear}"/>
					</h:panelGrid>					
					<h:dataTable value="#{degreeCurricularPlan.years}" var="year" styleClass="fullWidthClass">
						<h:column>
							<h:panelGroup styleClass="year">
								<h:outputText value="<br/>" escape="false"/><h:outputText value="#{bundle['label.year']}" /><h:outputText value=": " /><h:outputText value="#{year.year}" />
							</h:panelGroup>										
							<h:dataTable value="#{year.branches}" var="branch"  styleClass="lightBluecell" >
								<h:column>									
									<h:outputText value="#{(!empty branch.name) ? branch.name : bundle['label.commonBranch']} " styleClass="boldFontClass"/>
									<h:dataTable value="#{branch.semesters}" var="semester" styleClass="solidBorderClass" >
										<h:column>
											<h:outputText value="#{bundle['label.semester']}" styleClass="boldFontClass"/><h:outputText value=": " /><h:outputText value="#{semester.semester}" />
											<h:dataTable value="#{semester.scopes}" var="scope" rowClasses="white, bluecell" columnClasses=",,,,,,centerClass" styleClass="greyBorderClass" headerClass="grey">
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.curricularCourse']}" /></f:facet>
													<h:outputText value="#{scope.infoCurricularCourse.name}" />
												</h:column>
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.anotations']}" /></f:facet>
													<h:outputText value="#{scope.anotation}" />
												</h:column>												
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.theoretical.abbr']}" /></f:facet>
													<h:outputText value="#{scope.infoCurricularCourse.theoreticalHours}" />
												</h:column>
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.pratical.abbr']}" /></f:facet>
													<h:outputText value="#{scope.infoCurricularCourse.praticalHours}" />
												</h:column>
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.laboratorial.abbr']}" /></f:facet>
													<h:outputText value="#{scope.infoCurricularCourse.labHours}" />
												</h:column>																					
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.theoPrat.abbr']}" /></f:facet>
													<h:outputText value="#{scope.infoCurricularCourse.theoPratHours}" />
												</h:column>	
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.credits']}" /></f:facet>
													<h:outputText value="#{scope.infoCurricularCourse.credits}" />
												</h:column>	
												<h:column>
													<f:facet name="header"><h:outputText value="#{bundle['label.weight']}" /></f:facet>
													<h:outputText value="#{scope.infoCurricularCourse.weigth}" />
												</h:column>																																																																																					
											</h:dataTable>
										</h:column>
									</h:dataTable>
								</h:column>								
							</h:dataTable>
						</h:column>
					</h:dataTable>
					<h:outputText value="<pre>" escape="false"/><h:outputText value="#{ degreeCurricularPlan.anotation }" /><h:outputText value="</pre><br/><br/>" escape="false"/>
				</h:column>
			</h:dataTable>
		</h:column>			
	</h:dataTable>	
	
</ft:tilesView>

