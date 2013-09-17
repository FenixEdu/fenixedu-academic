<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
        <span class="error"><!-- Error messages go here --><html:errors /></span>
		<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
        <html:form action="<%=path%>">
        	<input alt="input.method" type="hidden" value="nextPage" name="method"/>

		<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

		<logic:present name="examDateAndTime">
			<html:hidden alt="<%= PresentationConstants.EXAM_DATEANDTIME %>" property="<%= PresentationConstants.EXAM_DATEANDTIME %>"
						 value="<%= pageContext.findAttribute("examDateAndTime").toString() %>"/>
		</logic:present>

			<html:hidden alt="<%= PresentationConstants.NEXT_PAGE %>" property="<%= PresentationConstants.NEXT_PAGE %>"
						 value="<%= pageContext.findAttribute("nextPage").toString() %>"/>
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
         <table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoop">Por favor, proceda &agrave; escolha
              do curso pretendido.</td>
          </tr>
        </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
       		<html:options collection="<%= PresentationConstants.DEGREES %>" property="value" labelProperty="label"/>
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
	     <td nowrap class="formTD"><html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear" size="1">
       		<html:options collection="<%= PresentationConstants.CURRICULAR_YEAR_LIST_KEY %>" property="value" labelProperty="label"/>
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
