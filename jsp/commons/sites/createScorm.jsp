<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="sectionId" name="section" property="idInternal"/>
<bean:define id="itemId" name="item" property="idInternal"/>

<html:xhtml/>

<h2>
	<fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
</h3>

<ul>
	<li>
		<html:link page="<%= actionName + "?method=section&amp;sectionID=" + sectionId + "&amp;" + context %>">
			<bean:message key="link.goBack"/>
		</html:link>
	</li>
</ul>
	
<p>
<span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= actionName + "?method=uploadFile&amp;itemID=" + itemId + "&amp;" + context %>">			
			<bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= actionName + "?method=prepareUploadScormFile&amp;itemID=" + itemId + "&amp;" + context %>">			
		<bean:message key="link.scorm.uploadScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
	</span>
</span>
</p>

<div class="infoop2" style="width: 700px">
	<p><bean:message key="label.scorm.description" bundle="SITE_RESOURCES"/></p>
	<br/>
	<p><a href="javascript:switchDisplay('instructions');"><bean:message key="label.instructions" bundle="SITE_RESOURCES"/></a></p>
	<div id="instructions" class="switchNone">
		<p><strong><bean:message key="label.generalDescription" bundle="SITE_RESOURCES"/></strong></p>
		<p><bean:message key="label.generalDescription.explanation" bundle="SITE_RESOURCES"/></p>
		
		<p><strong><bean:message key="label.metaMetaDados" bundle="SITE_RESOURCES"/></strong></p>
		<p><bean:message key="label.metaMetaDados.explanation" bundle="SITE_RESOURCES"/></p>
		
		<p><strong><bean:message key="label.educational" bundle="SITE_RESOURCES"/></strong></p>
		<p class="mbottom2"><bean:message key="label.educational.explanation" bundle="SITE_RESOURCES"/></p>
		
		<p><strong><bean:message key="label.authorRights" bundle="SITE_RESOURCES"/></strong></p>
		<p><bean:message key="label.authorRights.explanation" bundle="SITE_RESOURCES"/></p>
	</div>
</div>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="SITE_RESOURCES">
		<p class="mvert15">
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<div class="dinline forminline">
<fr:form 
action="<%= actionName + "?method=validateScormForm&amp;itemID=" + itemId + "&amp;" + context %>"
encoding="multipart/form-data"
> 

<fr:edit id="scormPackage" name="bean" visible="false"/>

<p class="mbottom025"><strong><bean:message key="label.file" bundle="SITE_RESOURCES"/></strong></p>
<table class="tstyle5 thright thlight mtop025">

<tr>
	<th style="width: 140px;"><span class="required">*</span> <bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName"/>:</th>
	<td style="width: 450px;"><fr:edit name="bean" id="displayName" slot="displayName"/></td>
</tr>
	<tr>
		<th><span class="required">*</span> <bean:message key="label.teacher.siteAdministration.uploadFile.AuthorsName"/>:</th>
		<td>
	        <fr:edit name="bean" slot="authorsName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
<tr>
	<th><span class="required">*</span> <bean:message key="label.file" bundle="SITE_RESOURCES"/>:</th>
	<td>
		<fr:edit name="bean" slot="file" >
			<fr:layout>
				<fr:property name="size" value="40"/>
				<fr:property name="fileNameSlot" value="fileName"/>
				<fr:property name="fileSizeSlot" value="fileSize"/>
			</fr:layout>
		</fr:edit>
	</td>
</tr>
	<tr>
		<th><span class="required">*</span> <bean:message key="label.teacher.siteAdministration.uploadFile.ResourceType"/>:</th>
		<td>
	        <fr:edit name="bean" slot="educationalLearningResourceType">
	        <fr:layout>
      				<fr:property name="excludedValues" value="PROJECT_SUBMISSION"/>
	        </fr:layout>
	        </fr:edit>
		</td>
	</tr>  
<tr>
	<th><span class="required">*</span> <bean:message key="label.permitions" bundle="SITE_RESOURCES"/>:</th>
	<td>
	<fr:edit id="filePermissions" name="bean" schema="item.file.create" layout="flow">
		<fr:layout>
		    <fr:property name="eachInline" value="true"/>
		    <fr:property name="labelExcluded" value="true"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%="/manageSites.do?method=prepareCreateScormFile&amp;itemID=" + itemId + "&amp;" + context %>"/>		
	</fr:edit>
	</td>
</tr>
</table>

<p class="mbottom025"><strong><bean:message key="label.generalDescription" bundle="SITE_RESOURCES"/></strong></p>
<table class="tstyle5 thright thlight mtop025">

<tr>
	<th style="width: 140px;"><span class="required">*</span> <bean:message key="label.generalCatalog" bundle="SITE_RESOURCES"/>:</th>
	<td style="width: 450px;"><fr:edit name="bean" slot="generalCatalog"/></td>
</tr>

<tr>
	<th><span class="required">*</span> <bean:message key="label.generalEntry" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="generalEntry"/></td>
</tr>

<tr>
	<th><span class="required">*</span> <bean:message key="label.generalDescription" bundle="SITE_RESOURCES"/>:</th>
	<td>
		<fr:edit name="bean" slot="generalDescription" >
			<fr:layout name="longText">
				<fr:property name="columns" value="50"/>
				<fr:property name="rows" value="5"/>
 			</fr:layout>
		</fr:edit>	
	</td>
</tr>

<tr>
	<th><bean:message key="label.generalLanguage" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="generalLanguage"/></td>
</tr>

<tr>
	<th><span class="required">*</span><bean:message key="label.keywords" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="keywords">
		<fr:layout name="longText">
				<fr:property name="columns" value="50"/>
				<fr:property name="rows" value="2"/>
		</fr:layout>
		</fr:edit>
	</td>
</tr>

</table>

<p class="mbottom025"><strong><bean:message key="label.metaMetaDados" bundle="SITE_RESOURCES"/></strong></p>

<table class="tstyle5 thright thlight mtop025">

<tr>
	<th style="width: 140px;"><bean:message key="label.contributeRole" bundle="SITE_RESOURCES"/>:</th>
	<td style="width: 450px;"><fr:edit name="bean" slot="contributeRole"/></td>
</tr>


<tr>
	<th><bean:message key="label.virtualCardFile" bundle="SITE_RESOURCES"/>:</th>
	<td>
		<fr:edit name="bean" slot="virtualCardFile">
			<fr:layout>
				<fr:property name="size" value="40"/>
				<fr:property name="fileNameSlot" value="virtualCardFilename"/>
				<fr:property name="fileSizeSlot" value="virtualCardSize"/>
			</fr:layout>
		</fr:edit>
	</td>
</tr>



<tr>
	<th><bean:message key="label.contributeDate" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="contributeDate"/></td>
</tr>

</table>

<p class="mbottom025"><strong><bean:message key="label.educational" bundle="SITE_RESOURCES"/></strong></p>

<table class="tstyle5 thright thlight mtop025">

<tr>
	<th style="width: 140px;"><bean:message key="label.educationalInteractivityType" bundle="SITE_RESOURCES"/>:</th>
	<td style="width: 450px;"><fr:edit name="bean" slot="educationalInteractivityType"/></td>
</tr>
<tr>
	<th><bean:message key="label.educationalLanguage" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="educationalLanguage"/></td>
</tr>

</table>


<p class="mbottom025"><strong><bean:message key="label.authorRights" bundle="SITE_RESOURCES"/></strong></p>

<table class="tstyle5 thright thlight mtop025">

<tr>
	<th style="width: 140px;"><span class="required">*</span> <bean:message key="label.rightsCost" bundle="SITE_RESOURCES"/>:</th>
	<td style="width: 450px;">
		<fr:edit name="bean" slot="rightsCost" layout="radio" >
			<fr:layout>
				<fr:property name="classes" value="nobullet liinline"/>
			</fr:layout>
		</fr:edit>
	</td>
</tr>

<tr>
	<th><span class="required">*</span> <bean:message key="label.rightsCopyRightOtherRestrictions" bundle="SITE_RESOURCES"/>:</th>
	<td>
		<fr:edit name="bean" slot="rightsCopyRightOtherRestrictions" layout="radio" >
			<fr:layout>
				<fr:property name="classes" value="nobullet liinline" />
			</fr:layout>
		</fr:edit>
	</td>
</tr>

<tr>
	<th><bean:message key="label.rightsDescription" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="rightsDescription">
			<fr:layout name="longText">
				<fr:property name="columns" value="50"/>
				<fr:property name="rows" value="4"/>
		</fr:layout>
		</fr:edit>
	</td>
</tr>


</table>

<p class="smalltxt"><em><bean:message key="label.fieldsWith" bundle="SITE_RESOURCES"/> <span class="required">*</span> <bean:message key="label.areRequired" bundle="SITE_RESOURCES"/></em></p>

        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton dinline">
            <bean:message key="button.save"/>
        </html:submit>
	</fr:form>
</div>

<div class="dinline forminline">
	<fr:form action="<%=String.format("%s?method=section&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>" encoding="multipart/form-data">
		<html:cancel styleClass="inputbutton dinline">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</fr:form>
</div>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
