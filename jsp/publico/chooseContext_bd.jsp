<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
        <span class="error"><html:errors/></span>
		<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
        <html:form action="<%=path%>" method="GET">
        	<input type="hidden" value="nextPagePublic" name="method"/>
        	<html:hidden property="page" value="1"/>
        	<html:hidden  property="ePName" value="<%= pageContext.findAttribute("ePName").toString() %>" />
			<html:hidden  property="eYName" value="<%= pageContext.findAttribute("eYName").toString() %>" /> 
         <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.public.degree.choose"/></td>
          </tr>
        </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
		<html:select property="index" size="1">
       		<html:options collection="degreeList" property="value" labelProperty="label"/>
       </html:select>
	 </p>
	 <br />
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="label.chooseYear" /></td>
          </tr>
    </table>
	 <br />
	 <br />   
	 <table border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td nowrap class="formTD"><bean:message key="property.context.curricular.year"/>:</td>
	     <td nowrap class="formTD"><html:select property="curYear" size="1">
       		<html:options collection="curricularYearList" property="value" labelProperty="label"/>
       </html:select></td>
       </tr>
<%--	   <tr>
	     <td nowrap class="formTD"><bean:message key="property.context.semester"/>:</td>
	     <td nowrap class="formTD"><html:select property="semestre" size="1">
            <html:options collection="semestres" property="value" labelProperty="label"/>
       </html:select></td>
       </tr> --%>
	   </table>
	   <br />
	   <html:submit value="Submeter" styleClass="inputbutton">
             <bean:message key="label.next"/>
       </html:submit>
</html:form>
