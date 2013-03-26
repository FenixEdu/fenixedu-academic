<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>


<jsp:include flush="true" page="/messaging/context.jsp"/>

	<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="announcementBoard" name="bean" property="fileHolder" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
	<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>		

<h2>
	<fr:view name="announcementBoard" property="name" />
</h2>

<h3>
	<bean:message key="label.files.management" bundle="MESSAGING_RESOURCES"/>
</h3>

<ul>
<li>
<html:link page="<%=  contextPrefix + "method=viewAnnouncements&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>">
	<bean:message key="link.goBack" bundle="SITE_RESOURCES"/>
</html:link>
</li>
<li>
	<a href="#" onclick="javascript:showElement('insertFile')">
		<bean:message key="title.item.file.upload" bundle="SITE_RESOURCES"/>
	</a>
</li>
</ul>


<div id="insertFile" class="switchNone">
		<logic:messagesPresent message="true">
			<html:messages id="messages" message="true">
				<p>
					<span class="error0"><bean:write name="messages" /></span>
				</p>
			</html:messages>
		</logic:messagesPresent>

		<p class="mvert1">
		    <span class="error0">
		        <html:errors property="unableToStoreFile"/>
		        <html:errors property="section" bundle="SITE_RESOURCES"/>
		    </span>
		</p>

		<div class="dinline forminline">
		<strong><bean:message key="title.item.file.upload" bundle="SITE_RESOURCES"/></strong>:

		<fr:form action="<%= contextPrefix + "method=fileUpload&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>" encoding="multipart/form-data" >
		    <fr:edit id="creator" name="bean" visible="false">
		        	<fr:destination name="cancel" path="<%= String.format("%s?method=viewAnnouncements&amp;announcementBoardId=%s&amp;%s", contextPrefix, announcementBoardId, extraParameters) %>"/>
		    </fr:edit>
		
		<table class="tstyle5 thright thlight mtop025">
			<tr>
				<th><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName" bundle="SITE_RESOURCES"/>:</th>
				<td>
			        <fr:edit id="displayName" name="bean" slot="displayName">
			            <fr:layout>
			                <fr:property name="size" value="40"/>
			            </fr:layout>
			        </fr:edit>
			        <p class="smalltxt color888 mvert0"><bean:message key="label.small.file.description" bundle="SITE_RESOURCES"/>.</p>
				</td>
			</tr>
			<tr>
				<th><bean:message key="label.teacher.siteAdministration.uploadFile.file" bundle="SITE_RESOURCES"/>:</th>
				<td>
			    <fr:edit id="file" name="bean" slot="file">
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
			        <fr:edit id="permissions" name="bean" schema="fileContent.permissions" layout="flow">
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
			
			<div class="switchInline">
				<fr:form>
					<html:submit onclick="javascript:hideElement('insertFile')">
					    	<bean:message key="button.cancel"/>
					</html:submit>
				</fr:form>
			</div>
		</div>
</div>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>

<p>
<fr:view name="announcementBoard" property="files" schema="site.item.file.advanced" layout="tabular">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="order(edit)" value="1"/>
		<fr:property name="link(edit)" value="<%= contextPrefix + "method=editFile&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>"/>
		<fr:property name="param(edit)" value="idInternal/fileId"/>
		<fr:property name="key(edit)" value="link.edit"/>
		<fr:property name="bundle(edit)" value="SITE_RESOURCES"/>
		<fr:property name="order(remove)" value="2"/>
		<fr:property name="link(remove)" value="<%= contextPrefix + "method=deleteFile&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>"/>
		<fr:property name="param(remove)" value="idInternal/fileId"/>
		<fr:property name="key(remove)" value="link.delete"/>
		<fr:property name="bundle(remove)" value="SITE_RESOURCES"/> 
	</fr:layout>
</fr:view>	
</p>




	
