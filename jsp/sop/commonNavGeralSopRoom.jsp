<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<ul id="navgeral">
    <li><html:link page="/home.do">Home</html:link></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0">Gest�o de Disciplinas</html:link></li>
    <li><html:link page="/chooseExecutionPeriod.do?method=prepare">Gest�o de Hor�rios</html:link></li>
    <li><html:link page="/principalSalas.do" styleClass="active">Gest�o de Salas</html:link></li>
    <li><html:link page="/mainExamsNew.do?method=prepare">Gest�o de Exames</html:link></li>
</ul>