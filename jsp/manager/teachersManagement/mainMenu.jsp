<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%><style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<center>	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/></center>

<ul>
	<br/>
	<li> 
		<html:link module="/manager" page="/teachersManagement.do?method=mainPage">			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />		</html:link>	</li>
	
	<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachersManagement"/></li>	<li>
		<html:link module="/manager" page="/teacherCategoriesManagement.do?method=prepareEdit">
			<bean:message bundle="MANAGER_RESOURCES" key="edit.categories" />
		</html:link>
	</li>
	<li>
		<html:link module="/manager" page="/dissociateProfShipsAndRespFor.do?method=prepareDissociateEC">
			<bean:message bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement.removeECAssociation" />
		</html:link>
	</li>
