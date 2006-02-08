<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CourseGroupManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CourseGroupManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['associate.course.group']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>		
		<h:outputText escape="false" value="<input id='parentCourseGroupID' name='parentCourseGroupID' type='hidden' value='#{CourseGroupManagement.parentCourseGroupID}'/>"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['chooseCourseGroup']}:</h4><br/>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	
		
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<h:outputText value="#{CourseGroupManagement.parentName}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroupToAssociate']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CourseGroupManagement.courseGroupID}">
			<f:selectItems value="#{CourseGroupManagement.courseGroups}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
				
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['associate']}"
			action="#{CourseGroupManagement.addContext}"/>	
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="editCurricularPlanStructure"/>
	</h:form>

</ft:tilesView>