<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html xhtml="true">
<head>
<title><bean:message key="alterarPassword.titulo"/></title>
<html:base/>
</head>

<body leftmargin="0" topmargin="0">

<div align="left">
  <table width="600" border="0" align="left" cellpadding="0" cellspacing="0" background="../../Imagens/bg_pattern.gif" nowrap="nowrap">
    <tr bgcolor='#ffffff'>
      <td> 
        <html:errors/>
      </td>
    </tr>
    <tr>
      <td>
          <div style='width=580' align="right">
            <font size="5" face="Arial, Helvetica, sans-serif">
              <bean:message key="alterarPassword.titulo"/>
            </font>
          </div><br>
      </td>
    </tr>
    <tr>
      <td align="center">
        &nbsp;&nbsp;&nbsp; <p></p>
      </td>
    </tr>
    <tr>
      <td>
          <div style='width=580' align="center">
            <font size="3" face="Arial, Helvetica, sans-serif">
              <bean:message key="confirmacao.alterarPassword"/>
            </font>
          </div><br>
      </td>
    </tr>
    <tr>
      <td align="center">
        &nbsp;&nbsp;&nbsp; <p></p>
      </td>
    </tr>
    <tr>
      <td align="center">
        <html:link forward="PortalAssiduidadeAction"><bean:message key="link.menu"/></html:link> 
      </td>
    </tr>      
    <tr>
      <td><br></td>       
    </tr>
    <tr bgcolor='#ffffff'>
      <td> 
       <table width="600" border="0" bgcolor="#333399">
          <tr> 
            <td width="600" valign="bottom">
              <div align="center"><font color="#FFFFFF">Copyright 
              2002 - Servi&ccedil;ode Aplica&ccedil;&otilde;es do CIIST</font></div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</div>
</body>
</html:html>