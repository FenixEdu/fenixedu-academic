<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">

<head>
<title><bean:message key="PasswordEsquecida.titulo"/></title>
<html:base/>
</head>

<body topmargin=0 leftmargin=0>

<html:errors/>
<table width="302" border="0" align="center">
  <tr>
    <td>
      <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
      codebase="http://active.macromedia.com/flash4/cabs/swflash.cab#version=4,0,0,0"
      id="ist2002" width="481" height="365">
        <param name="movie" value="../../ist2002.swf">
        <param name="quality" value="high">
        <param name="bgcolor" value="#FFFFFF">
      
        <embed name="ist2002" src="../../ist2002.swf" quality="high" bgcolor="#FFFFFF"
        width="481" height="365"
        type="application/x-shockwave-flash"
        pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"> 
        </embed> 
      </object>
    </td>
    <td width="300" align="center" valign="middle">
      <br>
      <div align="center" style="color:#000080; font-size:28px;letter-spacing:10pt ">
       PASSWORD
      </div>
      <hr width="250" noshade>
      <br><b><font color="#000080"><bean:message key="aviso.passwordEmail" /></font></b><br>
      <html:form action="/forgotPassword" focus="username">               
        <div style="font-size:14px">
          <table align="center" border="0" width="300" celspaccing="0" cellpadding="0" >
            <tr>
              <th align="right">
                <bean:message key="prompt.username"/> 
              </th>
              <td align="left">
                <html:text style="width: 100px; color:#000080; font-size=10px; background-color:#f5e8bc" property="username" size="16" maxlength="16"/>
              </td>
            </tr>
            <tr>
              <th align="right">
                <bean:message key="prompt.numeroDocumentoIdentificacaoAbreviado"/>
              </th>
              <td align="left">
                <html:password style="width: 100px; color:#000080; font-size=10px;background-color:#f5e8bc" property="numeroDocumentoIdentificacao" size="16" maxlength="16" redisplay="false"/>
              </td>
            </tr>            
          </table>
        </div>
        <br>
        <html:submit value="Submit" style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px"/>     
        <html:reset style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px"/>    
        <html:cancel style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px"/>
      </html:form>
      <hr width="250" noshade>
      <div align="center" style="color:#000080; font-size:14px">
        &copy;2002 - Núcleo de Aplicações do CIIST
      </div>
    </td>
  </tr>
</table>
</body>
</html:html>