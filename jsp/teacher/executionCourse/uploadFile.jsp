<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<bean:define id="section" type="net.sourceforge.fenixedu.domain.Section" name="item" property="section"/>
<bean:define id="item" type="net.sourceforge.fenixedu.domain.Item" name="item"/>
<bean:define id="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse" name="executionCourse"/>
<bean:define id="url">/fileUpload.do?method=fileUpload&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>

<h2>
	<fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
</h3>

<span class="error">
    <html:errors />
</span>

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
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=prepareCreateScormFile&amp;itemID=" + item.getIdInternal() + "&amp;executionCourseID=" + executionCourse.getIdInternal() + "&amp;forwardTo=uploadScorm"%>">			
		<bean:message key="link.scorm.uploadScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=prepareCreateScormFile&itemID=" + item.getIdInternal() + "&executionCourseID=" + executionCourse.getIdInternal() + "&forwardTo=createScorm"%>">			
			<bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
</span>
</p>

<fr:form action="<%= url %>" encoding="multipart/form-data">
    <fr:edit id="creator" name="fileItemCreator" visible="false"/>

    <p class="mtop15">
        <strong><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName"/>:</strong>
    </p>
    
    <p>
        <fr:edit name="fileItemCreator" slot="displayName">
            <fr:layout>
                <fr:property name="size" value="40"/>
            </fr:layout>
        </fr:edit>
    </p>
        
    <p class="mtop15">
        <strong><bean:message key="label.teacher.siteAdministration.uploadFile.file"/>:</strong>
    </p>
    
    <p>
	    <fr:edit name="fileItemCreator" slot="file">
	        <fr:layout>
	            <fr:property name="size" value="40"/>
	            <fr:property name="fileNameSlot" value="fileName"/>
	            <fr:property name="fileSizeSlot" value="fileSize"/>
	        </fr:layout>
	    </fr:edit>
    </p>
    
    <p class="mtop15">
        <strong><bean:message key="label.teacher.siteAdministration.uploadFile.permissions"/>:</strong>
    </p>
    <p>
        <fr:edit name="fileItemCreator" schema="item.file.create" layout="flow">
            <fr:layout>
                <fr:property name="eachInline" value="true"/>
                <fr:property name="labelExcluded" value="true"/>
            </fr:layout>
        </fr:edit>
    </p>
    
    <p class="mtop2">
        <html:submit styleClass="inputbutton">
            <bean:message key="button.save"/>
        </html:submit>
         | 
        <html:link page="<%= String.format("/manageExecutionCourse.do?method=section&executionCourseID=%s&sectionID=%s", executionCourse.getIdInternal(), item.getSection().getIdInternal()) %>">
            <bean:message key="button.cancel"/>
        </html:link>
    </p>
</fr:form>
