<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<span class="error"><html:errors /></span>
<p>A op��o Gest�o de Sec��es permite criar sec��es e sub-sec��es opcionais nas p�ginas da disciplina para conterem informa��o como listas de problemas, material de apoio, etc.
<br/>
Se desejar ver um exemplo pode consultar a p�gina de disciplina de  <html:link href="<%= request.getContextPath()+"/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=34661" %>"  target="_blank">Programa��o com Objectos</html:link>.<br/> Note que nesta p�gina de disciplina aparecem no menu de navega��o do lado esquerdo as sec��es: Avalia��o Conhecimentos, Material de Apoio e Planifica��o.
</p>
<p>Para come�ar dever� criar uma nova sec��o. Uma sec��o tem um t�tulo e dentro da mesma pode inserir itens e subsec��es. Os itens podem conter a informa��o que desejar, inclusiv� documentos.
</p>
