<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<br />

<br />
<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="component" property="infoSite.idInternal"/>
Mostrar Sumários:
<html:link page="<%= "/viewSite.do" + "?method=summaries&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
							<bean:message key="label.summaries.all"/>
</html:link>
|
<html:link page="<%= "/viewSite.do" + "?method=summaries&amp;typeFilter=T&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
							<bean:message key="label.summaries.theo"/>
</html:link>
|
<html:link page="<%= "/viewSite.do" + "?method=summaries&amp;typeFilter=P&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
							<bean:message key="label.summaries.prat"/>
</html:link>
|
<html:link page="<%= "/viewSite.do" + "?method=summaries&amp;typeFilter=TP&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
							<bean:message key="label.summaries.theoPrat"/>
</html:link>
|
<html:link page="<%= "/viewSite.do" + "?method=summaries&amp;typeFilter=L&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
							<bean:message key="label.summaries.lab"/>
</html:link>
<br/>
<br/>

<table width="100%">	
 <logic:present  name="component" property="summaryType">
      <tr>
      	<td>
      		<h3><bean:message key="label.summaries.lesson.type"/>
      		<bean:write name="component" property="summaryType.fullNameTipoAula"/></h3>
      	</td>
      </tr>
  </logic:present>
  <logic:notPresent  name="component" property="summaryType">
      <tr>
      	<td>
      		<h3><bean:message key="label.summaries.lesson.allType"/>
      		</h3>
      	</td>
      </tr>
  </logic:notPresent>
<logic:empty name="component" property="infoSummaries" >
	<tr>
		<td><bean:message key="label.summaries.not.found"/>
		</td>
	</tr>
</logic:empty>

<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
	
 <tr>
         <td>			
		<strong>	<bean:message key="label.summary.lesson"/>
			<bean:write name="summary" property="summaryTypeFormatted"/></strong>
            <div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            <bean:write name="summary" property="summaryHourFormatted"/>)</div> 
          </td>
      </tr>

 <tr>
         <td>
             <strong><bean:write name="summary" property="title"/></strong>
         </td>
     </tr>
    
	  	 <tr>
         <td>
             <bean:write name="summary" property="summaryText" filter="false"/>
         </td>
     </tr>
<tr>
                   <td>
						<span class="px9"><bean:message key="label.lastModificationDate" /> <bean:write name="summary" property="lastModifiedDateFormatted"/> </span>
                    </td>
                </tr>
            <tr><td>&nbsp;</td></tr>    
                

</logic:iterate>
</table>
</logic:present>