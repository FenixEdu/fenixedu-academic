<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html:html>
  <head>
    <title>EP2002 - Emitir Certificado</title>
  </head>
  <body>
    <table>
  	    <tr>
          		<td>
          			<jsp:include page="baseObtainCertificate.jsp"/>
          		</td>
         </tr>
         <tr>
          		<td>
          			<B>concluiu o curso especializado conducente à obtenção do grau de mestre em:</B>
          		</td>
         </tr>
          <tr>
          		<td>
			         <B><bean:write name="infoCourse" property="name"/></B>
           		</td>
         </tr>
         <tr>
          		<td>
			        <B>em ???data de termino??? com a média de ???média??? (???média por extenso???) valores.</B>
           		</td>
         </tr>
          <tr>
          		<td>
			        <B>Mais certifico, que o grau de mestre só será conferido após a elabo-</B>
           		</td>
         </tr>
          <tr>
          		<td>
			        <B>ração, aprovação e discussão de uma dissertação original, nos termos da alínea</B>
           		</td>
         </tr>        
          <tr>
          		<td>
			         <B><bean:message key="label.decreto.lei.mestrado"/>.</B>
           		</td>
         </tr>
        <tr>
          		<td>
          			<jsp:include page="dataObtainCertificate.jsp"/>
          		</td>
         </tr>
		</table>
  </body>
  </html:html>
