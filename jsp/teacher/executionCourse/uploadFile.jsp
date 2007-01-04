<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<bean:define id="section" type="net.sourceforge.fenixedu.domain.Section" name="item" property="section"/>
<bean:define id="item" type="net.sourceforge.fenixedu.domain.Item" name="item"/>
<bean:define id="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse" name="executionCourse"/>
<bean:define id="url">/fileUpload.do?method=fileUpload&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>

<h2>
	<fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
</h3>

<p>
	<span class="error0">
	    <html:errors />
	</span>
</p>

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
			<bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=prepareCreateScormFile&amp;itemID=" + item.getIdInternal() + "&amp;executionCourseID=" + executionCourse.getIdInternal() + "&amp;forwardTo=uploadScorm"%>">			
		<bean:message key="link.scorm.uploadScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=prepareCreateScormFile&amp;itemID=" + item.getIdInternal() + "&amp;executionCourseID=" + executionCourse.getIdInternal() + "&amp;forwardTo=createScorm"%>">			
			<bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
</span>
</p>

<div class="dinline forminline">
<fr:form action="<%= url %>" encoding="multipart/form-data" >
    <fr:edit id="creator" name="fileItemCreator" visible="false"/>
	<fr:destination name="cancel" path="<%=String.format("/manageExecutionCourse.do?method=section&amp;executionCourseID=%s&amp;sectionID=%s", executionCourse.getIdInternal(), item.getSection().getIdInternal()) %>"/>

<table class="tstyle5 thright thlight mtop025">
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName"/>:</th>
		<td>
	        <fr:edit name="fileItemCreator" slot="displayName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.AuthorsName"/>:</th>
		<td>
	        <fr:edit name="fileItemCreator" slot="authorsName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.ResourceType"/>:</th>
		<td>
	        <fr:edit name="fileItemCreator" slot="educationalLearningResourceType">
			<fr:layout>
				<fr:property name="excludedValues" value="PROJECT_SUBMISSION"/>
			</fr:layout>
	        </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.file"/>:</th>
		<td>
	    <fr:edit name="fileItemCreator" slot="file">
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
	        <fr:edit name="fileItemCreator" schema="item.file.create" layout="flow">
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
	<fr:form action="<%=String.format("/manageExecutionCourse.do?method=section&amp;executionCourseID=%s&amp;sectionID=%s", executionCourse.getIdInternal(), item.getSection().getIdInternal()) %>" encoding="multipart/form-data">
		<html:cancel styleClass="inputbutton dinline">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</fr:form>
</div>

