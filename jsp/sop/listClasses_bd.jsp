<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>A licenciatura seleccionada &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
    </table>
    <br />
   	<table cellpadding="0" border="0">
	 	<tr>   		
		 <td nowrap="nowrap">
    	   <h2>Listagem de Turmas</h2>
    	    <br/>		
   		 </td>
   	     <td nowrap="nowrap" class="formTD">
	    	<logic:present name="classesList" scope="request">
	 	<tr>
	 <!-- Table headers: Nome e Apagar -->
			<th>
				<bean:message key="label.name"/>
			</th>
			<th>
				<bean:message key="label.delete"/>
			</th>				
		</tr>		

		  <bean:define id="deleteConfirm">
			return confirm('<bean:message key="message.confirm.delete.class"/>')
		  </bean:define>
			<logic:iterate id="classView" name="classesList" scope="request">
		<tr>
		  <td nowrap="nowrap" class="listClasses">
			  <div align="center">
			    <html:link paramId="className" paramName="classView" paramProperty="nome" href="ClassManagerDA.do?method=viewClass">
			    </html:link>
		    </div>
		    <html:link paramId="className" paramName="classView" paramProperty="nome" href="ClassManagerDA.do?method=viewClass">
			  <div align="center">
			    <jsp:getProperty name="classView" property="nome" />
			    </div>
		    </html:link>
		  </td>
		  

		  
		  <td nowrap="nowrap" class="listClasses">
			<div align="center">
			  <html:link paramId="className" paramName="classView" paramProperty="nome" href="ClassManagerDA.do?method=deleteClass" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
			    <bean:message key="label.delete"/>
			  </html:link>
		      </div></td>
		</tr>
			</logic:iterate>
			</logic:present>
			<logic:notPresent name="classesList" scope="request">
	    <tr>
	       <td class="formTD">
	         <span class="error"><bean:message key="listClasses.emptyClasses"/></span>
	       </td>
	    </tr>		      
			</logic:notPresent>
		   </td>
		</tr>
</table>
