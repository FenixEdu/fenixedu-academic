<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="item" type="net.sourceforge.fenixedu.domain.Item" name="item"/>
<bean:define id="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse" name="executionCourse"/>

<h2>
	<fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/>
</h3>

<span class="error">
    <html:errors />
</span>

<bean:define id="url">/fileUpload.do?method=fileUpload&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>

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
