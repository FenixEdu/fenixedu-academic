<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
  <head>
    <title>EP2002 - Seleccionar Sitio</title>
  </head>
  <body>
    <html:errors/>
    <logic:iterate id="sitio" name="Sitios">
      <html:link page="/mostrarSitio.do" paramId="nomeSitio" paramName="sitio"><%= sitio %></html:link><br/>
    </logic:iterate>
  </body>
</html>