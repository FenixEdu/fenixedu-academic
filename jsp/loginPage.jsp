<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>.IST - Login</title>
    <link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  </head>
  <body>
     <p>&nbsp;</p>
     <table width="500"  border="0" align="center" cellpadding="0" cellspacing="0" class="tablebrd">
       <tr>
         <td class="ist"><img height="60" src="<%= request.getContextPath() %>/images/dotist_sop.gif" width="192" /></td>
       </tr>
       <tr>
         <td class="introtexto">           <h1><br />
           Login</h1>
           <p>O servi&ccedil;o <strong>.IST</strong> permite aos utilizadores
             certificados - alunos, docentes, funcion&aacute;rios - do <a href="http://www.ist.utl.pt">Instituto
             Superior T&eacute;cnico</a> utilizar diversos servi&ccedil;os e
             funcionalidades de forma a simplificar o quotidiano de todos aqueles
             que estudam ou trabalham. </p>
           <p>Caso precise de esclarecimentos adicionais ou n&atilde;o consiga
           efectuar o seu login contacte a equipa de suporte utilizando: <a href="mailto:suporte@dot.ist.utl.pt">suporte@dot.ist.utl.pt</a><br />
         </p>
         </td>
       </tr>
       <tr>
         <td><p>&nbsp;</p>
           <center><span class="error"><html:errors property="invalidAuthentication"/></span></center>
           <center><span class="error"><html:errors property="error.invalid.session"/></span></center>           
           <html:form action="/login" focus="username" >
			   <table align='center' border="0">
		           <!-- input utilizador -->
		           <tr>
		             <td>Username: </td>
		             <td><html:text property="username"/>
		             </td>
		             <td><span class="error"><html:errors property="username"/></span>
		             </td>
		           </tr>
		           <!-- input password -->
		           <tr>
		             <td>Password: </td>
		             <td>
					 	<html:password property="password" redisplay="false"/>
		             </td>
		             <td><span class="error"><html:errors property="password" /></span>
		             </td>
		           </tr>
	           </table>
	           <br/>
	           <table border="0" align='center'>
	             <tr>
	               <td align="right">
	                 <html:submit value="Submeter" styleClass="button" property="ok"/>
	               </td>
	               <td width='20'> </td>
	               <td align="left">
	                <html:reset value="Limpar" styleClass="button"/>
	             </td>
	             </tr>
	           </table>
		   </html:form>
         <p>&nbsp;</p></td>
       </tr>
  </table>
     <p>&nbsp;</p>	
     <%-- Invalidate session. 
     	  This is to work with FenixActionServlet --%>
     <% 	try{
     			session.invalidate();
     		}catch (Exception e){}
     %>
  </body>
</html>