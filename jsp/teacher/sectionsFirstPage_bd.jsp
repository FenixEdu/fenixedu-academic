<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<p>A opção Gestão de Secções permite criar secções e sub-secções opcionais nas páginas da disciplina para conterem informação como listas de problemas, material de apoio, etc.
<br/>
Se desejar ver um exemplo pode consultar a página de disciplina de  <html:link href="<%= request.getContextPath()+"/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=34661" %>"  target="_blank">Programação com Objectos</html:link>.<br/> Note que nesta página de disciplina aparecem no menu de navegação do lado esquerdo as secções: Avaliação Conhecimentos, Material de Apoio e Planificação.
</p>
<p>Para começar deverá criar uma nova secção. Uma secção tem um título e dentro da mesma pode inserir itens e subsecções. Os itens podem conter a informação que desejar, inclusivé documentos.
</p>
