<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html>
  <head>
    <title>EP2002 - Autenticação</title>
  </head>
  <body>
     <table>
      <html:form action="/autenticacaoForm">
        <tr>
          <td colspan=2><h2>Autenticação</h2></td>
        </tr>
        <!-- input utilizador -->
        <tr>
          <td>utilizador</td>
          <td><html:text property="utilizador"/></td>
          <td><html:errors property="utilizador"/></td>
        </tr>
        
        <!-- input password -->
        <tr>
          <td>password</td>
          <td><html:password property="password"/></td>
          <td><html:errors property="password"/></td>
        </tr>
        <tr>
          <td colspan=2><html:submit property="ok"/></td>
        </tr>
      </html:form>	
    </table>
  </body>
</html>