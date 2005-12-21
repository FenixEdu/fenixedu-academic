<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
<!--
.alignRight {
	text-align: right;
}
-->
</style>
<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.degree.nome}" style="font-style: italic"/>
	<h:outputText value=" (#{CurricularCourseManagement.degreeCurricularPlan.degree.sigla})" style="font-style: italic"/><br/>
	<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['deleteCurricularCourse']}"/></h2>
	<h:outputText styleClass="error" rendered="#{!empty CurricularCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CurricularCourseManagement.errorMessage]}<br/>" escape="false"/>			
	<h:messages styleClass="infomsg"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'"/>
		<h:outputText escape="false" value="<input id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourseID}'"/>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularCourseInformation']}: <br/>"  escape="false"/>
		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['name']}: "/>
			<h:outputText value="#{CurricularCourseManagement.curricularCourse.name}"/>
		</h:panelGrid>
		<br/>		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['context']}: <br/>" escape="false"/>
		<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
			<h:panelGrid columnClasses="alignRight infocell, infocell," columns="2" border="0">				
				<h:outputText value="#{bolonhaBundle['courseGroup']}: "/>
				<h:outputText value="#{context.courseGroup.name}"/>
				
				<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
				<h:outputText value="#{context.curricularSemester.curricularYear.year}"/>
				
				<h:outputText value="#{bolonhaBundle['semester']}: " />
				<h:outputText value="#{context.curricularSemester.semester}"/>
			</h:panelGrid>
		</fc:dataRepeater>
		<br/>
		<h:outputText styleClass="infomsg" value="#{bolonhaBundle['deleteLastCurricularCourseContext']}<br/>" escape="false"/>
		<h:outputText styleClass="error" value="#{bolonhaBundle['confirmDeleteMessage']}<br/><hr>" escape="false"/>
		<h:outputText escape="false" value="<input id='contextIDToDelete' name='contextIDToDelete' type='hidden' value='#{CurricularCourseManagement.contextIDToDelete}'"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			 action="#{CurricularCourseManagement.deleteContext}" actionListener="#{CurricularCourseManagement.setForceDeleteContext}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="editCurricularCourse"/>
	</h:form>
</ft:tilesView>