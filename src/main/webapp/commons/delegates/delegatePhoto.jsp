<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present name="delegateBean" property="delegate">
	<div style="float: left; margin-right: 1em;">
		<img src="${pageContext.request.contextPath}/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=${delegateBean.delegate.externalId}"/>
	</div>
</logic:present>
