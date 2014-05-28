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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
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
		top: 300px !important;
	}
</style>

<script src="<%= request.getContextPath() + "/javaScript/phroper/fabric-1.2.0.all.min.js" %>" type="text/javascript" ></script>
<script src="<%= request.getContextPath() + "/javaScript/phroper/phroper-1.2.0.min.js" %>" type="text/javascript" ></script>

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
		if (phroper.testEnvironment()) {
			$('<div id="photo-uploader"></div>').prependTo('#photoForm').css('margin-bottom','15px');
			phroper.start(false,captions);
			$('#photoForm table tr:not(:first)').toggle();
			
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
				$('#photo-uploader').toggle();
				$('#photoForm table tr:not(:first)').toggle();
				$('#resetButton').toggle();
				$('#toggleClassic').toggle();
			});
		}
	});
</script>

<h2><bean:message key="link.operator.submitPhoto"/></h2>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES" property="error">
	<p class="mtop15">
		<span class="error"><!-- Error messages go here -->
			<bean:write name="message"/>
		</span>
	</p>
</html:messages>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES" property="success">
	<p class="mtop15">
		<span class="success0"><!-- Success messages go here -->
			<bean:write name="message"/>
		</span>
	</p>
</html:messages>

<fr:form id="photoForm" action="/submitPhoto.do" encoding="multipart/form-data">
	<html:hidden property="method" value="" />
	<input type="hidden" id="phroperCaption" value="<%= request.getAttribute("phroperCaption") != null ? request.getAttribute("phroperCaption") : "" %>" />
	<input type="hidden" id="phroperSubCaption" value="<%= request.getAttribute("phroperSubCaption") != null ? request.getAttribute("phroperSubCaption") : "" %>" />
	<input type="hidden" id="phroperButtonCaption" value="<%= request.getAttribute("phroperButtonCaption") != null ? request.getAttribute("phroperButtonCaption") : "" %>" />
	<input type="hidden" id="phroperLoadingCaption" value="<%= request.getAttribute("phroperLoadingCaption") != null ? request.getAttribute("phroperLoadingCaption") : "" %>" />

   	<fr:edit id="photoUpload" name="photo" schema="party.photo.operatorUpload">
           <fr:layout name="tabular-editable">
               <fr:property name="classes" value="tstyle2 thlight thwhite"/>
           </fr:layout>
       </fr:edit>
   	<html:submit styleId="submitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='photoUpload'">
	  <bean:message key="button.submit" />
   </html:submit>
</fr:form>
