<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
        <h2><bean:message key="title.manage.schedule"/></h2>
        <span class="error"><!-- Error messages go here --><html:errors /></span>
        <html:form action="/escolherContextoForm">

		<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

        <table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoop">Por favor, proceda &agrave; escolha
              do curso pretendido.</td>
          </tr>
        </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
       		<html:options collection="licenciaturas" property="value" labelProperty="label"/>
       </html:select>
	 </p>
	 <br />
    <table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoop"><bean:message key="label.chooseYear" /></td>
          </tr>
    </table>
	 <br />
	 <br />   
	 <table border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td nowrap class="formTD"><bean:message key="property.context.curricular.year"/>:</td>
	     <td nowrap class="formTD"><html:select bundle="HTMLALT_RESOURCES" altKey="select.anoCurricular" property="anoCurricular" size="1">
       		<html:options collection="anosCurriculares" property="value" labelProperty="label"/>
       </html:select></td>
       </tr>
<%--	   <tr>
	     <td nowrap class="formTD"><bean:message key="property.context.semester"/>:</td>
	     <td nowrap class="formTD"><html:select bundle="HTMLALT_RESOURCES" altKey="select.semestre" property="semestre" size="1">
            <html:options collection="semestres" property="value" labelProperty="label"/>
       </html:select></td>
       </tr> --%>
	   </table>
	   <br />
	   <p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
             <bean:message key="label.next"/>
       </html:submit></p>
</html:form>