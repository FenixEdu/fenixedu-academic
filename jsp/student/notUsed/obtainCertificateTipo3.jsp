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
					<B>tem ??(Matricula | Matricula e Inscricao | Inscricao)?? no ano lectivo de ??ano lectivo??, no curso 
							de mestrado em:</B>
				</td>
		</tr>
		<tr>
				<td>
					<B><bean:write name="infoCourse" property="name"/></B>
				</td>
		</tr>
		<logic:present name="disciplinesList" scope="session">   
		<tr>
				<td>
						<B>nas seguintes disciplinas :</B>
				</td>
		</tr>
		<logic:iterate id="discipline" name="disciplinesList" scope="session"> 
		<tr>
				<td>
					<B><jsp:getProperty name="discipline" property="nome"/></B>
				</td>
		</tr>
		</logic:iterate>
    	</logic:present>
    	<tr>
          		<td>
          			<jsp:include page="dataObtainCertificate.jsp"/>
          		</td>
         </tr>
    	</table>
  </body>
  </html:html>
