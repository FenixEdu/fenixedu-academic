<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<br />
<table width="100%">
<tr>
<td class="infoop"><bean:message key="label.summary.explanation" /></td>
</tr>
</table>
<br />
<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<table>
	<tr>
         <td>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
							<bean:message key="label.insertSummary" />
						</html:link></div>
					<br />
					<br />
                </td>
      </tr>

<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
	 <tr>
         <td>
             <strong><bean:write name="summary" property="title"/></strong>
         </td>
     </tr>
     <tr>
         <td>			
			<bean:message key="label.summary.lesson"/>
			<bean:write name="summary" property="summaryTypeFormatted"/>
            <bean:write name="summary" property="summaryDateFormatted"/>
            <bean:write name="summary" property="summaryHourFormatted"/>
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
                
                <tr>
                    <td>
						<div class="gen-button">
							<bean:define id="summaryCode" name="summary" property="idInternal" />
							<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
								<bean:message key="button.edit" /> 
							</html:link></div>
						<div class="gen-button">
							<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
								<bean:message key="button.delete" />
							</html:link></div>
    	                <br />
    	                <br />
                    </td>
                </tr>

</logic:iterate>
</table>
</logic:present>