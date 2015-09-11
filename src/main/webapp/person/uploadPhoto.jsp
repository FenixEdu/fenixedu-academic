<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

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
		width: 480px;
		margin-left: 10px;
	}
	#upload-button {
		left: 155px !important;
		top: 420px !important;
	}
</style>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" ></script>
<link href="<%= request.getContextPath() + "/javaScript/phroper/cropper/cropper-1.0.0-rc1.min.css" %>" rel="stylesheet">
<script src="<%= request.getContextPath() + "/javaScript/phroper/cropper/cropper-1.0.0-rc1.min.js" %>" type="text/javascript" ></script>
<link href="<%= request.getContextPath() + "/javaScript/phroper/phroper-2.1.0.css" %>" rel="stylesheet">
<script src="<%= request.getContextPath() + "/javaScript/phroper/phroper-2.1.0.js" %>" type="text/javascript"></script>

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
				$('#photoForm .form-group').toggle();
				$('<div id="photo-uploader"></div>').prependTo('#photoForm').css('margin-bottom','15px');
				phroper.start(false, 480, 480, captions);
				
				$('#submitButton').click( function () {
					if (phroper.hasLoadedPicture()) {
						var data = {}
						data.encodedPhoto = phroper.getPicture();
						var processResponse = function(response){
							if (response.success) {
								window.location.href = '${pageContext.request.contextPath}/personal';
							}
							if (response.reload) {
								phroper.reset();
							}
							if (response.error) {
								if ($('.error').length == 0) {
									$('<p class="mtop15">\
									        <span class="error"><!-- Error messages go here -->' +
									        	response.message + 
								        '</span>\
								    </p>').insertBefore('#new-info-panel');
									$('html, body').animate({ scrollTop: 0 }, 'slow');
								} else {
									$('.error').html(response.message);
									$('html, body').animate({ scrollTop: 0 }, 'slow');
								}
								
								phroper.reset();
							}
						};
						$.ajax({
						    headers: { 
						        'Accept': 'application/json',
						        'Content-Type': 'application/json' 
						    },
						    'type': 'POST',
						    'url': '${pageContext.request.contextPath}/user/photo/upload',
						    'data': JSON.stringify(data),
						    'dataType': 'json',
						    'success': processResponse
						    });
						return false;
					}
				});
				
				$('<button id="resetButton" type="button"><%= request.getAttribute("buttonClean") != null ? request.getAttribute("buttonClean") : "Clear canvas"  %></button>').appendTo('#photoForm').click( function () {
					phroper.reset(true);
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
		<div class="mvert1">
			<img src="data:${photo.contentType};base64,${photo.base64RawThumbnail}"/>
		</div>
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
