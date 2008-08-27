<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.person.photo.title" /></h2>

<div class="infoop2">
    <p class="mvert0"><bean:message key="label.person.photo.file.info" /></p>
</div>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
    <p class="mtop15">
        <span class="error"><!-- Error messages go here -->
            <bean:write name="message"/>
        </span>
    </p>
</html:messages>

<fr:form action="/uploadPhoto.do" encoding="multipart/form-data">
	<html:hidden property="method" value="" />
	<fr:edit id="photoUpload" name="photo" schema="party.photo.upload">
        <fr:layout name="tabular-editable">
            <fr:property name="classes" value="tstyle2 thlight thwhite"/>
        </fr:layout>
    </fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		onclick="this.form.method.value='upload'">
		<bean:message key="button.submit" />
	</html:submit>

	<logic:present name="preview">
		<bean:define id="tempfile" name="photo" property="tempCompressedFile.absolutePath" />
		<div class="mvert1"><html:img align="middle"
			src="<%=request.getContextPath() + "/person/uploadPhoto.do?method=preview&amp;file=" + tempfile%>"
			altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showPhoto" /></div>
		<p class="mtop15 mbottom1">Deseja substituir a imagem antiga por esta?</p>
		<p class="mvert0"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='save'">
			<bean:message key="button.substitute" />
		</html:submit> <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='cancel'">
			<bean:message key="button.cancel" />
		</html:submit></p>
	</logic:present>
</fr:form>
