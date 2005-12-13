<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CurricularCourseManagement.personDepartmentName}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['editCurricularCourse']}"/></h2>		
	<h:outputText value="ROOT_PATH_HERE" /><br/>
	<h:form>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['competenceCourse']}: "/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['department']}: " />
			<h:selectOneMenu value="">
				<%-- <f:selectItems value="#{CurricularCourseManagement.regimeTypes}" /> --%>
			</h:selectOneMenu>
			
			<h:outputText value="#{bolonhaBundle['competenceCourse']}: " />
			<h:selectOneMenu value="">
				<%-- <f:selectItems value="#{CurricularCourseManagement.regimeTypes}" /> --%>
			</h:selectOneMenu>
		</h:panelGrid>
		<h:commandLink value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"
			action="showCompetenceCourse">			
			<%-- <f:param id="competenceCourseID" value="" /> --%>
		</h:commandLink>
		<h:outputText value=" (#{bolonhaBundle['newPage']})" />
		<br/><br/>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularCourseInformation']}: "/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['weight']}: " />
			<h:inputText required="true" maxlength="5" size="5"/>
		</h:panelGrid>
		<br/>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['context']}: "/><br/>
		<h:outputText value="ROOT_PATH_HERE" /><br/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
			<h:selectOneMenu value="">
				<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
			</h:selectOneMenu>
			
			<h:outputText value="#{bolonhaBundle['semester']}: " />
			<h:selectOneMenu value="">
				<f:selectItems value="#{CurricularCourseManagement.semesters}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<br/>
		<h:outputText value="OTHER_ROOT_PATHs_HERE" /><br/>
		<br/><br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
			action="#{CurricularCourseManagement.editCurricularCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>