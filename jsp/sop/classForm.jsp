<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/ClassManagerDA" >

<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<span class="error"><html:errors/></span>	
	<logic:notPresent name="<%= SessionConstants.CLASS_VIEW %>" scope="request">
		<input type="hidden" name="method" value="createClass">
	</logic:notPresent>
	<logic:present name="<%= SessionConstants.CLASS_VIEW %>" scope="request">
		<input type="hidden" name="method" value="editClass"/>
		<input type="hidden" name="change" value= "1"/>
		<table width="100%" cellspacing="0">
		  <tr>
		   	<td nowrap="nowrap" class="infoop">
		   		<html:link page="<%= "/ClassShiftManagerDA.do?method=viewClassShiftList&amp;"
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
		   			<bean:message key="label.add.shifts"/>
		   		</html:link>
		   	<td/>
		  </tr>
		  <tr>
		   	<td nowrap="nowrap">
				<br />		   	
				<bean:message key="message.class.changeName"/>
		   	<td/>
		  </tr>
		</table>
	</logic:present>
	<html:hidden property="page" value="1"/>
	<table cellspacing="0">
	  <tr>
	    <td nowrap="nowrap" class="formTD">
	    	<br />
		    <bean:message key="property.class.nameShift" />
		</td>
	    <td nowrap="nowrap">
	    	<br />
	    	<html:text property="className" value=""/> <html:submit styleClass="inputbuttonSmall"><bean:message key="label.ok"/></html:submit>
	    </td>
	  </tr>
	</table>
</html:form>