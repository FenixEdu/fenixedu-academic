<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul id="navgeral">
    <li><html:link page="/home.do" styleClass="active">Home</html:link></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0">Gestão de Disciplinas</html:link></td>
    <li><html:link page="/prepararEscolherContexto.do">Gestão de Horários</html:link></li>
    <li><html:link page="/principalSalas.do">Gestão de Salas</html:link></li>
    <li><html:link page="/mainExamesNew.do">Gestão de Exames</html:link></li>
    <li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></html:link></li>
</ul>