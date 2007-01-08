<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>

<<html:xhtml/>

<h2>
	<fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
</h3>

<ul>
<li>
<html:link page="<%= "/manageExecutionCourse.do?method=section&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") %>">
<bean:message key="link.goBack"/>
</html:link>
</li>
</ul>
	
<p>
<span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=uploadFile&amp;itemID=" + item.getIdInternal() + "&amp;executionCourseID=" + executionCourse.getIdInternal()%>">			
			<bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=prepareUploadScormFile&amp;itemID=" + item.getIdInternal() + "&amp;executionCourseID=" + executionCourse.getIdInternal()%>">			
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
action="<%="/manageExecutionCourse.do?method=validateScormForm&amp;itemID=" + request.getParameter("itemID") + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;forwardTo=createScorm"%>"
encoding="multipart/form-data"
> 

<fr:edit id="scormPackage" name="bean" visible="false"/>

<p class="mbottom025"><strong><bean:message key="label.file" bundle="SITE_RESOURCES"/></strong></p>
<table class="tstyle5 thright thlight mtop025" style="width: 550px">

<tr>
	<th style="width: 130px;"><span class="required">*</span> <bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName"/>:</th>
	<td style="width: 410px;"><fr:edit name="bean" id="displayName" slot="displayName"/></td>
</tr>
	<tr>
		<th><span class="required">*</span><bean:message key="label.teacher.siteAdministration.uploadFile.AuthorsName"/>:</th>
		<td>
	        <fr:edit name="bean" slot="authorsName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
<tr>
	<th><span class="required">*</span><bean:message key="label.file" bundle="SITE_RESOURCES"/>:</th>
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
		<th><span class="required">*</span><bean:message key="label.teacher.siteAdministration.uploadFile.ResourceType"/>:</th>
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
	<fr:edit name="bean" schema="item.file.create" layout="flow">
		<fr:layout>
		    <fr:property name="eachInline" value="true"/>
		    <fr:property name="labelExcluded" value="true"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%="/manageExecutionCourse.do?method=prepareCreateScormFile&amp;itemID=" + request.getParameter("itemID") + "&amp;executionCourseID=" + request.getParameter("executionCourseID")%>"/>		
	</fr:edit>
	</td>
</tr>
</table>



<p class="mbottom05">

<a href="javascript:switchDisplay('instructions');"><bean:message key="label.instructions" bundle="SITE_RESOURCES"/></a>
<div id="instructions" class="switchNone">
	<div class="infoop2 mtop025">
	<bean:message key="label.generalDescription" bundle="SITE_RESOURCES"/>
	<ul>
		<li> <bean:message key="label.generalDescription.explanation.item1" bundle="SITE_RESOURCES"/></li>
		<li> <bean:message key="label.generalDescription.explanation.item2" bundle="SITE_RESOURCES"/></li>
		<li> <bean:message key="label.generalDescription.explanation.item3" bundle="SITE_RESOURCES"/></li>
		<li> <bean:message key="label.generalDescription.explanation.item4" bundle="SITE_RESOURCES"/></li>
		<li> <bean:message key="label.generalDescription.explanation.item5" bundle="SITE_RESOURCES"/></li>
	</ul>
	
	<p><strong><bean:message key="label.metaMetaDados" bundle="SITE_RESOURCES"/></strong></p>
	<p><bean:message key="label.metaMetaDados.explanation" bundle="SITE_RESOURCES"/></p>
	
	<p><strong><bean:message key="label.educational" bundle="SITE_RESOURCES"/></strong></p>
	<p><bean:message key="label.educational.explanation" bundle="SITE_RESOURCES"/></p>
	
	<p><strong><bean:message key="label.authorRights" bundle="SITE_RESOURCES"/></strong></p>
	<p><bean:message key="label.authorRights.explanation" bundle="SITE_RESOURCES"/></p>
	
	</div>
</div>
 
<p class="mbottom025"><strong><bean:message key="label.generalDescription" bundle="SITE_RESOURCES"/></strong></p>
<table class="tstyle5 thright thlight mtop025" style="width: 550px">

<tr>
	<th><span class="required">*</span> <bean:message key="label.generalCatalog" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="generalCatalog"/></td>
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
				<fr:property name="rows" value="1"/>
		</fr:layout>
		</fr:edit>
	</td>
</tr>

</table>

<p class="mbottom025"><strong><bean:message key="label.metaMetaDados" bundle="SITE_RESOURCES"/></strong></p>

<table class="tstyle5 thright thlight mtop025" style="width: 550px">

<tr>
	<th style="width: 130px;"><bean:message key="label.contributeRole" bundle="SITE_RESOURCES"/>:</th>
	<td style="width: 410px;"><fr:edit name="bean" slot="contributeRole"/></td>
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

<table class="tstyle5 thright thlight mtop025" style="width: 550px">

<tr>
	<th style="width: 130px;"><bean:message key="label.educationalInteractivityType" bundle="SITE_RESOURCES"/>:</th>
	<td style="width: 410px;"><fr:edit name="bean" slot="educationalInteractivityType"/></td>
</tr>
<tr>
	<th><bean:message key="label.educationalLanguage" bundle="SITE_RESOURCES"/>:</th>
	<td><fr:edit name="bean" slot="educationalLanguage"/></td>
</tr>

</table>


<p class="mbottom025"><strong><bean:message key="label.authorRights" bundle="SITE_RESOURCES"/></strong></p>

<table class="tstyle5 thright thlight mtop025" style="width: 550px">

<tr>
	<th style="width: 130px;"><span class="required">*</span> <bean:message key="label.rightsCost" bundle="SITE_RESOURCES"/>:</th>
	<td style="width: 410px;">
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

<p><em><bean:message key="label.fieldsWith" bundle="SITE_RESOURCES"/> <span class="required">*</span> <bean:message key="label.areRequired" bundle="SITE_RESOURCES"/></em></p>

        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton dinline">
            <bean:message key="button.save"/>
        </html:submit>
	</fr:form>
</div>

<div class="dinline forminline">
	<fr:form action="<%=String.format("/manageExecutionCourse.do?method=section&amp;executionCourseID=%s&amp;sectionID=%s", executionCourse.getIdInternal(), item.getSection().getIdInternal()) %>" encoding="multipart/form-data">
		<html:cancel styleClass="inputbutton dinline">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</fr:form>
</div>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
