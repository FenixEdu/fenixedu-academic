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
          			<B>do curso de <bean:write name="infoStudent" property="degreeType"/> em 
          			<bean:write name="infoCourse" property="name"/></B>
          		</td>
        </tr>
        <tr>
          		<td>
          			<B>ministrado neste Instituto, obteve aproveitamento nas disciplinas abaixo discri- </B>
          		</td>
        </tr>
        <tr>
          		<td>
          			<B>minadas, que fazem parte do curso especializado conducente à obtenção do grau de</B>
          		</td>
        </tr>
        <tr>
	    		<td>
	    			<B>mestre :</B>
          		</td>
        </tr>
        <tr>
	    		<td><BR>
          		</td>
        </tr>
    <logic:present name="disciplinesList" scope="session">   
	<logic:iterate id="discipline" name="disciplinesList" scope="session"> 
		<tr>
				<td>
					<B><jsp:getProperty name="discipline" property="nome"/></B>
				</td>
		</tr>
	</logic:iterate>
    </logic:present>
	<logic:notPresent name="disciplinesList" scope="session">
		<tr>
				<td>
					<font color='red'><B> <bean:message key="message.student.enrolment.discipline.not.available"/></B> </font>
				</td>
		</tr>
		</logic:notPresent>
        <tr>
          		<td>
          			<jsp:include page="dataObtainCertificate.jsp"/>
          		</td>
         </tr>
		</table>
  </body>
  </html:html>
