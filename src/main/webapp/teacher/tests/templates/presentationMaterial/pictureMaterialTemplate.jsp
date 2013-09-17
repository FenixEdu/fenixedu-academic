<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>

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

