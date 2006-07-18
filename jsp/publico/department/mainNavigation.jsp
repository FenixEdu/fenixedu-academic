<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>

 	<h:outputText value="<ul class='treemenu'><li>" escape="false"/>
		<h:outputText rendered="#{empty CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.acronym}" escape="false"/>
		<h:outputLink 
				rendered="#{!empty CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" 
				value="#{CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" 
				target="_blank"
				title="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}">
			<h:outputText value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.acronym}" escape="false"/>
		</h:outputLink>
 	<h:outputText value="</li><li>" escape="false"/>
		<h:outputLink value="../department/showDepartmentTeachers.faces">
			<h:outputText value="#{publicDepartmentBundle['department.faculty']}"/>
			<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
		</h:outputLink>
 	<h:outputText value="</li><li>" escape="false"/>
		<h:outputLink value="../department/showDepartmentCompetenceCourses.faces">
			<h:outputText value="#{publicDepartmentBundle['department.courses']}"/>
			<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
		</h:outputLink>
 	<h:outputText value="</li></ul>" escape="false"/>
 	
<h:form>
	<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
</h:form>
