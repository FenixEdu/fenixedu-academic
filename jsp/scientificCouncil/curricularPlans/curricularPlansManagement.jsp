<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h2><h:outputText value="#{scouncilBundle['curricularPlansManagement']}"/></h2>

	<h:outputText value="* "/>
	<h:outputLink value="createDegree.faces">
		<h:outputText value="#{scouncilBundle['createDegree']}" />
	</h:outputLink>
	<h:outputText value="<br/><br/>" escape="false" />

	<h:messages errorClass="error" infoClass="infoMsg"/>
	
	<fc:dataRepeater value="#{ScientificCouncilCurricularPlanManagement.bolonhaDegrees}" var="degree">
		<h:outputText value="<table><tr><td>" escape="false"/>
		<h:outputText value="<b>#{scouncilBundle['degree']}:</b><br/>" escape="false" />
		<h:outputText value="#{degree.nome} > " escape="false" />
		<h:outputLink value="editDegree.faces?degreeID=#{degree.idInternal}">
			<h:outputText value="#{scouncilBundle['editDegree']}" />
		</h:outputLink>
		<h:outputText value="<br/>" escape="false" />
		
		<h:outputText value="<tr><td>#{scouncilBundle['curricularPlans']}</td></tr>" escape="false"/>
		<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan">
			<h:outputText value="<tr><td>" escape="false"/>
			<h:outputText value="<i>#{degreeCurricularPlan.curricularStage.name}:</i></br>" escape="false" />
			<h:outputText value="#{degreeCurricularPlan.name}</br>" escape="false" />
			<h:outputText value="</td></tr>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</table>" escape="false"/>
	</fc:dataRepeater>
	
<%-- 
	<h:outputText value="* "/>
	<h:outputLink value="createCurricularPlan.faces">
		<h:outputText value="#{scouncilBundle['createCurricularPlan']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="--== Temporary Links ==--"/>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="editCurricularPlan.faces">
		<h:outputText value="#{scouncilBundle['editCurricularPlan']}" />
	</h:outputLink>
	<br/>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="createCurricularCourse.faces">
		<h:outputText value="#{scouncilBundle['createCurricularCourse']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="editCurricularCourse.faces">
		<h:outputText value="#{scouncilBundle['editCurricularCourse']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="associateCurricularCourse.faces">
		<h:outputText value="#{scouncilBundle['associateCurricularCourse']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="--== Temporary Links ==--"/>
	<br/>
	<br/>
	<h:outputText value="ITERATE_DEGREES"/>
	<h:panelGrid columns="1" border="1">
		<h:panelGroup>
			<h:outputText style="font-weight: bold" value="#{scouncilBundle['degree']}: " />
			<h:outputText value="DEGREE_NAME > "/>
			<h:outputLink value="editDegree.faces">
				<h:outputText value="#{scouncilBundle['edit']}" />
			</h:outputLink>
		</h:panelGroup>
		<h:panelGroup>
			<h:outputText value="#{scouncilBundle['draft']}: <br/>" styleClass="italic" escape="false"/>
				<h:outputText style="font-weight: bold" value="#{scouncilBundle['curricularPlan']}: " />
				<h:outputText value="CURRICULAR_PLAN_NAME > "/>
				<h:outputLink value="showCurricularPlan.faces">
					<h:outputText value="#{scouncilBundle['showCurricularPlan']}" />
				</h:outputLink>
				<h:outputText value=", "/>
				<h:outputLink value="editCurricularPlan.faces">
					<h:outputText value="#{scouncilBundle['edit']} #{scouncilBundle['data']}" />
				</h:outputLink>
				<h:outputText value=", "/>
				<h:outputLink value="editCurricularPlanStructure.faces">
					<h:outputText value="#{scouncilBundle['edit']} #{scouncilBundle['structure']}" />
				</h:outputLink>	
				<h:outputText value="<br/>" escape="false"/>
			<h:outputText value="#{scouncilBundle['published']}: <br/>" styleClass="italic" escape="false"/>
			<h:outputText value="#{scouncilBundle['approved']}: " styleClass="italic"/>
		</h:panelGroup>
	</h:panelGrid>
--%>	

</ft:tilesView>