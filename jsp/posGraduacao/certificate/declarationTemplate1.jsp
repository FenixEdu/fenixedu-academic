<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.CalculateGuideTotal" %>

<%--     <bean:define id="infoStudent" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
--%>

 <b>ALUNO Nº: </b> 
	
<%--
 
 <table width="100%" height="100%" border="0">
 <tr height="30">
 <td>
     <table width="100%" border="0" valign="top">
     
      <tr> 
        <td height="100" colspan="2">
          <table border="0" width="100%" height="104" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                  <td><bean:write key="title.masterDegree.administrativeOffice.printDeclaration"/></td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> <b>ALUNO Nº: </b> 
                     <bean:write name="infoStudent" property="infoStudent.getInfoStudent().getNumber()"/> 
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
    <tr valign="top" >
    <td>

<%--	<table width="100%" border="0">
	 <tr>
	 <td>
      <table width="100%" border="0">
          <tr>
            <td width="20%"><strong>Processo de:</strong></td>
            <td width="80%">&nbsp;</td>
          </tr>
          <logic:present name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE%>">
	          <bean:define id="infoMasterDegreeCandidate" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE%>" scope="session" />    
	          <tr>
	            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
	            <td> <bean:write name="infoMasterDegreeCandidate" property="candidateNumber"/> </td>
	          </tr>
          </logic:present>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
            <td> <bean:write name="guide" property="infoPerson.nome"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="guide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
    
          <tr> 
            <td width="30%"><strong>Entidade Pagadora:</strong> </td>
            <td width="70%" >&nbsp;</td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/></td>
            <td><bean:write name="guide" property="infoContributor.contributorNumber"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/></td>
            <td><bean:write name="guide" property="infoContributor.contributorName"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/></td>
            <td><bean:write name="guide" property="infoContributor.contributorAddress"/></td>
          </tr>
	  </table>
	 </td>
	 </tr>
	 </table>
	 </td>
	 </tr>
	 
	 <tr>
	 <td> 
	   <table align="right">
        	<logic:iterate id="guideEntry" name="guide" property="infoGuideEntries" >
        	  <tr>
    			<td><bean:write name="guideEntry" property="documentType"/>&nbsp;<bean:write name="guideEntry" property="description"/></td>
    			<td>.........................................</td>&nbsp;
                <bean:define id="price" name="guideEntry" property="price" />
                <bean:define id="quantity" name="guideEntry" property="quantity" />
                <td><%= CalculateGuideTotal.formatNumber(new Double(new Double(pageContext.findAttribute("price").toString()).floatValue() *  new Integer(pageContext.findAttribute("quantity").toString()).intValue())) %>&nbsp;<bean:message key="label.currencySymbol"/></td>
        	  </tr>
        	</logic:iterate >
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    	<tr>
    	  	<td><strong>A liquidar a importância de </strong></td>
   			<td>_____________________</td>&nbsp;
   			<td><strong><bean:write name="guide" property="total"/>&nbsp;<bean:message key="label.currencySymbol"/></strong></td>
    	</tr>
	   </table>
	 </td>
	 </tr>
	 
	 <tr>
	 <td>
	 </td>
	 </tr>
	 
	 <tr valign="bottom">
	 <td>
     <table valign="bottom" width="100%" border="0">
       <tr>
         <td>
			<bean:write name="<%= SessionConstants.DATE %>" />			
         </td>
       </tr>
       
       <tr>
        <td>&nbsp;</td>
         <td colspan="2" valign="bottom">
           &nbsp;<div align="center">&nbsp;</div>
           <div align="center">&nbsp;</div>
           <div align="center"><b>O Funcionário</b> <br>
            <br>
            <br>
           </div>
          <hr align="center" width="300" size="1">
         </td>
       </tr>

	 </table>
	 </td>
	 </tr>
	 
     <tr>	 
	 <td>
     <table width="100%" border="0">
      <tr>	 
 	  <td>
	 	<table align="center" width="90%" valign="bottom">
	      <tr>
          <td colspan="2" valign="bottom" >
            <div align="center">
              <font size="2"> Documento processado por computador. Só é válido como recibo após o carimbo de pago e devidamente assinado.</font> 
            </div>
            <hr size="1" color="#000000" width="90%">
            <div align="center">
              <font size="2"> Av. Rovisco Pais, 1 1049-001 Lisboa Codex Telefone: 218417336 Fax: 218419531 Contribuinte Nº: 501507930</font>
            </div>
          </td>
          </tr>
        </table>
     </td>	 
	 </tr>
	</table>
--%>

<%--
    </td>
    </tr>
    </table>
    
	--%>