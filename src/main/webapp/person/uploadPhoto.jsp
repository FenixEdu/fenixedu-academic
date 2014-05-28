<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<style type="text/css">
	#photo-uploader {
		margin-top: 30px;
		width: 500px;
		margin-left: 10px;
	}
	#upload-button {
		left: 155px !important;
		top: 420px !important;
	}
</style>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" ></script>
<script src="<%= request.getContextPath() + "/javaScript/phroper/fabric-1.2.0.all.min.js" %>" type="text/javascript" ></script>
<script src="<%= request.getContextPath() + "/javaScript/phroper/phroper-1.2.0.min.js" %>" type="text/javascript" ></script>

<logic:notPresent name="preview">
	<script type="text/javascript">
		$(document).ready( function () {
			var captions = [];
			if ($('#phroperCaption').attr('value')) {
				captions[0] = $('#phroperCaption').attr('value');
			}
			if ($('#phroperSubCaption').attr('value')) {
				captions[1] = $('#phroperSubCaption').attr('value');
			}
			if ($('#phroperButtonCaption').attr('value')) {
				captions[2] = $('#phroperButtonCaption').attr('value');
			}
			if ($('#phroperButtonCaption').attr('value')) {
				captions[3] = $('#phroperLoadingCaption').attr('value');
			}
			if (phroper.testEnvironment()) {
				$('#old-info-panel').toggle();
				$('<div id="photo-uploader"></div>').prependTo('#photoForm').css('margin-bottom','15px');
				phroper.start(false,captions);
				$('#photoForm table').toggle();
				
				$('#submitButton').click( function () {
					if (phroper.hasLoadedPicture()) {
						var base64Thumbnail = phroper.getThumbnail();
						$('<input type="hidden" name="encodedThumbnail" value="'+base64Thumbnail+'">').appendTo('#photoForm');
						var base64Picture = phroper.getPicture();
						$('<input type="hidden" name="encodedPicture" value="'+base64Picture+'">').appendTo('#photoForm');
					}
				});
				
				$('<button id="resetButton" type="button"><%= request.getAttribute("buttonClean") != null ? request.getAttribute("buttonClean") : "Clear canvas"  %></button>').appendTo('#photoForm').click( function () {
					phroper.reset(true);
				});
				$('<button id="toggleClassic" type="button"><%= request.getAttribute("buttonRevert") != null ? request.getAttribute("buttonRevert") : "Use old version"  %></button>').appendTo('#photoForm').click( function () {
					$('#new-info-panel').toggle();
					$('#old-info-panel').toggle();
					$('#photo-uploader').toggle();
					$('#photoForm table').toggle();
					$('#resetButton').toggle();
					$('#toggleClassic').toggle();
				});
			}
		});
	</script>
</logic:notPresent>

<h2><bean:message key="label.person.photo.title" /></h2>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
    <p class="mtop15">
        <span class="error"><!-- Error messages go here -->
            <bean:write name="message"/>
        </span>
    </p>
</html:messages>

<logic:notPresent name="preview">

	<div id="new-info-panel" class="infoop2">
   		<p class="mvert0"><bean:message key="label.person.photo.file.info.crop" /></p>
	</div>
	
	<div id="old-info-panel" class="infoop2">
   		<p class="mvert0"><bean:message key="label.person.photo.file.info.classic" /></p>
	</div>
	

	<p class="mtop2">
		<html:link page="/uploadPhoto.do?method=backToShowInformation">
			<bean:message key="link.back" bundle="COMMON_RESOURCES"/>
		</html:link>
	</p>
</logic:notPresent>

<fr:form id="photoForm" action="/uploadPhoto.do" encoding="multipart/form-data">
	<html:hidden property="method" value="" />
	
	<logic:notPresent name="preview">
		<input type="hidden" id="phroperCaption" value="<%= request.getAttribute("phroperCaption") != null ? request.getAttribute("phroperCaption") : "" %>" />
		<input type="hidden" id="phroperSubCaption" value="<%= request.getAttribute("phroperSubCaption") != null ? request.getAttribute("phroperSubCaption") : "" %>" />
		<input type="hidden" id="phroperButtonCaption" value="<%= request.getAttribute("phroperButtonCaption") != null ? request.getAttribute("phroperButtonCaption") : "" %>" />
		<input type="hidden" id="phroperLoadingCaption" value="<%= request.getAttribute("phroperLoadingCaption") != null ? request.getAttribute("phroperLoadingCaption") : "" %>" />
	</logic:notPresent>

    <logic:notPresent name="preview">
		
    	<fr:edit id="photoUpload" name="photo" schema="party.photo.upload">
            <fr:layout name="tabular-editable">
                <fr:property name="classes" value="tstyle2 thlight thwhite"/>
            </fr:layout>
        </fr:edit>
    	<html:submit styleId="submitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		  onclick="this.form.method.value='upload'">
		  <bean:message key="button.submit" />
	   </html:submit>
    </logic:notPresent>

	<logic:present name="preview">
        <p class="mtop2">
            <html:link page="/uploadPhoto.do?method=prepare">
                <bean:message key="link.back" bundle="COMMON_RESOURCES" />
            </html:link>
        </p>

        <fr:edit id="photoUpload" name="photo" schema="party.photo.upload.clean" />
		<bean:define id="tempfile" name="photo" property="tempCompressedFile.absolutePath" />
		<div class="mvert1"><html:img align="middle"
			src="<%=request.getContextPath() + "/person/uploadPhoto.do?method=preview&amp;file=" + tempfile%>"
			altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showPhoto" /></div>
		<p class="mtop15 mbottom1">
			<bean:message key="message.person.uploadPhoto.confirm"/>
		</p>
		<p class="mvert0"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='save'">
			<bean:message key="button.replace" />
		</html:submit> <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='cancel'">
			<bean:message key="button.cancel" />
		</html:submit></p>
	</logic:present>
</fr:form>
