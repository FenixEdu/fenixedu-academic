<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

   <span class="error"><html:errors/></span>
   <table>
    <html:form action="/chooseExecutionYear.do?method=chooseExecutionYear">
	   <html:hidden property="page" value="1"/>
       <bean:define id="executionYearList" name="<%= SessionConstants.EXECUTION_YEAR_LIST %>" scope="request" />

       <!-- ExecutionYear -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.executionYear"/></td>
         <td><html:select property="executionYear">
                <html:options collection="executionYearList" property="value" labelProperty="label" />
             </html:select>
         </td>
       </tr>
    
       <br/>
         <td align="right">
             <html:submit value="Seguinte" styleClass="button" property="ok"/>
         </td>
         </tr>
    </html:form>
   </table>
  </body>
</html>
