<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['viewCurricularCourse']}"/></h2>
	<h:outputText styleClass="error" rendered="#{!empty CurricularCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CurricularCourseManagement.errorMessage]}<br/>" escape="false"/>			
	<h:messages styleClass="infomsg"/>
	<br/>
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['competenceCourse']}: <br/>" escape="false"/>
	<h:outputText style="font-style: italic" value="#{bolonhaBundle['department']}: "/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.name}<br/>" escape="false"/>
	<h:outputText style="font-style: italic" value="#{bolonhaBundle['competenceCourse']}: "/>
	<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.name}<br/>" escape="false"/>
	<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
		<h:outputText value="(#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']})"/>
		<f:param name="competenceCourseID" value="#{CurricularCourseManagement.curricularCourse.competenceCourse.idInternal}"/>
	</h:outputLink>
	<br/><br/>
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularCourseInformation']}: <br/>"  escape="false"/>
	<h:outputText style="font-style: italic" value="#{bolonhaBundle['weight']}: "/>
	<h:outputText style="font-style: italic" value="#{CurricularCourseManagement.curricularCourse.weigth}<br/>" escape="false"/>
	<h:outputText style="font-style: italic" value="#{bolonhaBundle['prerequisites']}: "/>
	<h:outputText style="font-style: italic" value="#{CurricularCourseManagement.curricularCourse.prerequisites}<br/>" escape="false"/>
	<h:outputText style="font-style: italic" value="#{bolonhaBundle['prerequisitesEn']}: "/>
	<h:outputText style="font-style: italic" value="#{CurricularCourseManagement.curricularCourse.prerequisitesEn}<br/>" escape="false"/>
	<br/>
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['contexts']}: <br/>" escape="false"/>
	<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
		<h:outputText value="#{bolonhaBundle['courseGroup']}: "/>
		<h:outputText value="#{context.courseGroup.name}<br/>" escape="false"/>
		
		<h:outputText value="#{bolonhaBundle['curricularPeriod']}: " />
		<h:outputText value="#{context.curricularPeriod.fullLabel}<br/><br/>" escape="false"/>
	</fc:dataRepeater>
	<hr>
	<h:form>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.idInternal}'/>"/>
		
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'return'}">
			<h:commandLink immediate="true" value="#{bolonhaBundle['back']}" action="buildCurricularPlan"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'close'}">
			<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{bolonhaBundle['close']}" />
		</h:panelGroup>
	</h:form>
</ft:tilesView>