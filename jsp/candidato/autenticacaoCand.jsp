<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Login - Candidatos a P&#243;s-Gradua&ccedil;&#245;es</title>
    <link href="../CSS/logdotist_student.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  </head>
  <body>
     <p>&nbsp;</p>
     <table width="500"  border="0" align="center" cellpadding="0" cellspacing="0" class="tablebrd">
       <tr>
         <td class="ist"><img height="60" src="../images/dotist_student.gif" width="192" /></td>
       </tr>
       <tr>
         <td class="introtexto">
           <h1><br />
           Login - Candidatos a P&#243;s-Gradua&ccedil;&#245;es</h1>
           <p>O servi&ccedil;o <strong>.ist</strong> permite aos utilizadores
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
           <html:form action="/authenticationForm">
           <center><span class="error"><html:errors property="invalidAuthentication"/></span></center>
           <center><span class="error"><html:errors property="error.invalid.session"/></span></center>  
		   <table align='center' border="0">
           <!-- input username -->
           <tr>
             <td><bean:message key="candidate.username"/> </td>
             <td><html:text property="username"/></td>
             <td><span class="error"><html:errors property="username"/></span>
             </td>
           </tr>
           <!-- input password -->
           <tr>
             <td><bean:message key="candidate.password"/> </td>
             <td><html:password property="password"/></td>
             <td><span class="error"><html:errors property="password"/></span>
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
  </body>
</html>



