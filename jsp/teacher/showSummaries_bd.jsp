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

<logic:notEqual name="executionCourse" property="theoreticalHours" value="0">
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=T" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="label.summaries.theo"/>
</html:link>
</logic:notEqual>

<logic:notEqual name="executionCourse" property="praticalHours" value="0">
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=P" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="label.summaries.prat"/>
</html:link>
</logic:notEqual>

<logic:notEqual name="executionCourse" property="theoPratHours" value="0">
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=TP" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="label.summaries.theoPrat"/>
</html:link>
</logic:notEqual>

<logic:notEqual name="executionCourse" property="labHours" value="0">
|
<html:link page="/summariesManager.do?method=showSummaries&typeFilter=L" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="label.summaries.lab"/>
</html:link>
</logic:notEqual>
<br/>
<br/>

<table width="100%">
	<tr>
    	<td>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
					<bean:message key="label.insertSummary" />
				</html:link>
			</div>
			<br />
			<br />
   		</td>
	</tr>

<logic:notPresent name="component" property="summaryType">
<logic:notEqual name="executionCourse" property="theoreticalHours" value="0">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.theo"/></strong>
      	</td>
	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="T">
<bean:define id="hasT" value="whatever"/>
	<tr>
    	<td>
        	<strong><bean:write name="summary" property="title"/></strong>
        </td>
	</tr>
	<tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
    	</td>
	</tr>      
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasT">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:notEqual>

<logic:notEqual name="executionCourse" property="praticalHours" value="0">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.prat"/></strong>
      	</td>
	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="P">
<bean:define id="hasP" value="whatever"/>
	<tr>
		<td>
    		<strong><bean:write name="summary" property="title"/></strong>
 	   </td>
   	</tr>
    <tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
    	</td>
	</tr>      
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasP">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:notEqual>

<logic:notEqual name="executionCourse" property="theoPratHours" value="0">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.theoPrat"/></strong>
      	</td>
 	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="TP">
<bean:define id="hasTP" value="whatever"/>
 	<tr>
    	<td>
        	<strong><bean:write name="summary" property="title"/></strong>
        </td>
 	</tr>
    <tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
     	</td>
    </tr>      
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasTP">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:notEqual>

<logic:notEqual name="executionCourse" property="labHours" value="0">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.lab"/></strong>
      	</td>
  	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="L">
<bean:define id="hasL" value="whatever"/>
	<tr>
    	<td>
        	<strong><bean:write name="summary" property="title"/></strong>
        </td>
  	</tr>
    <tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
		</td>
	</tr>     
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasL">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:notEqual>
</logic:notPresent>



<logic:present name="component" property="summaryType">
<logic:equal name="component" property="summaryType.siglaTipoAula" value="T">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.theo"/></strong>
     	</td>
  	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="T">
<bean:define id="hasT" value="whatever"/>
	<tr>
    	<td>
        	<strong><bean:write name="summary" property="title"/></strong>
      	</td>
  	</tr>
    <tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
     	</td>
   	</tr>     
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasT">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:equal>

<logic:equal name="component" property="summaryType.siglaTipoAula" value="P">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.prat"/></strong>
      	</td>
  	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="P">
<bean:define id="hasP" value="whatever"/>
	<tr>
    	<td>
        	<strong><bean:write name="summary" property="title"/></strong>
      	</td>
 	</tr>
   	<tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
   		</td>
  	</tr>     
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasP">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:equal>

<logic:equal name="component" property="summaryType.siglaTipoAula" value="TP">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.theoPrat"/></strong>
      	</td>
  	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="TP">
<bean:define id="hasTP" value="whatever"/>
	<tr>
    	<td>
        	<strong><bean:write name="summary" property="title"/></strong>
      	</td>
  	</tr>
    <tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
       	</td>
  	</tr>         
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasTP">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:equal>

<logic:equal name="component" property="summaryType.siglaTipoAula" value="L">
	<tr>
    	<td class="infoop">
      		<strong><bean:message key="label.summaries.lab"/></strong>
      	</td>
  	</tr>
<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
<logic:equal name="summary" property="summaryType.siglaTipoAula" value="L">
<bean:define id="hasL" value="whatever"/>
	<tr>
    	<td>
        	<strong><bean:write name="summary" property="title"/></strong>
      	</td>
  	</tr>
    <tr>
    	<td>
        	<div class="greytxt">(<bean:write name="summary" property="summaryDateFormatted"/>
            	<bean:write name="summary" property="summaryHourFormatted"/>)
            </div> 
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
    <tr>
    	<td>
			<div class="gen-button">
				<bean:define id="summaryCode" name="summary" property="idInternal" />
				<html:link page="<%= "/summariesManager1.do?method=prepareEditSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
					<bean:message key="button.edit" /> 
				</html:link>
			</div>
			<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
    	    <br />
    	    <br />
      	</td>
	</tr>          
</logic:equal>
</logic:iterate>
<logic:notPresent name="hasL">
<tr><td><bean:message key="message.summaries.not.found"/></td></tr>
</logic:notPresent>
</logic:equal>
</logic:present>
      
   
</table>
</logic:present>