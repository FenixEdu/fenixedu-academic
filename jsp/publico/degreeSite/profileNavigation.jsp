<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="institutionUrlStudents" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/aluno.shtml</bean:define>
<bean:define id="institutionUrlTeachers" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/docente.shtml</bean:define>
<bean:define id="institutionUrlEmployees" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/funcionario.shtml</bean:define>
<bean:define id="institutionUrlCandidates" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/candidato.shtml</bean:define>
<bean:define id="institutionUrlInternational" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/international.shtml</bean:define>
<ul>
	<li><a href="<%= institutionUrl %>">In&iacute;cio</a></li>
  	<li><a href="<%= institutionUrlStudents %>">Aluno</a></li>
  	<li><a href="<%= institutionUrlTeachers %>">Docente</a></li>
	<li><a href="<%= institutionUrlEmployees %>">N&atilde;o Docente</a></li>
	<li><a href="<%= institutionUrlCandidates %>">Candidato</a></li>	
  	<li><a href="<%= institutionUrlInternational %>">International</a></li>
</ul>