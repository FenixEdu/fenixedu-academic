<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.GuideState" %>
<%@ page import="net.sourceforge.fenixedu.domain.DocumentType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry" %>

   	  <bean:define id="infoGuide" name="<%= SessionConstants.GUIDE %>" scope="session" type="net.sourceforge.fenixedu.dataTransferObject.InfoGuide"/>  		
      <bean:define id="number" name="infoGuide" property="number" />
      <bean:define id="year" name="infoGuide" property="year" />
      <bean:define id="version" name="infoGuide" property="version" />

	 <strong>
	 <bean:message key="label.masterDegree.administrativeOffice.guideInformation" 
				   arg0='<%= pageContext.findAttribute("version").toString() %>'
				   arg1='<%= pageContext.findAttribute("number").toString() %>' 
				   arg2='<%= pageContext.findAttribute("year").toString() %>' 
	  />
	 </strong>
  	 <br>
  	 <br>
      <span class="error"><html:errors/></span>
      <br>
  	 <br>

     <table>
          <tr>
            <td><bean:message key="label.person.name" /></td>
            <td><bean:write name="infoGuide" property="infoPerson.nome"/></td>
          </tr>

          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="infoGuide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>
     </table>
     
     <br>
     <br>
     
     <table>
          <tr> 
            <td><strong>Entidade Pagadora:</strong></td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorNumber"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorName"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorAddress"/></td>
          </tr>
	</table>

	<br>
	
	<bean:define id="link">/editGuideInformation.do?method=editGuideInformation<%= "&" 
%>page=0<%= "&" 
%>year=<bean:write name="year"/><%= "&" 
%>number=<bean:write name="number"/><%= "&" 
%>version=<bean:write name="version"/>
    </bean:define>

    <html:form action='<%= pageContext.findAttribute("link").toString() %>'>
   	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/> 
	<table>
      <tr> 
        <td><bean:message key="label.masterDegree.administrativeOffice.newContributor"/></td>
		<td>	
         <html:select bundle="HTMLALT_RESOURCES" altKey="select.contributor" property="contributor">
           <option value="" selected="selected"><bean:message key="label.masterDegree.administrativeOffice.contributor.default"/></option>
           <html:options collection="<%= SessionConstants.CONTRIBUTOR_LIST %>" property="value" labelProperty="label"/>
    	 </html:select>        
        </td>
      </tr> 
	</table>
	
	<br>
	<br>
	
 	 <table>
		<tr align="center">
			<td><bean:message key="label.masterDegree.administrativeOffice.documentType" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.description" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.quantity" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.price" /></td>
		</tr>
		
		<%
			if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.PAYED))
			{
		
%>
				<logic:iterate id="guideEntry" name="infoGuide" property="infoGuideEntries" indexId="position">
           	  		<bean:define id="entryQuantity" name="guideEntry" property="quantity" />  		
        
           			<tr>
            			<td>
            				<bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
							<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />
            			</td>
            			<td><bean:write name="guideEntry" property="description"/></td>
            			<input alt="<%= new String("quantityList" + pageContext.findAttribute("position").toString()) %>" type="hidden" name="<%= new String("quantityList" + pageContext.findAttribute("position").toString()) %>" value='<%= pageContext.findAttribute("entryQuantity").toString() %>' >
            			<td><bean:write name="entryQuantity" /></td>
            			<td align="right"><bean:write name="guideEntry" property="price"/> <bean:message key="label.currencySymbol" /></td>
		   			</tr>
        		</logic:iterate>

				<tr>
					<td><html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.othersRemarks" property="othersRemarks"/></td>
					<td><html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.othersQuantity" property="othersQuantity"/></td>
					<td><html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.othersPrice" property="othersPrice"/></td>
				</tr>
		
		<%
			}
			else
			{
		%>
        		<logic:iterate id="guideEntry" name="infoGuide" property="infoGuideEntries" indexId="position">
           	  		<bean:define id="entryQuantity" name="guideEntry" property="quantity" />  		
        
           			<tr>
            			<td>
            				<bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
							<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />
            			</td>
            			<td><bean:write name="guideEntry" property="description"/></td>
						
						<% if(((InfoGuideEntry)guideEntry).getDocumentType().equals(DocumentType.GRATUITY) || ((InfoGuideEntry)guideEntry).getDocumentType().equals(DocumentType.INSURANCE)){ %>
							
							<input alt="<%= new String("quantityList" + pageContext.findAttribute("position").toString()) %>" type="hidden" name="<%= new String("quantityList" + pageContext.findAttribute("position").toString()) %>" value='<%= pageContext.findAttribute("entryQuantity").toString() %>' >
							<td><bean:write name="entryQuantity" /></td>
            			
						<%	}else{ %>
						
							<td><input alt="<%= new String("quantityList" + pageContext.findAttribute("position").toString()) %>" type="text" name="<%= new String("quantityList" + pageContext.findAttribute("position").toString()) %>" value='<%= pageContext.findAttribute("entryQuantity").toString() %>' ></td>
						
						<% } %>
													
						<td align="right"><bean:write name="guideEntry" property="price"/> <bean:message key="label.currencySymbol" /></td>
		   			</tr>
        		</logic:iterate>

				<tr>
					<td><bean:message key="label.masterDegree.administrativeOffice.others" /></td>
					<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.othersRemarks" property="othersRemarks"/></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.othersQuantity" property="othersQuantity"/></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.othersPrice" property="othersPrice"/> <bean:message key="label.currencySymbol" /></td>
				</tr>
         <%
         	}
         %>
            <tr>
         		<td></td>
         		<td></td>
         		<td><bean:message key="label.masterDegree.administrativeOffice.guideTotal" />:</td>
         		<td align="right"><bean:write name="infoGuide" property="total"/> <bean:message key="label.currencySymbol" /></td>
         	</tr>
     </table>
     
     <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.Alterar" property="Alterar">Alterar Informa��o</html:submit>
     
    </html:form>

