<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.department" attributeName="body-inline">
	<style>@import "<%= request.getContextPath() %>/CSS/transitional.css";</style>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>

	<h:outputLink value="#{globalBundle['institution.url']}" >
		<h:outputText value="#{globalBundle['institution.name.abbreviation']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputText rendered="#{empty CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}"/>
	<h:outputLink rendered="#{!empty CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" value="#{CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" target="_blank">
		<h:outputText value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputText value="#{publicDepartmentBundle['department.competence.courses']}"/>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:outputText value="<br/><h1>#{publicDepartmentBundle['department.competence.courses']} #{publicDepartmentBundle['from.masculine']} #{CompetenceCourseManagement.selectedDepartmentUnit.department.acronym}</h1>" escape="false"/>

	<h:form>

		<h:dataTable value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit"
				rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
			<h:column>
				<h:outputText value="<h2 class='mtop2 mbottom0 greytxt'><strong>#{scientificAreaUnit.name}</strong></h2>" escape="false"/>
				<h:panelGroup rendered="#{empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:outputText style="font-style:italic" value="#{scouncilBundle['noCompetenceCourseGroupUnits']}<br/>" escape="false"/>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{!empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:dataTable style="padding-left: 40px;" value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
							<h:column>
								<h:outputText value="<h2 class='arrow_bullet'>#{competenceCourseGroupUnit.name}</h2>" escape="false"/>
								<h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
										styleClass="showinfo1 smallmargin mtop05" style="width: 50em;" rowClasses="color2" columnClasses=",aright" rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
	
										<h:column rendered="#{competenceCourse.curricularStage.name == 'APPROVED'}">
											<h:outputLink value="showCompetenceCourse.faces" style="text-decoration:none">
												<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{competenceCourse.name} (#{competenceCourse.acronym})"/>
												<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{competenceCourse.nameEn} (#{competenceCourse.acronym})"/>
												<f:param name="action" value="ccm"/>
												<f:param name="competenceCourseID" value="#{competenceCourse.idInternal}"/>
												<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
											</h:outputLink>
										</h:column>
								</h:dataTable>
							</h:column>
					</h:dataTable>
				</h:panelGroup>
			</h:column>
		</h:dataTable>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.scientificAreaUnits && !empty CompetenceCourseManagement.selectedDepartmentUnitID}">
			<h:outputText  value="<i>#{scouncilBundle['noScientificAreaUnits']}<i><br/>" escape="false"/>
		</h:panelGroup>
		
	</h:form>

</ft:tilesView>
