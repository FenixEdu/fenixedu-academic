<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:html xhtml="true">
<head>
<title><bean:message key="alterarPassword.titulo"/></title>
<html:base/>
</head>
<body leftmargin="0" topmargin="0">
<div align="left">
  <html:form action="/alterarPassword" focus="passwordAntiga">
    <table border="0" align="left" cellpadding="0" cellspacing="0" nowrap="nowrap">
      <tr>
        <td> 
          <html:errors/>
        </td>
      </tr>
      <tr>
        <td> 
          <div style='width=580' align="right">
            <font size="5" face="Arial, Helvetica, sans-serif">
              <bean:message key="alterarPassword.titulo" />
            </font>
          </div><br/>
        </td>
      </tr>

      <tr>
        <td> 
          <table width="100%" border="0">
            <tr>	
              <td bgcolor="#FFFFCC" align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">                
                  <bean:message key="prompt.passwordAntiga"/>:
                </font>
              </td>
              <td align="left">
                <html:password bundle="HTMLALT_RESOURCES" altKey="password.passwordAntiga" name="AlterarPasswordForm" property="passwordAntiga" size="16" maxlength="16" />
              </td>
            </tr>

            <tr>	
              <td bgcolor="#FFFFCC" align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.passwordNova"/>:
                </font>
              </td>
              <td align="left">
                <html:password bundle="HTMLALT_RESOURCES" altKey="password.passwordNova" name="AlterarPasswordForm" property="passwordNova" size="16" maxlength="16" />
              </td>
            </tr>
           
            <tr>	
              <td bgcolor="#FFFFCC" align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.passwordNovaConfirmacao"/>:
                </font>
              </td>
              <td align="left">
                <html:password bundle="HTMLALT_RESOURCES" altKey="password.passwordNovaConfirmacao" name="AlterarPasswordForm" property="passwordNovaConfirmacao" size="16" maxlength="16" />
              </td>
            </tr>

            <tr>
              <td colspan='2' align="center">
                &nbsp;&nbsp;&nbsp; <p></p><p></p>
              </td>
            </tr>

            <tr>
              <td align="center" colspan='2'>
                <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="submit" style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                    &nbsp;&nbsp;&nbsp;<bean:message key="botao.ok"/>&nbsp;&nbsp;&nbsp;
                </html:submit>
                <html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                    <bean:message key="botao.apagar"/>
                </html:reset>
                <html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                    <bean:message key="botao.cancelar"/>
                </html:cancel>
              </td>
            </tr>
          </table>
        </td>
      </tr>

      <tr>
        <td colspan='2'><br/></td>       
      </tr>

      <tr>
        <td>
          <table width="600" align="left" bgcolor="#333399" nowrap="nowrap">
            <tr> 
              <td width="600">
                <div align="center">
                  <font color="#FFFFFF">Copyright 2002 - N&uacute;cleo de Aplica&ccedil;&otilde;es do CIIST</font>
                </div>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>

  </html:form>
</div>	
</body>
</html:html>