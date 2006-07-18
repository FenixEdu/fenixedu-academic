<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<center>
	<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" width="100" height="100" />
</center>
<br/>

<ul>
<li> 
	<html:link module="/manager" page="/index.do">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
</li>
<li class="navheader">Mensagens e Avisos</li>
<li> 
	<html:link module="/manager" page="/advisoriesManagement/listCurrentAdvisories.faces">
		Gestão de Avisos
	</html:link>
</li>
<li> 
	<html:link module="/manager" page="/sendMail.faces">
		Envio de E-mails
	</html:link>
</li>

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.manager.organizationalStructureManagement"/></li>
<li> 
	<html:link module="/manager" page="/organizationalStructureManagament/listAllUnits.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="link.manager.organizationalStructureManagement" />
	</html:link>
</li>
<li> 
	<html:link module="/manager" page="/rulesManagement.do?method=listRules">
		<bean:message bundle="MANAGER_RESOURCES" key="link.manager.rules.management" />
	</html:link>
</li>

<li class="navheader">Gestão da Estrutura de Ensino</li>
<li> 
	<html:link module="/manager" module="/manager" page="/readDegrees.do">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.readDegrees" />
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/competenceCourseManagement.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.competence.course.management" />
	</html:link>
</li>
<li> 
  	<html:link module="/manager" module="/manager" page="/curricularPlans/chooseCurricularPlan.faces">
	  	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.oldCurricularPlans" />
  	</html:link>  
</li>
<li> 
  	<html:link module="/manager" page="/bolonha/curricularPlans/curricularPlansManagement.faces">
  		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.bolonhaCurricularPlansManagement" />
  	</html:link>  
</li>
<li> 
  	<html:link module="/manager" page="/bolonha/competenceCourses/competenceCoursesManagement.faces">
  		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.bolonhaCompetenceCoursesManagement" />
  	</html:link>  
</li>

<li class="navheader">Gestão de Execuï¿½ï¿½es</li>
<li> 
	<html:link module="/manager" module="/manager" page="/manageExecutionPeriods.do?method=prepare">
		Periodos Execuï¿½ï¿½o
	</html:link>
</li>
<li> 
	<html:link module="/manager" page="/executionDegreesManagementMainPage.do">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegreeManagement" />
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/executionCourseManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement" />
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/manageEnrolementPeriods.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period" />
	</html:link>
</li>

<li class="navheader">Gestão de Pessoal</li>
<li> 
	<html:link module="/manager" module="/manager" page="/teachersManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement" />
	</html:link>
</li>

<li> 
	<html:link module="/manager" module="/manager" page="/studentsManagement.do?method=show">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement" />
	</html:link>
</li>

<li>
	<html:link module="/manager" module="/manager" page="/personManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.personManagement" />
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/manageHolidays.do?method=prepare&amp;page=0">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manage.holidays" />
	</html:link>
</li>

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manageFiles"/></li>
<li>
	<html:link module="/manager" module="/manager" page="/generateFiles.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles"/>
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/uploadFiles.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.uploadFiles"/>
	</html:link>
</li>

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manageFinance"/></li>
<li>
	<html:link module="/manager" module="/manager" page="/guideManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.guidesManagement"/>
	</html:link>	
</li>
<li>
	<html:link module="/manager" module="/manager" page="/gratuity/updateGratuitySituations.faces">
		Actualizar Situaï¿½ï¿½es de Propina
	</html:link>
</li>

<li class="navheader">Gestão de CMS</li>
<li> 
	<html:link module="/cms" action="/personalGroupsManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" key="link.userGroupsManagement" />
	</html:link>
	</li>
<li>
	<html:link module="/cms" action="/cmsConfigurationManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" key="link.cmsConfiguration" />
	</html:link>	
</li>
<li>
	<html:link module="/cms" action="/executionCourseWebsiteManagement?method=viewAll" >
		<bean:message bundle="MANAGER_RESOURCES" key="cms.executionCourseWebsite.label" />
	</html:link>	
</li>

<li class="navheader">Gestão do Suporte</li>
<li>
	<html:link module="/manager" module="/manager" page="/manageGlossary.do?method=prepare">
		Gestão de Glossï¿½rio
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/manageFAQs.do?method=prepare">
		Gestão de FAQ's
	</html:link>
</li>

<li class="navheader">Gestão de Objectos</li>
<li> 
	<html:link module="/manager" page="/manageCache.do?method=prepare">
		Gestão da Cache
	</html:link>
</li>
<li>
	<html:link module="/manager"page="/domainObjectStringPropertyFormatter.do?method=prepare">
		Formataï¿½ï¿½o de Propriedades
	</html:link>
</li>

<li class="navheader">Gestão do Sistema</li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorServices.do?method=monitor">
		Monitorizaï¿½ï¿½o de Serviï¿½os
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorRequestLogs.do?method=listFiles">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="manager.monitor.requests.title"/>
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorUsers.do?method=monitor">
		Monitorizaï¿½ï¿½o de Utilizadores
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorSystem.do?method=monitor">
		Informaï¿½ï¿½es do Sistema
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/changePasswordForward.do">
		Kerberos Test
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/cron.do?method=showScripts&amp;page=0">
		Cron
	</html:link>
</li>

<li class="navheader">Frameworks</li>
<li>
	<html:link module="/manager" page="/someStrutsPage.do?method=showFirstPage">
		Struts Example
	</html:link>
</li>
<li> 
    <html:link module="/manager" page="/reloadStruts.do">
        Reload Struts Configuration
    </html:link>
</li>
<br/>
<li> 
	<html:link module="/manager" module="/manager" page="/somePage.faces">
		Java Server Faces Example
	</html:link>
</li>
<br/>
<li>
    <html:link module="/manager" page="/renderers/index.do">Exemplos Renderers</html:link>
</li>
<li>
    <html:link module="/manager" page="/renderers/reload.do">Reler Configuraï¿½ï¿½o Renderers</html:link>
</li>

</ul>
