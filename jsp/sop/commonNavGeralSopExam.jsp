<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<ul id="navgeral">
	<li><html:link page="/home.do">Home</html:link></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0">Gestão de Disciplinas</html:link></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare">Gestão de Horários</html:link></li>
	<li><html:link page="/principalSalas.do">Gestão de Salas</html:link></li>
	<li><html:link page="/mainExamsNew.do?method=prepare" styleClass="active">Gestão de Exames</html:link></li>
</ul>