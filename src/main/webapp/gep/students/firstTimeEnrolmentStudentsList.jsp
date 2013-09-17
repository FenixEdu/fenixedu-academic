<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="definition.gep.two-column" attributeName="body-inline">

	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	<f:loadBundle basename="resources/GEPResources" var="bundleGEP"/>
	
	<h:form>
		<h:outputText value="#{bundleGEP['label.gep.chooseExecutionYear']}: " />
		<h:selectOneMenu value="#{listFirstTimeEnrolmentMasterDegreeStudents.selectedExecutionYear}" onchange="this.form.submit()">
			<f:selectItems value="#{listFirstTimeEnrolmentMasterDegreeStudents.executionYears}" />	
		</h:selectOneMenu>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</h:form>

	<h:dataTable value="#{listFirstTimeEnrolmentMasterDegreeStudents.studentCurricularPlans}" var="studentCurricularPlan" columnClasses="solidBorderClass2">
		<h:column>
			<f:facet name="header">
				<h:outputText value="#{bundleGEP['label.gep.studentNumber']}" />		
			</f:facet>
			<h:outputText value="#{studentCurricularPlan.infoStudent.number}" />
		</h:column>	
		<h:column>
			<f:facet name="header">
				<h:outputText value="#{bundleGEP['label.gep.studentName']}" />		
			</f:facet>		
			<h:outputText value="#{studentCurricularPlan.infoStudent.infoPerson.nome}" />
		</h:column>			
		<h:column>
			<f:facet name="header">
				<h:outputText value="#{bundleGEP['label.gep.dcpName']}" />		
			</f:facet>		
			<h:outputText value="#{studentCurricularPlan.infoDegreeCurricularPlan.name}" />
		</h:column>	
		<h:column>
			<f:facet name="header">
				<h:outputText value="#{bundleGEP['label.gep.scpSpecialization']}" />		
			</f:facet>		
			<h:outputText value="#{bundleEnumeration[studentCurricularPlan.specialization]}" />
		</h:column>			
		<h:column>
			<f:facet name="header">
				<h:outputText value="#{bundleGEP['label.gep.scpState']}" />		
			</f:facet>		
			<h:outputText value="#{bundleEnumeration[studentCurricularPlan.currentState]}" />
		</h:column>			
		<h:column>
			<f:facet name="header">
				<h:outputText value="#{bundleGEP['label.gep.firstTimeEnrolment']}" />		
			</f:facet>		
			<h:outputText rendered="#{studentCurricularPlan.firstTimeEnrolment}" value="Sim" />
		</h:column>			
	</h:dataTable>



</ft:tilesView>