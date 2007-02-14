<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.department" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>

	<h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
		<h:outputLink value="#{globalBundle['institution.url']}">
			<h:outputText value="#{globalBundle['institution.name.abbreviation']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink target="_blank" value="#{globalBundle['institution.url']}#{globalBundle['link.institution.structure']}">
			<h:outputText value="#{publicDepartmentBundle['structure']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink value="showDepartments.faces">
			<h:outputText value="#{publicDepartmentBundle['academic.units']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink value="departmentSite.do?method=presentation&selectedDepartmentUnitID=#{CompetenceCourseManagement.selectedDepartmentUnit.idInternal}">
			<h:outputText value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputText value="#{publicDepartmentBundle['department.faculty']}"/>
	<h:outputText value="</div>" escape="false"/>
	
	
	<h:outputText value="<h1>#{publicDepartmentBundle['department.faculty']} #{publicDepartmentBundle['of.masculine']} #{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}</h1>" escape="false"/>
	
	<h:form>

		<fc:dataRepeater value="#{DepartmentManagement.sortedDepartmentCategories}" var="category" >
			<h:outputText value="<h2 class='greytxt mtop2'>#{category.name.content}</h2>" escape="false"/>
		
            <h:outputText value="<ul>" escape="false"/>
        		<fc:dataRepeater value="#{DepartmentManagement.teachersByCategory[category]}" var="teacher" >
                    <h:outputText value="<li>" escape="false"/>
        				<h:outputLink rendered="#{!empty teacher.person.homepage && teacher.person.homepage.activated}" value="../../homepage/#{teacher.person.user.userUId}">
					<h:outputText value="#{teacher.person.nickname}"/>
        				</h:outputLink>
        				<h:outputText rendered="#{empty teacher.person.homepage || !teacher.person.homepage.activated}" value="#{teacher.person.nickname}"/>
                    <h:outputText value="</li>" escape="false"/>
        		</fc:dataRepeater>
            <h:outputText value="</ul>" escape="false"/>
		</fc:dataRepeater>

	</h:form>

</ft:tilesView>
