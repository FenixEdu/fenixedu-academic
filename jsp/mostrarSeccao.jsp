<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title>EP2002 - <bean:write name="Seccao" property="nome"/></title>
  </head>
  <body>
    Nome: <bean:write name="Seccao" property="nome"/></br>
    <logic:iterate id="item" name="Seccao" property="itens">
      Nome: <bean:write name="item" property="nome"/><br/>
      Informacao: <bean:write name="item" property="informacao"/><br/>
      Urgente: <bean:write name="item" property="urgente"/><br/>
      <br/>
    </logic:iterate>
  </body>
</html>