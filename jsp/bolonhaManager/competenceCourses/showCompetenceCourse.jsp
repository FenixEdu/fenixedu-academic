<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{bolonhaBundle['competenceCourse']}" style="font-style:italic"/>
	<h2><h:outputText value="COMPETENCE_COURSE_NAME"/></h2>
	
	<h:outputText value="#{bolonhaBundle['activeCurricularPlans']}: " style="font-weight: bold"/><br/>
	<h:outputText value="ACTIVE_CURRICULAR_PLANS"/><br/><br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}"/><br/>
	<h:outputText value="#{bolonhaBundle['scientificArea']}:" style="font-weight: bold"/><br/>
	<h:outputText value="#{bolonhaBundle['group']}: " style="font-weight: bold"/><br/>
	<br/>		

	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['name']}: "/>
	<h:outputText value="NAME"/><br/>
	
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['code']}: " />
	<h:outputText value="CODE"/><br/>
	
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['ectsCredits']}: " />
	<h:outputText value="ECTS_CREDITS"/><br/>
	
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['basic']}: " />
	<h:outputText value="TRUE"/><br/>
	
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['regime']}: " />
	<h:outputText value="SEMESTER"/><br/>
	
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['lessonHours']}:" /><br/>
	<ul>
		<li>
			<h:outputText value="#{bolonhaBundle['theoreticalLesson']}:"/>
			<h:outputText value="THEORETICAL_HOURS"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['problemsLesson']}:"/>
			<h:outputText value="PROBLEMS_HOURS"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['laboratorialLesson']}:"/>
			<h:outputText value="LABORATORIAL_HOURS"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['projectLesson']}:"/>
			<h:outputText value="PROJECT_HOURS"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['seminaryLesson']}:"/>
			<h:outputText value="SEMINARY_HOURS"/>
		</li>
	</ul>					
	<br/>
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['program']}:" /><br/>
	<h:outputText value="PROGRAM"/><br/><br/>
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['bibliography']}:" /><br/>
	<h:outputText value="BIBLIOGRAPHY"/><br/>
	<br/><hr>
	<h:form>
		<h:commandLink action="competenceCoursesManagement" value="#{bolonhaBundle['back']}">
				<%-- <f:param id="competenceCourseID" value="ID" /> --%>
		</h:commandLink>
	</h:form>
</ft:tilesView>