<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="df.layout.two-column.contents" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<br/>" escape="false" />
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
		
	<h:outputText value="<h2>#{managerBundle['label.manager.bolonhaCurricularPlansManagement']}</h2>" escape="false"/>
	<h:outputText value="<i>#{bolonhaBundle['no.curricularPlans']}</i>" escape="false" rendered="#{empty AcademicAdministrationDegreeManagement.filteredBolonhaDegrees}"/>

	<h:panelGroup>
	
		<fc:dataRepeater value="#{AcademicAdministrationDegreeManagement.filteredBolonhaDegrees}" var="degree" rendered="#{!empty AcademicAdministrationDegreeManagement.filteredBolonhaDegrees}">
			<h:outputText value="<table style='width: 750px' class='showinfo1 bgcolor1'>" escape="false"/>
			<h:outputText value="<tr><th width='80px'><strong>#{bolonhaBundle['degree']}:</strong></th>" escape="false"/>
	
			<h:outputText value="<td> #{degree.presentationName} (#{degree.sigla})</td>" escape="false"/>
			<h:outputText value="<td style='width: 140px'>" escape="false"/>
			<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/viewDegree.faces">
				<h:outputFormat value="#{bolonhaBundle['view']}"/>
				<f:param name="degreeId" value="#{degree.externalId}"/>
			</h:outputLink>
	
			<h:outputText value="</td></tr>" escape="false"/>

			<h:outputText value="<tr><td colspan='3' align='center'><i>#{bolonhaBundle['no.curricularPlan']}.</i></td></tr>" escape="false" rendered="#{empty degree.degreeCurricularPlans}"/>
	
			<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan" rendered="#{!empty degree.degreeCurricularPlans and degree.canBeAccessedByUser}" rowIndexVar="index">
				<h:outputText value="<tr>" escape="false"/>

				<h:outputText value="<tr>" escape="false"/>
				<h:outputText rendered="#{index == 0}" value="<th><strong>#{bolonhaBundle['plans']}:</strong></th>" escape="false"/>
				<h:outputText rendered="#{index > 0}" value="<th></th>" escape="false"/>
		
				<h:outputText value="<td>" escape="false" />
				<h:outputText rendered="#{degreeCurricularPlan.curricularStage.name == 'DRAFT'}" value="<em class='highlight1' style='color: #555;'>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}</em>" escape="false" />
				<h:outputText rendered="#{degreeCurricularPlan.curricularStage.name == 'PUBLISHED'}" value="<em class='highlight3' style='color: #555;'>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}</em>" escape="false" />
				<h:outputText rendered="#{degreeCurricularPlan.curricularStage.name == 'APPROVED'}" value="<em class='highlight4' style='color: #555;'>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}</em>" escape="false" />
				<h:outputText value=" #{degreeCurricularPlan.name}</td>" escape="false" />

				<h:outputText value="<td>" escape="false"/>
				<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/viewCurricularPlan.faces">
					<h:outputText value="#{bolonhaBundle['view']}" />
					<f:param name="degreeCurricularPlanID" value="#{degreeCurricularPlan.externalId}"/>
					<f:param name="organizeBy" value="groups"/>
					<f:param name="showRules" value="false"/>
					<f:param name="hideCourses" value="false"/>
					<f:param name="action" value="view"/>
				</h:outputLink>
				<h:outputText value=" , " escape="false" rendered="#{degreeCurricularPlan.userCanBuild}"/>
				<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/editCurricularPlan.faces" rendered="#{degreeCurricularPlan.userCanBuild}">
					<h:outputText value="#{bolonhaBundle['edit']}" />
					<f:param name="degreeCurricularPlanID" value="#{degreeCurricularPlan.externalId}"/>
					<f:param name="organizeBy" value="groups"/>
					<f:param name="showRules" value="false"/>
					<f:param name="hideCourses" value="false"/>
					<f:param name="action" value="view"/>
				</h:outputLink>
				<h:outputText value=" , " escape="false" rendered="#{degreeCurricularPlan.userCanBuild}"/>
				<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/buildCurricularPlan.faces" rendered="#{degreeCurricularPlan.userCanBuild}">
					<h:outputText value="#{bolonhaBundle['manageCurricularPlan']}" />
					<f:param name="degreeCurricularPlanID" value="#{degreeCurricularPlan.externalId}"/>
					<f:param name="organizeBy" value="groups"/>
					<f:param name="showRules" value="false"/>
					<f:param name="hideCourses" value="false"/>					
					<f:param name="action" value="build"/>
				</h:outputLink>
				<h:outputText value=" , " escape="false" />
				<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/searchCurricularCourses.do">
					<h:outputText value="#{bolonhaBundle['search.curricular.courses']}" />
					<f:param name="dcpId" value="#{degreeCurricularPlan.externalId}"/>
					<f:param name="method" value="prepareSearch"/>
				</h:outputLink>								
				<h:outputText value="</td></tr>" escape="false"/>
			</fc:dataRepeater>
	
			<h:outputText value="</table><br/>" escape="false"/>
		</fc:dataRepeater>
	</h:panelGroup>
	
	<h:outputText value="<h2>#{managerBundle['label.manager.readDegrees']}</h2>" escape="false"/>
	<h:outputText value="<i>#{bolonhaBundle['no.curricularPlans']}</i>" escape="false" rendered="#{empty AcademicAdministrationDegreeManagement.filteredPreBolonhaDegrees}"/>
	<h:panelGroup>
	
		<fc:dataRepeater value="#{AcademicAdministrationDegreeManagement.filteredPreBolonhaDegrees}" var="degree" rendered="#{!empty AcademicAdministrationDegreeManagement.filteredPreBolonhaDegrees}">
			<h:outputText value="<table style='width: 750px' class='showinfo1 bgcolor1'>" escape="false"/>
			<h:outputText value="<tr><th width='80px'><strong>#{bolonhaBundle['degree']}:</strong></th>" escape="false"/>
	
			<h:outputText value="<td> #{degree.presentationName} (#{degree.sigla})</td>" escape="false"/>
			<h:outputText value="<td style='width: 140px'>" escape="false"/>
			<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/viewDegree.faces">
				<h:outputFormat value="#{bolonhaBundle['view']}"/>
				<f:param name="degreeId" value="#{degree.externalId}"/>
			</h:outputLink>
			<h:outputText value="</td></tr>" escape="false"/>

			<h:outputText value="<tr><td colspan='3' align='center'><i>#{bolonhaBundle['no.curricularPlan']}.</i></td></tr>" escape="false" rendered="#{empty degree.degreeCurricularPlans}"/>
	
			<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan" rendered="#{!empty degree.degreeCurricularPlans}" rowIndexVar="index">
				<h:outputText value="<tr>" escape="false"/>

				<h:outputText value="<tr>" escape="false"/>
				<h:outputText rendered="#{index == 0}" value="<th><strong>#{bolonhaBundle['plans']}:</strong></th>" escape="false"/>
				<h:outputText rendered="#{index > 0}" value="<th></th>" escape="false"/>
		
				<h:outputText value="<td>#{degreeCurricularPlan.name}</td>" escape="false" />

				<h:outputText value="<td>" escape="false"/>
				<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/viewCurricularPlan.faces">
					<h:outputText value="#{bolonhaBundle['view']}" />
					<f:param name="degreeCurricularPlanID" value="#{degreeCurricularPlan.externalId}"/>
					<f:param name="organizeBy" value="groups"/>
					<f:param name="showRules" value="false"/>
					<f:param name="hideCourses" value="false"/>
					<f:param name="action" value="view"/>
				</h:outputLink>
				<h:outputText value=" , " escape="false" rendered="#{degreeCurricularPlan.userCanBuild}"/>
				<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/buildCurricularPlan.faces" rendered="#{degreeCurricularPlan.userCanBuild}">
					<h:outputText value="#{bolonhaBundle['manageCurricularPlan']}" />
					<f:param name="degreeCurricularPlanID" value="#{degreeCurricularPlan.externalId}"/>
					<f:param name="organizeBy" value="groups"/>
					<f:param name="showRules" value="false"/>
					<f:param name="hideCourses" value="false"/>					
					<f:param name="action" value="build"/>
				</h:outputLink>
				<h:outputText value=" , " escape="false" />
				<h:outputLink value="#{AcademicAdministrationDegreeManagement.request.contextPath}/academicAdministration/searchCurricularCourses.do">
					<h:outputText value="#{bolonhaBundle['search.curricular.courses']}" />
					<f:param name="dcpId" value="#{degreeCurricularPlan.externalId}"/>
					<f:param name="method" value="prepareSearch"/>
				</h:outputLink>				
				
				<h:outputText value="</td></tr>" escape="false"/>
			</fc:dataRepeater>
	
			<h:outputText value="</table><br/>" escape="false"/>
		</fc:dataRepeater>
	</h:panelGroup>

</ft:tilesView>
