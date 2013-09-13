<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.department" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>

	<h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
	
		<h:outputLink value="#{globalBundle['institution.url']}" >
			<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>
		</h:outputLink>
		
		&nbsp;&gt;&nbsp;
		<h:outputLink target="_blank" value="#{globalBundle['institution.url']}#{globalBundle['link.institution.structure']}">
			<h:outputText value="#{publicDepartmentBundle['structure']}"/>
		</h:outputLink>
		
		&nbsp;&gt;&nbsp;
		<h:outputLink value="#{CompetenceCourseManagement.contextPath}/publico/department/showDepartments.faces">
			<h:outputText value="#{publicDepartmentBundle['academic.units']}"/>
		</h:outputLink>
		
		&nbsp;&gt;&nbsp;		
		<fc:contentLink label="#{CompetenceCourseManagement.selectedDepartmentUnit.department.nameI18n.content}" content="#{CompetenceCourseManagement.selectedDepartmentUnit.site}" />
		
		&nbsp;&gt;&nbsp;
		<h:outputText value="#{publicDepartmentBundle['department.courses']}"/>

	<h:outputText value="</div>" escape="false"/>
	
	<h:messages rendered="#{!empty error0 || !empty success0}" infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:outputText value="<h1>#{publicDepartmentBundle['department.courses']} #{publicDepartmentBundle['from.masculine']} " escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.nameI18n.content}</h1>" escape="false"/>

	<h:form id="SomeId">

		<h:dataTable value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit"
				rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
			<h:column>
				<h:outputText value="<h2 class='mtop1 mbottom0 greytxt'><strong>#{scientificAreaUnit.nameI18n}</strong></h2>" escape="false"/>
				<h:panelGroup rendered="#{empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:outputText style="font-style:italic" value="#{scouncilBundle['noCompetenceCourseGroupUnits']}<br/>" escape="false"/>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{!empty scientificAreaUnit.competenceCourseGroupUnits}">
					<h:dataTable value="#{scientificAreaUnit.competenceCourseGroupUnits}" var="competenceCourseGroupUnit">
							<h:column>
								<h:outputText value="<h2 class='arrow_bullet'>#{competenceCourseGroupUnit.nameI18n}</h2>" escape="false"/>
								<h:panelGroup rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
								<h:outputText value="<table class='showinfo1 smallmargin mtop05' style='width: 50em;'>" escape="false"/>
								<fc:dataRepeater value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse">
										<h:panelGroup rendered="#{competenceCourse.curricularStage.name == 'APPROVED'}">
										<h:outputText value="<tr class='color2'><td>" escape="false"/>	

										<h:outputLink value="#{CompetenceCourseManagement.contextPath}/publico/department/showCompetenceCourse.faces" style="text-decoration:none">
											<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{competenceCourse.name} (#{competenceCourse.acronym})"/>
											<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{competenceCourse.nameEn} (#{competenceCourse.acronym})"/>
											<f:param name="action" value="ccm"/>
											<f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
											<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
										</h:outputLink>

										<h:outputText value="</td></tr>" escape="false"/>
										</h:panelGroup>
								</fc:dataRepeater>
								<h:outputText value="</table>" escape="false"/>
								</h:panelGroup>
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
