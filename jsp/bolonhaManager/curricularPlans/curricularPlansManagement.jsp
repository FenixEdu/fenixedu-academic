<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{bolonhaBundle['bolonhaManager']}</i>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['curricularPlans']}</h2>" escape="false"/>
	<h:outputText value="<br/>" escape="false" />

	<h:messages errorClass="error" infoClass="infoMsg"/>
	
	<h:outputText value="<table border='0' width='70%'>" escape="false"/>
		<fc:dataRepeater value="#{ScientificCouncilDegreeManagement.bolonhaDegrees}" var="degree">
		<h:outputText value="<tr><td colspan='2'><b>#{bolonhaBundle['degree']}:</b></td></tr>" escape="false"/>

		<h:outputText value="<tr><td colspan='2'>#{degree.nome} > " escape="false"/>
		<h:outputLink value="viewDegree.faces">
			<h:outputFormat value="#{bolonhaBundle['view']}"/>
			<f:param name="degreeId" value="#{degree.idInternal}"/>
		</h:outputLink>
		<h:outputText value="</td></tr>" escape="false"/>
		
		<h:outputText value="<tr><td colspan='2'><b>#{bolonhaBundle['curricularPlan']}:</b></td></tr>" escape="false" rendered="#{!empty degree.degreeCurricularPlans}"/>
		<h:outputText value="<tr><td colspan='2' align='center'><i>#{bolonhaBundle['no.curricularPlan']}.</i></td></tr>" escape="false" rendered="#{empty degree.degreeCurricularPlans}"/>

		<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan" rendered="#{!empty degree.degreeCurricularPlans}">
			<h:outputText value="<tr>" escape="false"/>

			<h:outputText value="<td><i>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}:</i> " escape="false" />
			<h:outputText value="#{degreeCurricularPlan.name}</td>" escape="false" />

			<h:outputText value="<td align='right'>" escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces">
				<h:outputText value="#{bolonhaBundle['view']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="buildCurricularPlan.faces">
				<h:outputText value="#{bolonhaBundle['buildCurricularPlan']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value="</td>" escape="false"/>
			
			<h:outputText value="</tr>" escape="false"/>
		</fc:dataRepeater>

		<h:outputText value="<tr><td colspan='2'>&nbsp;</td></tr>" escape="false"/>
		
	</fc:dataRepeater>
	<h:outputText value="</table>" escape="false"/>	

</ft:tilesView>
