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
<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.messages.and.notices" /></li>
<li> 
	<html:link module="/manager" page="/advisoriesManagement/listCurrentAdvisories.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="title.notices" />
	</html:link>
</li>
<li> 
	<html:link module="/manager" page="/sendMail.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="title.emails" />
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

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.teaching.structure" /></li>
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

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.executions"/></li>
<li> 
	<html:link module="/manager" module="/manager" page="/manageExecutionPeriods.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="title.execution.periods"/>
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

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.people"/></li>
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

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.equivalencies"/></li>
<li> 
	<html:link module="/manager" module="/manager" page="/prepareNotNeedToEnroll.do">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.notNeedToEnrol" />
	</html:link>
</li>

<li> 
	<html:link module="/manager" module="/manager" page="/curricularCourseEquivalencies.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.equivalencies" />
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
		<bean:message bundle="MANAGER_RESOURCES" key="title.gratuity.situations"/>
	</html:link>
</li>

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.cms"/></li>
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

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.support"/></li>
<li>
	<html:link module="/manager" module="/manager" page="/manageGlossary.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="title.glossary"/>
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/manageFAQs.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="title.faqs"/>
	</html:link>
</li>

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.objects"/></li>
<li> 
	<html:link module="/manager" page="/manageCache.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="title.cache"/>
	</html:link>
</li>
<li>
	<html:link module="/manager"page="/domainObjectStringPropertyFormatter.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="title.properties"/>
	</html:link>
</li>

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.system"/></li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorSystem.do?method=monitor">
		<bean:message bundle="MANAGER_RESOURCES" key="title.system.information"/>
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorServices.do?method=monitor">
		<bean:message bundle="MANAGER_RESOURCES" key="title.services"/>
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/transactionSystem.do?method=view">
		<bean:message bundle="MANAGER_RESOURCES" key="title.transaction.logs"/>
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorRequestLogs.do?method=listFiles">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="manager.monitor.requests.title"/>
	</html:link>
</li>
<li> 
	<html:link module="/manager" module="/manager" page="/monitorUsers.do?method=monitor">
		<bean:message bundle="MANAGER_RESOURCES" key="title.users"/>
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/changePasswordForward.do">
		<bean:message bundle="MANAGER_RESOURCES" key="title.kerberos.test"/>
	</html:link>
</li>
<li>
	<html:link module="/manager" module="/manager" page="/cron.do?method=showScripts&amp;page=0">
		<bean:message bundle="MANAGER_RESOURCES" key="title.cron"/>
	</html:link>
</li>

<li class="navheader"><bean:message bundle="FUNCTIONALITY_RESOURCES" key="title.functionalities" /></li>
<li> 
    <html:link module="/manager/functionalities" page="/toplevel/view.do">
        <bean:message bundle="FUNCTIONALITY_RESOURCES" key="link.functionalities" />
    </html:link>
</li>
<li> 
    <html:link module="/manager/functionalities" page="/filter/index.do">
        <bean:message bundle="FUNCTIONALITY_RESOURCES" key="link.filter.test" />
    </html:link>
</li>

<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="title.frameworks"/></li>
<li>
	<html:link module="/manager" page="/someStrutsPage.do?method=showFirstPage">
		<bean:message bundle="MANAGER_RESOURCES" key="title.struts"/>
	</html:link>
</li>
<li> 
    <html:link module="/manager" page="/reloadStruts.do">
    	<bean:message bundle="MANAGER_RESOURCES" key="title.relaod.struts"/>
    </html:link>
</li>
<br/>
<li> 
	<html:link module="/manager" module="/manager" page="/somePage.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="title.faces"/>
	</html:link>
</li>
<br/>
<li>
    <html:link module="/manager" page="/renderers/index.do">
	    <bean:message bundle="MANAGER_RESOURCES" key="title.renderers"/>
    </html:link>
</li>
<li>
    <html:link module="/manager" page="/renderers/reload.do">
	    <bean:message bundle="MANAGER_RESOURCES" key="title.reload.renderers"/>
    </html:link>
</li>

</ul>