<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

   <span class="error"><html:errors/></span>

    <bean:define id="title" name="<%= SessionConstants.ACTION %>" scope="session" />
    <h2><bean:message name="title"/></h2>
    
    

   <table>

    <html:form action="/chooseGuideDispatchAction?method=choose">
   	  <html:hidden property="page" value="1"/>
       <!-- Guide Year -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideYear"/> </td>
         <td><html:text property="guideYear"/></td>
         </td>
       </tr>

       <!-- Guide Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideNumber"/> </td>
         <td><html:text property="guideNumber"/></td>
         </td>
       </tr>


      </table>

     <html:submit value="Seguinte" styleClass="button" property="ok"/>
     <html:reset value="Limpar" styleClass="button"/>

    </html:form>
	</logic:present>
