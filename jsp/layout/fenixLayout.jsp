<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <title><tiles:getAsString name="title"/></title>
  </head>

  <body>
    <html:errors/>
    <table cellpadding="2" cellspacing="2" border="0" width="100%">
      <tbody>
        <tr>
          <td rowspan="3" valign="center" width="140" bgcolor="#bdbdd6"><tiles:insert attribute="menu" /></td>
          <td valign="Top">
            <table cellpadding="2" cellspacing="2" border="0" width="100%">
              <tbody>
                <tr>
                  <td valign="center"><br><tiles:insert attribute="header" /></td>
                </tr>
                <tr>
                  <td valign="center"><br><tiles:insert attribute="body" /></td>
                </tr>
                <tr>
                  <td valign="center"><br> <tiles:insert attribute="footer" /></td>
                </tr>
              </tbody>
            </table>
          </td>
        </tr>
      </tbody>
    </table>
  </body>
</html>
