<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>

<ft:define id="pictureMaterial" type="net.sourceforge.fenixedu.domain.tests.NewPictureMaterial" />
<ft:define id="pictureMaterialFile" property="pictureMaterialFile" type="net.sourceforge.fenixedu.domain.tests.PictureMaterialFile" />

<logic:equal name="pictureMaterial" property="inline" value="true">
	<ft:view property="pictureMaterialFile" layout="image" />
</logic:equal>

<logic:equal name="pictureMaterial" property="inline" value="false">
	<ft:view property="pictureMaterialFile">
		<ft:layout name="image">
			<ft:property name="classes" value="dblock" />
		</ft:layout>
	</ft:view>
</logic:equal>

