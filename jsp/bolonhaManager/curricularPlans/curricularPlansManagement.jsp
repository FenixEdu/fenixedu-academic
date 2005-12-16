<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>
.italic {
font-style: italic
}
</style>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h2><h:outputText value="#{bolonhaBundle['curricularPlansManagement']}"/></h2>

	<h:outputText value="* "/>
	<h:outputLink value="createDegree.faces">
		<h:outputText value="#{bolonhaBundle['createDegree']}" />
	</h:outputLink>
	<h:outputText value="<br/><br/>" escape="false" />
<%-- 
	<fc:dataRepeater value="#{CurricularPlanManagement.departmentDegrees}" var="degree">
		<h:outputText value="<table><tr><td>" escape="false"/>
		<h:outputText value="<b>#{bolonhaBundle['degree']}:</b><br/>" escape="false" />
		<h:outputText value="#{degree.nome} > " escape="false" />
		<h:outputLink value="editDegree.faces?degreeID=#{degree.idInternal}">
			<h:outputText value="#{bolonhaBundle['editDegree']}" />
		</h:outputLink>
		<h:outputText value="<br/>" escape="false" />
		<fc:dataRepeater value="#{degree.unit.parentUnits}" var="departmentUnit">
			<h:outputText value="- #{departmentUnit.department.realName}" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</td></tr>" escape="false"/>
		
		<h:outputText value="<tr><td>#{bolonhaBundle['curricularPlans']}</td></tr>" escape="false"/>
		<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan">
			<h:outputText value="<tr><td>" escape="false"/>
			<h:outputText value="<i>#{degreeCurricularPlan.curricularStage.name}:</i></br>" escape="false" />
			<h:outputText value="#{degreeCurricularPlan.name}</br>" escape="false" />
			<h:outputText value="</td></tr>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</table>" escape="false"/>
	</fc:dataRepeater>
--%>	
<%-- 
	<h:outputText value="* "/>
	<h:outputLink value="createCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['createCurricularPlan']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="--== Temporary Links ==--"/>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="editCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['editCurricularPlan']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="--== ................. ==--"/>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="createCurricularCourse.faces">
		<h:outputText value="#{bolonhaBundle['createCurricularCourse']}" />
		<%-- Temporary values --%>
		<f:param name="degreeCurricularPlanID" value="422" />
		<f:param name="courseGroupID" value="15799" />
	</h:outputLink>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="editCurricularCourse.faces">
		<h:outputText value="#{bolonhaBundle['editCurricularCourse']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="* "/>
	<h:outputLink value="associateCurricularCourse.faces">
		<h:outputText value="#{bolonhaBundle['associateCurricularCourse']}" />
	</h:outputLink>
	<br/>
	<h:outputText value="--== ................. ==--"/>
	<br/>
	<h:outputText value="--== Temporary Links ==--"/>
	<br/>
	<br/>
	<h:outputText value="ITERATE_DEGREES"/>
	<h:panelGrid columns="1" border="1">
		<h:panelGroup>
			<h:outputText style="font-weight: bold" value="#{bolonhaBundle['degree']}: " />
			<h:outputText value="DEGREE_NAME > "/>
			<h:outputLink value="editDegree.faces">
				<h:outputText value="#{bolonhaBundle['edit']}" />
			</h:outputLink>
		</h:panelGroup>
		<h:panelGroup>
			<h:outputText value="#{bolonhaBundle['draft']}: <br/>" styleClass="italic" escape="false"/>
				<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularPlan']}: " />
				<h:outputText value="CURRICULAR_PLAN_NAME > "/>
				<h:outputLink value="showCurricularPlan.faces">
					<h:outputText value="#{bolonhaBundle['showCurricularPlan']}" />
				</h:outputLink>
				<h:outputText value=", "/>
				<h:outputLink value="editCurricularPlan.faces">
					<h:outputText value="#{bolonhaBundle['edit']} #{bolonhaBundle['data']}" />
				</h:outputLink>
				<h:outputText value=", "/>
				<h:outputLink value="editCurricularPlanStructure.faces">
					<h:outputText value="#{bolonhaBundle['edit']} #{bolonhaBundle['structure']}" />
				</h:outputLink>	
				<h:outputText value="<br/>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['published']}: <br/>" styleClass="italic" escape="false"/>
			<h:outputText value="#{bolonhaBundle['approved']}: " styleClass="italic"/>
		</h:panelGroup>
	</h:panelGrid>
--%>	

</ft:tilesView>