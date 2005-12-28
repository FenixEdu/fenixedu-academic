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
	
	<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['editCurricularCourse']}"/></h2>
	<h:outputText styleClass="error" rendered="#{!empty CurricularCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CurricularCourseManagement.errorMessage]}<br/>" escape="false"/>			
	<h:messages styleClass="infomsg"/>
	<br/>
	<h:form>
		<fc:viewState binding="#{CurricularCourseManagement.viewState}" />
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'"/>
		<h:outputText escape="false" value="<input id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourseID}'"/>
		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['competenceCourse']}: "/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['department']}: " />
			<h:selectOneMenu value="#{CurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
					valueChangeListener="#{CurricularCourseManagement.resetCompetenceCourse}">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</h:selectOneMenu>
			
			<h:outputText value="#{bolonhaBundle['competenceCourse']}: " />
			<h:selectOneMenu value="#{CurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
				<f:selectItems value="#{CurricularCourseManagement.competenceCourses}"/>
			</h:selectOneMenu>
		</h:panelGrid>

		<h:panelGroup rendered="#{(!empty CurricularCourseManagement.competenceCourseID) && (CurricularCourseManagement.competenceCourseID != 0) }">
			<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
				<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
				<f:param name="competenceCourseID" value="#{CurricularCourseManagement.competenceCourseID}"/>
			</h:outputLink>
			<h:outputText value=" (#{bolonhaBundle['newPage']})<br/>" escape="false"/>
		</h:panelGroup>		
		<br/>
		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularCourseInformation']}: <br/>"  escape="false"/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['weight']}: "/>
			<h:inputText id="weight" maxlength="5" size="5" value="#{CurricularCourseManagement.weight}" />
			
			<h:outputText value="#{bolonhaBundle['prerequisites']}: "/>
			<h:inputTextarea id="prerequisites" cols="80" rows="5" value="#{CurricularCourseManagement.prerequisites}"/>
		</h:panelGrid>
		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['english']}: <br/>"  escape="false"/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['prerequisitesEn']}: "/>
			<h:inputTextarea id="prerequisitesEn" cols="80" rows="5" value="#{CurricularCourseManagement.prerequisitesEn}"/>
			
			<h:outputText value=" " escape="false"/>
			<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['update']}"
				action="#{CurricularCourseManagement.editCurricularCourse}"/>
		</h:panelGrid>		
		<br/>		
		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['contexts']}: <br/>" escape="false"/>
		<h:outputLink value="editCurricularCourse.faces">
			<h:outputText value="#{bolonhaBundle['newContext']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}" />
			<f:param name="curricularCourseID" value="#{CurricularCourseManagement.curricularCourseID}" />
		</h:outputLink>
		<br/><br/>
		<h:dataTable value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
			<h:column>
				<h:panelGrid columnClasses="alignRight,," columns="2" border="0"
					rendered="#{context.idInternal != CurricularCourseManagement.contextID}">
					
					<h:outputText value="#{bolonhaBundle['courseGroup']}: "/>
					<h:outputText value="#{context.courseGroup.name}"/>
					
					<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
					<h:outputText value="#{context.curricularSemester.curricularYear.year}"/>
					
					<h:outputText value="#{bolonhaBundle['semester']}: " />
					<h:outputText value="#{context.curricularSemester.semester}"/>
					
					<h:outputText value=" " escape="false"/>
					<h:panelGroup>
						<h:outputLink value="editCurricularCourse.faces">
							<h:outputText value="#{bolonhaBundle['edit']}" />
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}" />
							<f:param name="courseGroupID" value="#{context.courseGroup.idInternal}" />
							<f:param name="contextID" value="#{context.idInternal}" />
							<f:param name="curricularCourseID" value="#{CurricularCourseManagement.curricularCourseID}" />
						</h:outputLink>
						<h:outputText value=", " escape="false"/>	
						<h:commandLink value="#{bolonhaBundle['delete']}" action="#{CurricularCourseManagement.editCurricularCourseReturnPath}"
							actionListener="#{CurricularCourseManagement.deleteContext}">
							<f:param name="contextIDToDelete" value="#{context.idInternal}"/>
						</h:commandLink>
					</h:panelGroup>
				</h:panelGrid>
				
				<h:panelGrid columnClasses="alignRight,," columns="2" border="0"
					rendered="#{context.idInternal == CurricularCourseManagement.contextID}">
				
					<h:outputText value="#{bolonhaBundle['courseGroup']}: "/>
					<h:selectOneMenu value="#{CurricularCourseManagement.courseGroupID}">
						<f:selectItems value="#{CurricularCourseManagement.courseGroups}" />
					</h:selectOneMenu>
				
					<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
					<h:selectOneMenu value="#{CurricularCourseManagement.curricularYearID}">
						<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
					</h:selectOneMenu>
				
					<h:outputText value="#{bolonhaBundle['semester']}: " />
					<h:selectOneMenu value="#{CurricularCourseManagement.curricularSemesterID}">
						<f:selectItems value="#{CurricularCourseManagement.curricularSemesters}" />
					</h:selectOneMenu>
					
					<h:outputText value=" " escape="false"/>
					<h:panelGroup>
						<h:outputText escape="false" value="<input id='contextID' name='contextID' type='hidden' value='#{context.idInternal}'"/>
						<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['update']}"
							action="#{CurricularCourseManagement.editContext}"/>
					</h:panelGroup>				
				</h:panelGrid>
				<h:outputText value="<br/>" escape="false"/>
			</h:column>
		</h:dataTable>
		<br/>
		<h:panelGrid columnClasses="alignRight,," columns="2" border="0"
				rendered="#{empty CurricularCourseManagement.contextID}">
			
				<h:outputText value="#{bolonhaBundle['courseGroup']}: "/>
				<h:selectOneMenu value="#{CurricularCourseManagement.courseGroupID}">
					<f:selectItems value="#{CurricularCourseManagement.courseGroups}" />
				</h:selectOneMenu>
			
				<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
				<h:selectOneMenu value="#{CurricularCourseManagement.curricularYearID}">
					<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
				</h:selectOneMenu>
			
				<h:outputText value="#{bolonhaBundle['semester']}: " />
				<h:selectOneMenu value="#{CurricularCourseManagement.curricularSemesterID}">
					<f:selectItems value="#{CurricularCourseManagement.curricularSemesters}" />
				</h:selectOneMenu>
				
				<h:outputText value=" " escape="false"/>
				<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['associate']}"
					action="" actionListener="#{CurricularCourseManagement.addContext}"/>
		</h:panelGrid>
		<br/><hr>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>