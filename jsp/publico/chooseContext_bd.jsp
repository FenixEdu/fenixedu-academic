<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

        <span class="error"><html:errors/></span>
		<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
        <html:form action="<%=path%>">
        	<input type="hidden" value="nextPage" name="method"/>
        	<html:hidden property="page" value="1"/>
         <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoop">Por favor, proceda &agrave; escolha
              da licenciatura pretendida.</td>
          </tr>
        </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
		<html:select property="degreeInitials" size="1">
			<option value="">[Escolha a licenciatura]</option>
       		<html:options collection="<%= SessionConstants.INFO_DEGREE_LIST_KEY %>" property="sigla" labelProperty="nome"/>
       </html:select>
	 </p>
	 <br />
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoop"><bean:message key="label.chooseYearAndSemester" /></td>
          </tr>
    </table>
	 <br />
	 <br />   
	 <table border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td nowrap class="formTD"><bean:message key="property.context.curricular.year"/>:</td>
	     <td nowrap class="formTD"><html:select property="curricularYear" size="1">
	     	<option value="">[Escolha o ano curricular]</option>	     
       		<html:options collection="<%= SessionConstants.CURRICULAR_YEAR_LIST_KEY %>" property="value" labelProperty="label"/>
       </html:select></td>
       </tr>
	   <tr>
	     <td nowrap class="formTD"><bean:message key="property.context.semester"/>:</td>
	     <td nowrap class="formTD">
	     <html:select property="semester" size="1">
	     	<option value="">[Escolha o semestre]</option>
            <html:options collection="<%= SessionConstants.SEMESTER_LIST_KEY %>" property="value" labelProperty="label"/>
       </html:select></td>
       </tr>
	   </table>
	   <br />
	   <p><html:submit value="Submeter" styleClass="inputbutton">
             <bean:message key="label.next"/>
       </html:submit></p>
</html:form>