<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<html>
  <head>
    <title><bean:message key="candidate.titleAuthentication" /></title>
  </head>
  <body>
     <table>
      <html:form action="/authenticationForm">
        <tr>
          <td colspan=2><h2><bean:message key="candidate.labelAuthentication" /></h2></td>
        </tr>
        <!-- input utilizador -->
        <tr>
          <td><bean:message key="candidate.username" /></td>
          <td><html:text property="username"/></td>
          <td><html:errors property="username"/></td>
        </tr>
        
        <!-- input password -->
        <tr>
          <td><bean:message key="candidate.password" /></td>
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