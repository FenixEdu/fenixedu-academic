<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<style type="text/css">
	#photo-uploader {
		margin-top: 30px;
		width: 500px;
		margin-left: 10px;
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

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
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

<%--
<html:form styleId="photoForm" action="/submitPhoto.do?method=photoUpload" enctype="multipart/form-data">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	
	<input type="hidden" id="phroperCaption" value="<%= request.getAttribute("phroperCaption") != null ? request.getAttribute("phroperCaption") : "" %>" />
	<input type="hidden" id="phroperSubCaption" value="<%= request.getAttribute("phroperSubCaption") != null ? request.getAttribute("phroperSubCaption") : "" %>" />
	<input type="hidden" id="phroperButtonCaption" value="<%= request.getAttribute("phroperButtonCaption") != null ? request.getAttribute("phroperButtonCaption") : "" %>" />
	<input type="hidden" id="phroperLoadingCaption" value="<%= request.getAttribute("phroperLoadingCaption") != null ? request.getAttribute("phroperLoadingCaption") : "" %>" />

	<table class="tstyle5 thlight thright thmiddle mtop05">
		<tr>
			<th><bean:message key="property.login.username" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" size="55"/></td>
		</tr>
		<tr>
			<th><bean:message key="title.loadMarks" /></th>
			<td><html:file bundle="HTMLALT_RESOURCES" altKey="file.theFile" property="theFile" size="50"/></td>
		</tr>
	</table>

	<p>
		<html:submit styleId="submitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
	</p>
</html:form> 
--%>
