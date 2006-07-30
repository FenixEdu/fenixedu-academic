<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado &eacute;:</p>
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
			<th class="listClasses-header">
				<bean:message key="label.name"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.delete"/>
			</th>				
		</tr>		
		  <bean:define id="deleteConfirm">
			return confirm('<bean:message key="message.confirm.delete.class"/>')
		  </bean:define>
			<logic:iterate id="classView" name="classesList" scope="request">
			<bean:define id="classOID"
						 type="java.lang.Integer"
						 name="classView"
						 property="idInternal"/>
		<tr>
		  <td nowrap="nowrap" class="listClasses">
		   		<html:link page="<%= "/ClassManagerDA.do?method=viewClass&amp;"
					+ SessionConstants.CLASS_VIEW_OID
				  	+ "="
				  	+ pageContext.findAttribute("classOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_PERIOD_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionPeriodOID")
				  	+ "&amp;"
				  	+ SessionConstants.CURRICULAR_YEAR_OID
					+ "="
				  	+ pageContext.findAttribute("curricularYearOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_DEGREE_OID
				  	+ "="
					+ pageContext.findAttribute("executionDegreeOID") %>">
			  <div align="center">
			    <jsp:getProperty name="classView" property="nome" />
			    </div>
		    </html:link>
		  </td>
		  <td nowrap="nowrap" class="listClasses">
			<div align="center">

		   		<html:link page="<%= "/ClassManagerDA.do?method=deleteClass&amp;"
					+ SessionConstants.CLASS_VIEW_OID
				  	+ "="
				  	+ pageContext.findAttribute("classOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_PERIOD_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionPeriodOID")
				  	+ "&amp;"
				  	+ SessionConstants.CURRICULAR_YEAR_OID
					+ "="
				  	+ pageContext.findAttribute("curricularYearOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_DEGREE_OID
				  	+ "="
					+ pageContext.findAttribute("executionDegreeOID") %>"
					onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
			    <bean:message key="label.delete"/>
			  </html:link>
		      </div></td>
		</tr>
			</logic:iterate>
			</logic:present>
			<logic:notPresent name="classesList" scope="request">
	    <tr>
	       <td class="formTD">
	         <span class="error"><!-- Error messages go here --><bean:message key="listClasses.emptyClasses"/></span>
	       </td>
	    </tr>		      
			</logic:notPresent>
		   </td>
		</tr>
</table>
