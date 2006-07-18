<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<html>
    <head>
    	<title><bean:message key="title.masterDegree.administrativeOffice.printGuide"/></title>
    </head>

    <body>
    
    <bean:define id="candidate" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="session" />     
    
    <table width="100%" height="100%" border="0">
    <tr height="30"><td>
     <table width="100%" border="0" valign="top">
      <tr> 
        <td height="100" colspan="2">
          <table border="0" width="100%" height="100" align="center" cellpadding="0" cellspacing="0">
            <tr> 
              <td width="50" height="100">
               <img src="<%= request.getContextPath() %>/posGraduacao/guide/images/istlogo.gif" alt="<bean:message key="istlogo" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr valign="top" align="left"> 
                    <td>&nbsp;<b>INSTITUTO SUPERIOR T�CNICO</b><br>
                      &nbsp;<b>Secretaria da P�s-Gradua��o</b><br>
                      <hr size="1">
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
	</table>

	</td>
	</tr>
    <tr valign="top"><td>

	<table width="100%" border="0">
	 <tr>
	 <td>
      <table width="100%" border="0">
          <tr>
            <td width="40%"><strong>Processo de:</strong></td>
            <td width="60%">&nbsp;</td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
            <td> <bean:write name="candidate" property="infoPerson.nome"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.candidateNumber"/> </td>
            <td> <bean:write name="candidate" property="candidateNumber"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
            <td> <bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.executionYear"/> </td>
            <td> <bean:write name="candidate" property="infoExecutionDegree.infoExecutionYear.year"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.identificationDocumentNumber"/> </td>
            <td> <bean:write name="candidate" property="infoPerson.numeroDocumentoIdentificacao"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.identificationDocumentType"/> </td>
            <td> 
            	<bean:define id="idType" name="candidate" property="infoPerson.tipoDocumentoIdentificacao"/>
            	<bean:message key='<%=idType.toString()%>'/> 
            </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.username"/> </td>
            <td> <bean:write name="candidate" property="infoPerson.username"/> </td>
          </tr>
          
          <logic:present name="<%= SessionConstants.PRINT_PASSWORD %>">
		      <tr>
		        <td> <bean:message key="label.candidate.password"/> </td>
		        <td> <font face="Verdana"><bean:write name="candidate" property="infoPerson.password"/> </font></td>
		      </tr>
	      </logic:present>
          
          <tr>
            <td> <bean:message key="label.candidate.accessAddress"/> </td>
            <td> <bean:message key="label.candidate.url"/> </td>
          </tr>

          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          



	  </table>

	  <tr>
	  	<td>
		  <jsp:include page="./warning.jsp" flush="true" />
		</td>
	  </tr>

	 </td>
	 </tr>
	</table>
	</td>
	</tr>




	<tr height="30">
	<td>

    <table width="100%" border="0">
     <tr>	 
	 <td>
	 	<table align="center" width="100%" valign="bottom">
	      <tr>
          <td colspan="2" valign="bottom" >
            <div align="center">
              <font size="2"> Documento processado por computador.</font> 
            </div>
            <hr size="1" color="#000000" width="100%">
            <div align="center">
              <font size="2"> Av. Rovisco Pais, 1 1049-001 Lisboa Codex Telefone: 218417336 Fax: 218419531 Contribuinte N�: 501507930</font>
            </div>
          </td>
          </tr>
        </table>
     </td>	 
	 </tr>
	</table>

    </td>
    </tr>
    </table>
    
    </body>
</html>
