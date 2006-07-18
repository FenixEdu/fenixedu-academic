<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<span class="error"><html:errors /></span>
<p>A opï¿?ï¿?o Gestï¿?o de Secï¿?ï¿?es permite criar secï¿?ï¿?es e sub-secï¿?ï¿?es opcionais nas pï¿?ginas da disciplina para conterem informação como listas de problemas, material de apoio, etc.
<br/>
Se desejar ver um exemplo pode consultar a pï¿?gina de disciplina de  <html:link href="<%= request.getContextPath()+"/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=34661" %>"  target="_blank">Programaï¿?ï¿?o com Objectos</html:link>.<br/> Note que nesta pï¿?gina de disciplina aparecem no menu de navegaï¿?ï¿?o do lado esquerdo as secï¿?ï¿?es: Avaliaï¿?ï¿?o Conhecimentos, Material de Apoio e Planificaï¿?ï¿?o.
</p>
<p>Para comeï¿?ar deverï¿? criar uma nova secï¿?ï¿?o. Uma secï¿?ï¿?o tem um tï¿?tulo e dentro da mesma pode inserir itens e subsecï¿?ï¿?es. Os itens podem conter a informação que desejar, inclusivï¿? documentos.
</p>
