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
Mostrar Sumários:
<html:link page="/summariesManager.do?method=showSummaries" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="label.summaries.all"/>
</html:link>
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=T" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="label.summaries.theo"/>
</html:link>
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=P" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="label.summaries.prat"/>
</html:link>
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=TP" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="label.summaries.theoPrat"/>
</html:link>
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=L" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="label.summaries.lab"/>
</html:link>
<br/>
<br/>
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
     <logic:present  name="component" property="summaryType">
      <tr>
      	<td><h3><bean:message key="label.summaries.lesson.type"/><bean:write name="component" property="summaryType.fullNameTipoAula"/></h3>
      	
      	</td>
      </tr>
      </logic:present>
<logic:empty name="component" property="infoSummaries" >
	<tr>
		<td><bean:message key="label.summaries.not.found"/>
		</td>
	</tr>
</logic:empty>
<logic:notEmpty name="component" property="infoSummaries" >
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
	 
	  <tr>
         <td>			
			<strong><bean:message key="label.summary.lesson"/>
			<bean:write name="summary" property="summaryTypeFormatted"/><div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            <bean:write name="summary" property="summaryHourFormatted"/>)</div></strong>
            
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
                
                <tr>
                    <td>
						<div class="gen-button">
							<bean:define id="summaryCode" name="summary" property="idInternal" />
							<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
								<bean:message key="button.edit" /> 
							</html:link></div>
						<div class="gen-button">
							<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
								<bean:message key="button.delete" />
							</html:link></div>
    	                <br />
    	                <br />
                    </td>
                </tr>

</logic:iterate>
</logic:notEmpty>
</table>
</logic:present>