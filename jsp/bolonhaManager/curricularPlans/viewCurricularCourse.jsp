<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['viewCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['competenceCourse']}:</h4><br/>" escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	
	<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.name}</p>" escape="false"/>	
	<h:outputText value="<p><label>#{bolonhaBundle['competenceCourse']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.name}</p>" escape="false"/>	
	<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
	<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
		<h:outputText value="(#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']})"/>
		<f:param name="competenceCourseID" value="#{CurricularCourseManagement.curricularCourse.competenceCourse.idInternal}"/>
	</h:outputLink>
	<h:outputText value="</p></fieldset></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularCourseInformation']}:</h4><br/>" escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
	<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.weigth}</p>" escape="false"/>
	<h:outputText value="<p><label>#{bolonhaBundle['prerequisites']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.prerequisites}</p>" escape="false"/>	
	<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.prerequisitesEn}</p>" escape="false"/>
	<h:outputText value="</fieldset></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['contexts']}:</h4><br/>" escape="false"/>	
	<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<h:outputText value="#{context.courseGroup.name}</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularPeriod']}:</label>" escape="false"/>
		<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
		<h:outputText value="</fieldset><br/>" escape="false"/>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.idInternal}'/>"/>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.idInternal}'/>"/>

		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'view'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="viewCurricularPlan"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'build'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="buildCurricularPlan"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'close'}">
			<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{bolonhaBundle['close']}" />
		</h:panelGroup>
	</h:form>
</ft:tilesView>