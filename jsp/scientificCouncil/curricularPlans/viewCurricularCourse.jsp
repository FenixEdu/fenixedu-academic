<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scientificBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{scientificBundle['viewCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{scientificBundle['competenceCourse']}:</h4><br/>" escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	
	<h:outputText value="<p><label>#{scientificBundle['department']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.name}</p>" escape="false"/>	
	<h:outputText value="<p><label>#{scientificBundle['competenceCourse']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.name}</p>" escape="false"/>	
	<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
	<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
		<h:outputText value="(#{scientificBundle['showPage']} #{scientificBundle['competenceCourse']})"/>
		<f:param name="competenceCourseID" value="#{CurricularCourseManagement.curricularCourse.competenceCourse.idInternal}"/>
	</h:outputLink>
	<h:outputText value="</p></fieldset></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{scientificBundle['curricularCourseInformation']}:</h4><br/>" escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
	<h:outputText value="<p><label>#{scientificBundle['weight']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.weigth}</p>" escape="false"/>
	<h:outputText value="<p><label>#{scientificBundle['prerequisites']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.prerequisites}</p>" escape="false"/>	
	<h:outputText value="<p><label>#{scientificBundle['prerequisitesEn']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.prerequisitesEn}</p>" escape="false"/>
	<h:outputText value="</fieldset></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{scientificBundle['contexts']}:</h4><br/>" escape="false"/>	
	<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{scientificBundle['courseGroup']}:</label>" escape="false"/>
		<h:outputText value="#{context.courseGroup.name}</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{scientificBundle['curricularPeriod']}:</label>" escape="false"/>
		<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
		<h:outputText value="</fieldset><br/>" escape="false"/>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.idInternal}'/>"/>

		<h:outputText escape="false" value="<br/><br/><hr/>"/>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'view'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{scientificBundle['back']}" action="viewCurricularPlan"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'build'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{scientificBundle['back']}" action="buildCurricularPlan"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'close'}">
			<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{scientificBundle['close']}" />
		</h:panelGroup>
	</h:form>
</ft:tilesView>