<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="section" type="net.sourceforge.fenixedu.domain.Section" name="item" property="section"/>
<bean:define id="item" type="net.sourceforge.fenixedu.domain.Item" name="item"/>

<h2>
	<fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="title.item.file.upload" bundle="SITE_RESOURCES"/>
</h3>

<ul>
<li>
<html:link page="<%= actionName + "?method=section&amp;sectionID=" + section.getIdInternal() + "&amp;" + context %>">
<bean:message key="link.goBack" bundle="SITE_RESOURCES"/>
</html:link>
</li>
</ul>

<logic:equal name="site" property="scormContentAccepted" value="true">
<p>
<span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<bean:message key="title.item.file.upload" bundle="SITE_RESOURCES"/>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= actionName + "?method=prepareUploadScormFile&amp;itemID=" + item.getIdInternal() + "&amp;" + context %>">			
		<bean:message key="link.scorm.uploadScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= actionName + "?method=prepareCreateScormFile&amp;itemID=" + item.getIdInternal() + "&amp;" + context %>">			
			<bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
</span>
</p>
</logic:equal>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<p class="mvert1">
    <span class="error">
        <html:errors property="section" bundle="SITE_RESOURCES"/>
    </span>
</p>

<div class="dinline forminline">
<fr:form action="<%= actionName + "?method=fileUpload&amp;" + context + "&amp;itemID=" + item.getIdInternal()  %>" encoding="multipart/form-data" >
    <fr:edit id="creator" name="fileItemCreator" visible="false">
        	<fr:destination name="cancel" path="<%= String.format("%s?method=section&amp;%s&amp;sectionID=%s", actionName, context, item.getSection().getIdInternal()) %>"/>
    </fr:edit>

<table class="tstyle5 thright thlight mtop025">
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName" bundle="SITE_RESOURCES"/>:</th>
		<td>
	        <fr:edit id="displayName" name="fileItemCreator" slot="displayName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.AuthorsName" bundle="SITE_RESOURCES"/>:</th>
		<td>
	        <fr:edit id="authorsName" name="fileItemCreator" slot="authorsName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
    <logic:notPresent name="skipFileClassification">
        	<tr>
        		<th><bean:message key="label.teacher.siteAdministration.uploadFile.ResourceType" bundle="SITE_RESOURCES"/>:</th>
        		<td>
        	        <fr:edit id="educationalLearningResourceType" name="fileItemCreator" slot="educationalLearningResourceType">
        			<fr:layout>
        				<fr:property name="excludedValues" value="PROJECT_SUBMISSION,SITE_CONTENT"/>
        			</fr:layout>
        	        </fr:edit>
        		</td>
        	</tr>
    </logic:notPresent>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.file" bundle="SITE_RESOURCES"/>:</th>
		<td>
	    <fr:edit id="file" name="fileItemCreator" slot="file">
	        <fr:layout>
	            <fr:property name="size" value="40"/>
	            <fr:property name="fileNameSlot" value="fileName"/>
	            <fr:property name="fileSizeSlot" value="fileSize"/>
	        </fr:layout>
	    </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.permissions" bundle="SITE_RESOURCES"/>:</th>
		<td>
	        <fr:edit id="permissions" name="fileItemCreator" schema="item.file.create" layout="flow">
	            <fr:layout>
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
</table>

	<p class="mtop0 mbottom1 smalltxt"><em><bean:message key="label.allFieldsRequired" bundle="SITE_RESOURCES"/></em></p>
    
        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton dinline">
            <bean:message key="button.save"/>
        </html:submit>
	</fr:form>
</div>

<div class="dinline forminline">
	<fr:form action="<%=String.format("%s?method=section&amp;%s&amp;sectionID=%s", actionName, context, item.getSection().getIdInternal()) %>" encoding="multipart/form-data">
		<html:cancel styleClass="inputbutton dinline">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</fr:form>
</div>
