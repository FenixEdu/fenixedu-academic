<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.Collection" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoGuideSituation" %>
<%@ page import="DataBeans.InfoGuide" %>
<%@ page import="Util.State" %>
<%@ page import="Util.SituationOfGuide" %>
<%@ page import="ServidorAplicacao.Servico.UserView" %>


  <html:form action="/editGuideSituation.do?method=editGuideSituation">
   	  <html:hidden property="page" value="1"/>  
   <table>
   		<bean:define id="infoGuide" name="<%= SessionConstants.GUIDE %>" scope="request"/>  		
   		<bean:define id="guideSituation" name="infoGuide" property="infoGuideSituation"/>  		
   		<bean:define id="days" name="<%= SessionConstants.MONTH_DAYS_KEY %>" scope="request"/>
   		<bean:define id="months" name="<%= SessionConstants.MONTH_LIST_KEY %>" scope="request"/>
   		<bean:define id="years" name="<%= SessionConstants.YEARS_KEY %>" scope="request"/>

      <span class="error"><html:errors/></span>


       <tr>
		<td><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
        <td><bean:write name="guideSituation" property="remarks"/></td>
       </tr>
       <tr>
		<td><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
        <td><bean:write name="guideSituation" property="situation"/></td>
       </tr>
       <tr>
		<td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
        <td><bean:write name="guideSituation" property="date"/></td>
       </tr>
       <% if (((InfoGuideSituation) guideSituation).getSituation().equals(SituationOfGuide.PAYED_TYPE)) { %>
       		<tr>
    			<td><bean:message key="label.masterDegree.administrativeOffice.payment" /></td>
    			<td><bean:write name="infoGuide" property="paymentType"/></td>
    		</tr>
     	<% } %>

	<table>
	
	<br>
	<br>
	
	</table>

	   <!-- Guide Situation -->
       <tr>
        <td><bean:message key="label.masterDegree.administrativeOffice.remarks"/> </td>
       	<td><html:textarea property="remarks"/></td>
        <td><bean:message key="label.masterDegree.administrativeOffice.newGuideSituation" />
            <html:select property="guideSituation">
           		<html:options collection="<%= SessionConstants.GUIDE_SITUATION_LIST %>" property="value" labelProperty="label" />
            </html:select>          
       	</td>
       </tr>


        <!-- Payment Date -->
        <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.paymentDate" /></td>
          <td><html:select property="paymentDateYear">
                <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="paymentDateMonth">
                <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="paymentDateDay">
                <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>
        
   </table>

    <bean:message key="label.masterDegree.administrativeOffice.payment" />
    <html:select property="paymentType">
    	<html:options collection="<%= SessionConstants.PAYMENT_TYPE %>" property="value" labelProperty="label" />
    </html:select>     

   
	<bean:message key="label.masterDegree.administrativeOffice.payment"/><br>


   <html:submit property="Alterar">Alterar Situação</html:submit>
   
   <html:reset property="Reset">Dados Originais</html:reset>
   
</html:form>     

      
      