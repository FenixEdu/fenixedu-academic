<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>




<h3>Adicionar Docente à Equipa de Coordenação</h3>
<html:form action="/addCoordinator">
<html:hidden property="method" value="AddCoordinator" />
<html:text property="teacherNumber" />

<html:submit/>
<br/>
<br/>

</html:form>




