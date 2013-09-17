<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/chooseDegreeAndYearContext.do?method=choose" focus="executionDegree">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
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
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegree" property="executionDegree" size="1">
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
	     <td nowrap class="formTD">
	     	<bean:message key="property.context.curricular.year"/>:
	     </td>
	     <td nowrap class="formTD">
	     	<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear" size="1">
       			<html:options collection="<%= PresentationConstants.LABELLIST_CURRICULAR_YEARS %>" property="value" labelProperty="label"/>
       	 	</html:select>
       	 </td>
       </tr>
	   </table>
	   <br />
	   <p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
             <bean:message key="label.next"/>
       </html:submit></p>
</html:form>
