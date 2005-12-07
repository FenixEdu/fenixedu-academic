<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundleDEP"/>

	<h:outputText value="<h2>#{bundleDEP['curricularPlansManagementGroup']}</h2><br/>" escape="false"/>

	<h:form>

		<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"
				value="#{bundleDEP[managerFunctionsManagementBackingBean.errorMessage]}"/>

		<h:outputText value="<b>#{bundleDEP['add.new.members']}</b><br/>" escape="false"/>
		<h:outputText value="<i>#{ManagementGroupsBackingBean.department.realName}</i><br/>" escape="false"/>
<%-- 
 		<h:outputText value="#{bundleDEP['number.of.persons']}: #{ManagementGroupsBackingBean.completeList.size}<br/>" escape="false"/>
--%>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundleDEP['label.departmentTeachersList.teacherName']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
<%-- 
				<h:inputText value="#{ManagementGroupsBackingBean.name}"/>
--%>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundleDEP['label.departmentTeachersList.teacherNumber']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
<%-- 
				<h:inputText value="#{ManagementGroupsBackingBean.number}"/>
--%>
			</h:panelGroup>
		</h:panelGrid>

		<fc:commandLink value="#{ManagementGroupsBackingBean.indexingSize} #{bundleDEP['results.per.page']}" action="#{ManagementGroupsBackingBean.listIndexing = false}" rendered="#{ManagementGroupsBackingBean.listIndexing == true}"/>
		<fc:commandLink value="#{bundleDEP['results.per.page']}" action="#{ManagementGroupsBackingBean.listIndexing = true}" rendered="#{ManagementGroupsBackingBean.listIndexing == false}"/>

		<fc:dataRepeater value="#{ManagementGroupsBackingBean.pageIndexes}" var="pageIndex">
			<h:outputText value="#{pageIndex} , " rendered="#{pageIndex == ManagementGroupsBackingBean.currentPageIndex}"/>
			<fc:commandLink value="#{pageIndex}" rendered="#{pageIndex != ManagementGroupsBackingBean.currentPageIndex}" action="#{ManagementGroupsBackingBean.onPageChange}">
				<f:param name="pageNumber" value="#{pageIndex}" />
			</fc:commandLink>
		</fc:dataRepeater>

	</h:form>		
	
</ft:tilesView>
