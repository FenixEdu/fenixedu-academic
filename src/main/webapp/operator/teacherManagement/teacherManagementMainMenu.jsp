<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<ul>
	<li>
		<html:link page="/back.do">
			<bean:message key="label.return" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>
		
	<li class="navheader">Gestão de Docentes</li>
		<li>
			<html:link page="/teacherCategoriesManagement.do?method=prepareEdit">
				<bean:message key="edit.categories" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/dissociateProfShipsAndRespFor.do?method=prepareDissociateEC">
				<bean:message key="label.manager.teachersManagement.removeECAssociation" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link module="/researcher" page="/teacherEvaluation.do?method=viewManagementInterface">
				<bean:message key="label.manager.teachersManagement.evaluation" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		
</ul>