<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<br />

<br />
<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<table>
	

<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
	
 <tr>
         <td>			
		<strong>	<bean:message key="label.summary.lesson"/>
			<bean:write name="summary" property="summaryTypeFormatted"/>
            <bean:write name="summary" property="summaryDateFormatted"/>
            <bean:write name="summary" property="summaryHourFormatted"/> </strong>
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