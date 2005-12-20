<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{scouncilBundle['scientificCouncil']}</i>" escape="false"/>
	<h:outputText value="<h2>#{scouncilBundle['curricularPlansManagement']}</h2>" escape="false"/>

	<h:outputText value="</br>* " escape="false"/>
	<h:outputLink value="createDegree.faces">
		<h:outputFormat value="#{scouncilBundle['create.param']}">
			<f:param value="#{scouncilBundle['degree']}"/>
		</h:outputFormat>
	</h:outputLink>
	<h:outputText value="<br/><br/>" escape="false" />

	<h:messages errorClass="error" infoClass="infoMsg"/>
	
	<h:outputText value="<table border='0' width='70%'>" escape="false"/>
		<fc:dataRepeater value="#{ScientificCouncilDegreeManagement.bolonhaDegrees}" var="degree">
		<h:outputText value="<tr><td colspan='2'><b>#{scouncilBundle['degree']}:</b></td></tr>" escape="false"/>

		<h:outputText value="<tr><td colspan='2'>#{degree.nome} > " escape="false"/>
		<h:outputLink value="editDegree.faces">
			<h:outputFormat value="#{scouncilBundle['edit']}"/>
			<f:param name="degreeId" value="#{degree.idInternal}"/>
		</h:outputLink>
		<h:panelGroup rendered="#{empty degree.degreeCurricularPlans}">
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="deleteDegree.faces">
				<h:outputFormat value="#{scouncilBundle['delete']}"/>
				<f:param name="degreeId" value="#{degree.idInternal}"/>
			</h:outputLink>
		</h:panelGroup>
		<h:outputText value="</td></tr>" escape="false"/>
		
		<h:outputText value="<tr><td colspan='2'><b>#{scouncilBundle['curricularPlan']}:</b></td></tr>" escape="false" rendered="#{!empty degree.degreeCurricularPlans}"/>
		<h:outputText value="<tr><td colspan='2' align='center'><i>#{scouncilBundle['no.curricularPlan']}.</i></td></tr>" escape="false" rendered="#{empty degree.degreeCurricularPlans}"/>

		<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan" rendered="#{!empty degree.degreeCurricularPlans}">
			<h:outputText value="<tr>" escape="false"/>

			<h:outputText value="<td><i>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}:</i> " escape="false" />
			<h:outputText value="#{degreeCurricularPlan.name}</td>" escape="false" />

			<h:outputText value="<td align='right'>" escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces">
				<h:outputText value="#{scouncilBundle['view']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="editCurricularPlan.faces">
				<h:outputText value="#{scouncilBundle['editData']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="buildCurricularPlan.faces">
				<h:outputText value="#{scouncilBundle['buildCurricularPlan']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="deleteCurricularPlan.faces">
				<h:outputFormat value="#{scouncilBundle['delete']}"/>
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="curricularPlanGroup.faces">
				<h:outputText value="#{scouncilBundle['group']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value="</td>" escape="false"/>
			
			<h:outputText value="</tr>" escape="false"/>
		</fc:dataRepeater>

		<h:outputText value="<tr><td colspan='2' align='right'>" escape="false"/>
		<h:outputLink value="createCurricularPlan.faces">
			<h:outputFormat value="#{scouncilBundle['create.param']}">
				<f:param value="#{scouncilBundle['curricularPlan']}"/>
			</h:outputFormat>
			<f:param name="degreeId" value="#{degree.idInternal}"/>
		</h:outputLink>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='2'>&nbsp;</td></tr>" escape="false"/>
		
	</fc:dataRepeater>
	<h:outputText value="</table>" escape="false"/>	

</ft:tilesView>
