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
          			<B>prestou provas para obtenção do grau de mestre em:</B>
          		</td>
         </tr>
          <tr>
          		<td>
			         <B><bean:write name="infoCourse" property="name"/></B>
           		</td>
         </tr>
         <tr>
          		<td>
			         <B>concluidas em ???data de termino??? com a defesa da dissertação intitulada:</B>
           		</td>
         </tr>
          <tr>
          		<td>
			         <B>"????????????????dissertação???????????????"</B>
           		</td>
         </tr>
          <tr>
          		<td>
			         <B>Da acta da prova consta o seguinte resultado atribuído pelo júri legalmente </B>
           		</td>
         </tr>        
          <tr>
          		<td>
          			<B>constituído: APROVADO pelo que tem direito ao grau académico de MESTRE.</B>
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
