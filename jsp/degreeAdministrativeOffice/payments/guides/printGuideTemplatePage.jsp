<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

<html>
    <head>
    	<title><bean:message key="label.payments.printTemplates.guide"/></title>
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
               <img src="<%= request.getContextPath() %>/images/LogoIST.gif" alt="<bean:message key="LogoIST" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr align="left"> 
                    <td>&nbsp;<b><bean:message key="label.payments.printTemplates.institutionName.upper.case"/></b><br/>
                      &nbsp;<b><bean:write name="currentUnit" property="name"/></b><br/>
                      &nbsp;<b><bean:message  key="label.payments.printTemplates.costCenter"/> <bean:write name="currentUnit" property="costCenterCode"/></b>
                      <hr size="1">
                    </td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> <b><bean:message  key="label.payments.printTemplates.guide"/></b>
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
            <td width="20%"><strong><bean:message  key="label.payments.printTemplates.processFrom"/>:</strong></td>
            <td width="80%">&nbsp;</td>
          </tr>
          <tr>
            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.name" bundle="APPLICATION_RESOURCES" /> </td>
            <td> <bean:write name="paymentsManagementDTO" property="person.name"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES" /> </td>
            <td> <bean:message name="paymentsManagementDTO" property="person.idDocumentType.name" bundle="ENUMERATION_RESOURCES"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" bundle="APPLICATION_RESOURCES" /> </td>
            <td> <bean:write name="paymentsManagementDTO" property="person.documentIdNumber"/> </td>
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
        	<logic:iterate id="entryDTO" name="paymentsManagementDTO" property="selectedEntries" >
        		<tr>
        			<td>
        				<app:labelFormatter name="entryDTO" property="description">
        					<app:property name="enum" value="ENUMERATION_RESOURCES"/>
        					<app:property name="application" value="APPLICATION_RESOURCES"/>
							<app:property name="default" value="APPLICATION_RESOURCES"/>	
        				</app:labelFormatter>
        			</td>
        			<td>...................................&nbsp;</td>
        			<td> <bean:define id="amountToPay" name="entryDTO" property="amountToPay" type="java.math.BigDecimal" /> <%= amountToPay.toPlainString() %> &nbsp;<bean:message key="label.currencySymbol"/></td>
        		</tr>
        	</logic:iterate >
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    	<tr>
    	  	<td><strong><bean:message  key="label.payments.printTemplates.totalAmountToPay"/></strong></td>
   			<td>_________________&nbsp;</td>
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
         	<bean:message  key="label.payments.printTemplates.city"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", request.getLocale()).format(new java.util.Date()) %>
         </td>
       </tr>
       
       <tr>
        <td>&nbsp;</td>
         <td colspan="2" valign="bottom">
           &nbsp;<div align="center">&nbsp;</div>
           <div align="center">&nbsp;</div>
           <div align="center"><b><bean:message  key="label.payments.printTemplates.theEmployee"/></b> <br/>
            <br/>
            <br/>
           </div>
          <hr align="center" width="300" size="1">
         </td>
       </tr>

	 </table>
	 </td>
	 </tr>
	 
     <tr>	 
	 <td>
		 <jsp:include page="/degreeAdministrativeOffice/payments/commons/footer.jsp" flush="true" />
    </td>
    </tr>
    </table>
	
    </body>
</html>

</logic:present>
