<%@page contentType="text/html" import="java.util.Calendar"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<app:checkLogon/>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">	
<base target="_output"/>
</head>

<body link="#333399" vlink="#333399" alink="#333399" leftmargin="0" topmargin="0">  
<div align="left"> 
  <table width="600" height="117" border="0" align="left" celspaccing="0" cellpadding="0" nowrap="wrap">
    <tr>
      <td>
	    <table width="600" border="0" align="left" celspaccing="0" cellpadding="0" background="Imagens/bg_pattern.gif" nowrap="wrap">
    	  <tr>
      	    <td>
              <font style="text-decoration=none" color="#333399" size="3" face="Arial, Helvetica, sans-serif">
        	  &nbsp;&nbsp;<b>
        	  <html:link forward="ConsultarFuncionarioMostrarAction"><bean:message key="portalAssiduidade"/></html:link>
        	  &nbsp;&nbsp;&middot;&nbsp;&nbsp;
        	  <html:link forward="AlterarPassword"><bean:message key="menuPrincipal.alterarPassword"/></html:link><b>	    	  
        	  &nbsp;&nbsp;&middot;&nbsp;&nbsp;
        	  <html:link forward="Logoff" target="_parent"><bean:message key="logoff"/></html:link><b>
	    	  </font>
            </td>
    	  </tr>
	    </table>
      </td>
    </tr>
	<tr bgcolor="#FFFFFF"> 
	  <td align="center" valign="top" nowrap="wrap"> 
	    <p align="right"><img src="Imagens/ap_admini.gif" width="374" height="63"></p>
	  </td>
	</tr>
	<tr bgcolor="#FFFFFF"> 
	   <td width="600" bgcolor="#000099">
	     <font color="#FFFFFF"> 
	       <bean:message key="prompt.benvindo" />&nbsp;
	       <bean:write name="user" property="nome" scope="session"/>&nbsp;&nbsp;&nbsp;&nbsp;
	       <% Calendar agora = Calendar.getInstance();%> 
	       <% 
	       	String dia = String.valueOf(agora.get(Calendar.DAY_OF_MONTH)); 
	       	if(agora.get(Calendar.DAY_OF_MONTH) < 10){
	       		dia = "0" + dia;
	       	}
	       	String mes = String.valueOf(agora.get(Calendar.MONTH)+1); 
	       	if((agora.get(Calendar.MONTH)+1) < 10){
	       		mes = "0" + mes;
	       	}
	       	/*String horas = String.valueOf(agora.get(Calendar.HOUR_OF_DAY)); 
	       	if(agora.get(Calendar.HOUR_OF_DAY) < 10){
	       		horas = "0" + horas;
	       	}
	       	String minutos = String.valueOf(agora.get(Calendar.MINUTE)); 
	       	if(agora.get(Calendar.MINUTE) < 10){
	       		minutos = "0" + minutos;
	       	}
	       	String segundos = String.valueOf(agora.get(Calendar.SECOND)); 
	       	if(agora.get(Calendar.SECOND) < 10){
	       		segundos = "0" + segundos;
	       	}*/	       		
	       %>
	       
	       <%= dia%>/<%= mes%>/<%= agora.get(Calendar.YEAR)%>&nbsp;	       
	     </font>
	   </td>
	</tr>
  </table>
</div> 
</body>
</html:html>