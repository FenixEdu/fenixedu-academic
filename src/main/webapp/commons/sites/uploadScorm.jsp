<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="siteId" name="site" property="idInternal"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="container" name="bean" property="fileHolder"/>
<bean:define id="containerId" name="container" property="idInternal"/>

<bean:define id="topContainerString" value="<%= "sectionID=" %>"/>
<bean:define id="selectContainerString" value="<%= "itemID=" + containerId %>"/>

<logic:equal name="container" property="class.simpleName" value="Section">
	<bean:define id="selectContainerString" value="<%= "sectionID=" + containerId %>"/>
	<bean:define id="topContainerString" value="<%= "sectionID=" + containerId %>"/>
</logic:equal>
 <logic:equal name="container" property="class.simpleName" value="Item">
	<bean:define id="sectionID" name="container" property="section.idInternal"/>
	<bean:define id="topContainerString" value="<%= "sectionID=" + sectionID%>"/> 
 </logic:equal>


<h2>
	<fr:view name="container" property="name" />
</h2>

<h3>
    <bean:message key="link.scorm.uploadScormFile"  bundle="SITE_RESOURCES"/>
</h3>

<ul>
<li>
<html:link page="<%= actionName + "?method=section&amp;" + topContainerString + "&amp;" + context %>">
<bean:message key="link.goBack"/>
</html:link>
</li>
</ul>

<logic:equal name="site" property="scormContentAccepted" value="true">
    <p>
    <span>
    	<span class="pleft1">
    		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
    		<html:link page="<%= actionName + "?method=uploadFile&amp;" + selectContainerString + "&amp;" + context %>">			
    			<bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
    		</html:link>
    	</span>
        	<span class="pleft1">
        		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        		<bean:message key="link.scorm.uploadScormFile" bundle="SITE_RESOURCES"/>
        	</span>
        	<span class="pleft1">
        		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        		<html:link page="<%= actionName + "?method=prepareCreateScormFile&amp;" + selectContainerString + "&amp;" + context %>">			
        			<bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
        		</html:link>
        	</span>
    </span>
    </p>
</logic:equal>

<div class="infoop2">
<p><bean:message key="label.scorm.description" bundle="SITE_RESOURCES"/></p>
</div>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="SITE_RESOURCES">
		<p class="mvert15">
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>


<div class="dinline forminline">
<fr:form action="<%= actionName + "?method=scormFileUpload&amp;" + context + "&amp;" + selectContainerString %>" encoding="multipart/form-data">
    <fr:edit id="creator" name="bean" visible="false"/>
<table class="tstyle5 thright thlight mtop025">
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName"/>:</th>
		<td>
	        <fr:edit name="bean" slot="displayName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.AuthorsName"/>:</th>
		<td>
	        <fr:edit name="bean" slot="authorsName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
    <logic:notPresent name="skipFileClassification">
        	<tr>
        		<th><bean:message key="label.teacher.siteAdministration.uploadFile.ResourceType"/>:</th>
        		<td>
        	        <fr:edit name="bean" slot="educationalLearningResourceType">
        	        <fr:layout>
              				<fr:property name="excludedValues" value="PROJECT_SUBMISSION,SITE_CONTENT"/>
        	        </fr:layout>
        	        </fr:edit>
        		</td>
        	</tr>
    </logic:notPresent>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.file"/>:</th>
		<td>
	    <fr:edit name="bean" slot="file">
	        <fr:layout>
	            <fr:property name="size" value="40"/>
	            <fr:property name="fileNameSlot" value="fileName"/>
	            <fr:property name="fileSizeSlot" value="fileSize"/>
	        </fr:layout>
	    </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.permissions"/>:</th>
		<td>
	        <fr:edit name="bean" schema="item.file.create" layout="flow">
	            <fr:layout>
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
</table>


        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton dinline">
            <bean:message key="button.save"/>
        </html:submit>
	</fr:form>
</div>

<div class="dinline forminline">
	<fr:form action="<%=String.format("%s?method=section&amp;%s&amp;%s", actionName, context, topContainerString) %>" encoding="multipart/form-data">
		<html:cancel styleClass="inputbutton dinline">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</fr:form>
</div>
