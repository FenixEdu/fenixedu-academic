<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title>SOP - Excepção</title>
  </head>
  <body>
    <html:errors/>
   <center> <b> Ocorreu um erro! </b> </center>
     <br/> Se desejar avisar a equipa de desenvolvimento do erro,
     preencha os campos que achar necessários e carregue em submeter 
     (todos os campos são opcionais)! 
     <br/>Caso contrário carregue em voltar atrás.
     <br/>
    <html:link page="/exceptionHandling.do?method=goBack">
    Voltar Atrás!
    </html:link>
    <html:form action="/exceptionHandling.do?method=sendEmail">
    <table align="center"  cellpadding='20' cellspacing='10'>
    	<tr><td>
    	Endereço de E-Mail:</td><td>
    	<html:text property="email" value=""/> </td>
    	</tr>
    	<tr><td>
    	Assunto:</td><td>
   		<html:text property="subject" value=""/></td>
   		</tr>
    	<tr><td>
   		Mensagem:</td><td>
    	<html:textarea property="body" value=""/></td>
    	</tr>
    	
      </table>	
      <center>
    	<html:submit >
   		 <bean:message key="label.ok"/>
    	</html:submit>
   	  </center>
    </html:form>
  </body>
</html>
