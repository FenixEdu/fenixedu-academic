<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<app:checkLogon/>

<html:html>

<head>
<title><bean:message key="consultarFuncionario.titulo"/></title>
<html:base/>
</head>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
<div align="left">
  <html:form action="/consultarFuncionario" focus="numMecanografico">
    <table width="600" border="0" align="left" cellpadding="0" cellspacing="0" background="Imagens/bg_pattern.gif" nowrap="nowrap">
      <tr bgcolor='#ffffff'>
        <td> 
          <html:errors/>
        </td>
      </tr>      
      <tr>
        <td> 
          <div style='width=580' align="right">
            <font size="5" face="Arial, Helvetica, sans-serif">
              <bean:message key="consultarFuncionario.titulo" />
            </font>
          </div><br>
        </td>
      </tr>

      <tr>
        <td>
          <table width="100%" border="0">
            <tr>
              <td colspan='2' align="center">
                &nbsp;&nbsp;&nbsp; <p></p>
              </td>
            </tr>

            <tr>	
              <td bgcolor="#FFFFCC" align="right" width='25%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.numMecanografico"/>:
                </font>
              </td>
              <td align="left">
                <html:text property="numMecanografico" size="4" maxlength="4"/>
              </td>
            </tr>

            <tr>
              <td colspan='2' align="center">
                &nbsp;&nbsp;&nbsp; <p></p><p></p>
              </td>
            </tr>

            <tr>
              <td colspan='2' align="center">
                <html:submit property="submit" style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                  &nbsp;&nbsp;&nbsp;<bean:message key="botao.consultar"/>&nbsp;&nbsp;&nbsp;
                </html:submit>
               <html:reset style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                  <bean:message key="botao.apagar"/>
                </html:reset>
                <html:cancel style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                  <bean:message key="botao.cancelar"/>
                </html:cancel>
              </td>
            </tr>
          </table>
        </td>
      </tr>

      <tr>
        <td colspan='2'><br></td>       
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