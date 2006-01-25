<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>
<br />

<div style="font-size: 1.20em;">

<strong>Mensagens e Avisos</strong>
<p><strong>&raquo; 
	<html:link module="/manager" page="/advisoriesManagement/listCurrentAdvisories.faces">
		Gestão de Avisos
	</html:link>
</strong></p>
<p><strong>&raquo; 
	<html:link module="/manager" page="/sendMail.faces">
		Envio de E-mails
	</html:link>
</strong></p>
<br />
<strong><bean:message bundle="MANAGER_RESOURCES" key="title.manager.organizationalStructureManagement"/></strong>
<p><strong>&raquo; 
	<html:link module="/manager" page="/organizationalStructureManagament/listAllUnits.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="link.manager.organizationalStructureManagement" />
	</html:link>
</strong></p>

<br />
<strong>Gestão de Execuções</strong>
<p><strong>&raquo; 
		<html:link module="/manager" module="/manager" page="/readDegrees.do">
			<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.readDegrees" />
		</html:link>
</strong></p>
<p><strong>&raquo; 
	  	<html:link module="/manager" module="/manager" page="/curricularPlans/chooseCurricularPlan.faces">
	  		Consulta de Currículo
	  	</html:link>  
</strong></p>

<p><strong>&raquo; 
	  	<html:link module="/manager" module="/manager" page="/degree/chooseDegreeType.faces">
	  		Criação de Cursos de Execução
	  	</html:link>  
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/manageExecutionPeriods.do?method=prepare">
		Gestão de Periodos Execução
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/executionCourseManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/competenceCourseManagement.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.competence.course.management" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/manageEnrolementPeriods.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period" />
	</html:link>
</strong></p>

<br />
<strong>Gestão de Pessoal</strong>
<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/teachersManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/studentsManagement.do?method=show">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/personManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.personManagement" />
	</html:link>
</strong></p>

<br />
<strong><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manageFiles"/></strong>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/generateFiles.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles"/>
	</html:link>
</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/uploadFiles.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.uploadFiles"/>
	</html:link>
</strong></p>
<br />

<strong><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manageFinance"/></strong>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/guideManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.guidesManagement"/>
	</html:link>	
</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/gratuity/updateGratuitySituations.faces">
		Actualizar Situações de Propina
	</html:link>
</strong></p>
<br/>
<strong>Gestão de CMS</strong>
<p><strong>&raquo; 
	<html:link module="/cms" action="/personalGroupsManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" key="link.userGroupsManagement" />
	</html:link>
	</strong></p>
<p><strong>&raquo;
	<html:link module="/cms" action="/cmsConfigurationManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" key="link.cmsConfiguration" />
	</html:link>	
</strong></p>
<p><strong>&raquo;
	<html:link module="/cms" action="/executionCourseWebsiteManagement?method=viewAll" >
		<bean:message bundle="MANAGER_RESOURCES" key="cms.executionCourseWebsite.label" />
	</html:link>	
</strong></p>
<br />
<strong>Gestão de Suport</strong>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/manageGlossary.do?method=prepare">
		Gestão de Glossário
	</html:link>
</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/manageFAQs.do?method=prepare">
		Gestão de FAQ's
	</html:link>
</strong></p>

<br />
<strong>Gestão do Sistema</strong>
<p><strong>&raquo; 

	<html:link module="/manager" page="/manageCache.do?method=prepare">
		Gestão da Cache
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorServices.do?method=monitor">
		Monitorização de Serviços
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorRequestLogs.do?method=listFiles">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="manager.monitor.requests.title"/>
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorUsers.do?method=monitor">
		Monitorização de Utilizadores
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorSystem.do?method=monitor">
		Informações do Sistema
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/somePage.faces">
		Java Server Faces Example
	</html:link>
<p><strong>&raquo; 
	<html:link module="/manager" page="/someStrutsPage.do?method=showFirstPage">
		Struts Example
	</html:link>
<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/changePasswordForward.do">
		Kerberos Test
	</html:link>
	
</strong></p>
<br />
<strong>Renderers</strong>
<p>
    <strong>&raquo;<html:link module="/manager" page="/renderers/reload.do">Reler configuração</html:link></strong>
</p>
<!-- FIXME: This is only temporary to show some examples of how renderers can be used -->
<p>
    <strong>&raquo;<html:link module="/manager" page="/renderers/index.do">Exemplos</html:link></strong>
</p>


</div>