<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<style>
ul.links {
list-style: none;
padding-left: 2em;
}
ul.links li {
padding-top: 0;
padding-bottom: 0;
margin-top: 0;
margin-bottom: 0;
}
</style>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['label.finalEvaluation']}</h2>" escape="false" />
	
		<h:outputText value="<ul class=\"links\"><li>" escape="false"/>
		<h:commandLink action="enterEditEnrolmentPeriod">
			<h:outputFormat value="#{bundle['label.students.listMarks']}"/>
		</h:commandLink>
		<h:outputText value="</li>" escape="false"/>
		
		<h:outputText value="<li>" escape="false"/>
		<h:commandLink action="enterSubmitMarksList">
			<h:outputFormat value="#{bundle['label.submit.listMarks']}" />
		</h:commandLink>
		<h:outputText value="</li></ul>" escape="false"/>
	</h:form>

</ft:tilesView>
