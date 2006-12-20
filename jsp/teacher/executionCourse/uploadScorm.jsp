<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>

<h2>
	<fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="link.scorm.uploadScormFile"  bundle="SITE_RESOURCES"/>
</h3>

<ul>
<li>
<html:link page="<%= "/manageExecutionCourse.do?method=section&sectionID=" + section.getIdInternal() + "&executionCourseID=" + request.getParameter("executionCourseID") %>">
<bean:message key="link.goBack"/>
</html:link>
</li>
</ul>

<p>
<span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=uploadFile&itemID=" + item.getIdInternal() + "&executionCourseID=" + executionCourse.getIdInternal() + "&forwardTo=createScorm"%>">			
			<bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
		</html:link>
	</span>
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:message key="link.scorm.uploadScormFile" bundle="SITE_RESOURCES"/>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=prepareCreateScormFile&itemID=" + item.getIdInternal() + "&executionCourseID=" + executionCourse.getIdInternal() + "&forwardTo=createScorm"%>">			
			<bean:message key="link.scorm.createScormFile" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
</span>
</p>

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


<div class="switchNone" id="uploadScormFile">
<bean:define id="url">/fileUpload.do?method=scormFileUpload&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>

<fr:form action="<%= url %>" encoding="multipart/form-data">
    <fr:edit id="creator" name="bean" visible="false"/>

    <p class="mtop15">
        <strong><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName"/>:</strong>
    </p>
    
    <p>
        <fr:edit name="bean" slot="displayName">
            <fr:layout>
                <fr:property name="size" value="40"/>
            </fr:layout>
        </fr:edit>
    </p>
        
    <p class="mtop15">
        <strong><bean:message key="label.teacher.siteAdministration.uploadFile.file"/>:</strong>
    </p>
    
    <p>
	    <fr:edit name="bean" slot="file">
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
        <fr:edit name="bean" schema="item.file.create" layout="flow">
            <fr:layout>
                <fr:property name="eachInline" value="true"/>
                <fr:property name="labelExcluded" value="true"/>
            </fr:layout>
        </fr:edit>
    </p>
        
    <p class="mtop2">
        <html:submit styleClass="inputButton">
            <bean:message key="button.save"/>
        </html:submit>
		<html:link page="<%= String.format("/manageExecutionCourse.do?method=section&executionCourseID=%s&sectionID=%s", executionCourse.getIdInternal(), item.getSection().getIdInternal()) %>">
            <bean:message key="button.cancel"/>
        </html:link>
    </p>
</fr:form>
</div>
<p/>