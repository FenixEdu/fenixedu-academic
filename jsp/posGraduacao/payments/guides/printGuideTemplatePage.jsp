<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
    	<title><bean:message key="title.masterDegree.administrativeOffice.printGuide"/></title>
    </head>

    <body>
     
    <table width="100%" height="100%" border="0">
    <tr height="30"><td>
     <table width="100%" border="0" valign="top">
      <tr> 
        <td height="100" colspan="2">
          <table border="0" width="100%" height="104" align="center" cellpadding="0" cellspacing="0">
            <tr> 
              <td width="50" height="100">
               <img src="<%= request.getContextPath() %>/images/LogoIST.gif" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr align="left"> 
                    <td>&nbsp;<b>INSTITUTO SUPERIOR TÉCNICO</b><br/>
                      &nbsp;<b>Secretaria da Pós-Graduação</b><br/>
                      &nbsp;<b>Centro de Custo 0212</b>
                      <hr size="1">
                    </td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> <b>Guia de Pagamento</b>
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

	<table width="100%" border="0">
	 <tr>
	 <td>
      <table width="100%" border="0">
          <tr>
            <td width="20%"><strong>Processo de:</strong></td>
            <td width="80%">&nbsp;</td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.payments.name" /> </td>
            <td> <bean:write name="paymentsManagementDTO" property="person.name"/> </td>
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

        	<logic:iterate id="entryDTO" name="paymentsManagementDTO" property="entryDTOs" >
        		<tr>
        			<td>
        				<bean:message name="entryDTO" property="entryType.name" bundle="ENUMERATION_RESOURCES" />
        				&nbsp;<bean:write name="entryDTO" property="description"/>
        			</td>
        			<td>.........................................</td> &nbsp;
        			<td> <bean:define id="amountToPay" name="entryDTO" property="amountToPay" type="java.math.BigDecimal" /> <%= amountToPay.toPlainString() %> &nbsp;<bean:message key="label.currencySymbol"/></td>
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
   			<td><strong><bean:define id="totalAmountToPay" name="paymentsManagementDTO" property="totalAmountToPay" type="java.math.BigDecimal"/><%= totalAmountToPay.toPlainString() %>&nbsp;<bean:message key="label.currencySymbol"/></strong></td>
    	</tr>
	   </table>
	 </td>
	 </tr>	
	 <tr>
	 <td>&nbsp;
	 </td>
	 </tr>
	 <tr>
	 <td>&nbsp;
	 </td>
	 </tr>
	 <tr valign="bottom">
	 <td>
     <table valign="bottom" width="100%" border="0">
       <tr>
         <td>
         	<%= "Lisboa, " + new java.text.SimpleDateFormat("dd MMMM yyyy", request.getLocale()).format(new java.util.Date()) %>
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
		 <jsp:include page="./commons/footer.jsp" flush="true" />
    </td>
    </tr>
    </table>
	
    </body>
</html>