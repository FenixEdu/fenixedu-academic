<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title>EP2002 - <bean:write name="Sitio" property="nome"/> </title>
  </head>
  <body>
    Nome: <bean:write name="Sitio" property="nome"/></br>
    Ano Curricular: <bean:write name="Sitio" property="anoCurricular"/></br>
    Semestre: <bean:write name="Sitio" property="semestre"/></br>
    Curso: <bean:write name="Sitio" property="curso"/></br>
    Departamento: <bean:write name="Sitio" property="departamento"/></br>
    Secções:<br/>

    <logic:iterate id="seccao" name="Sitio" property="seccoes">
    - <%= seccao %><br/>
    <%--      <html:link page="/mostrarSeccao.do?nomeSitio=<%= request.getAttribute("Sitio").getNome()%>&nomeSeccao=<%= seccao %>"><%= seccao %></html:link><br/>--%>
    </logic:iterate>
  </body>
</html>