<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="commonComponent" name="siteView" property="commonComponent"/>
<tiles:insert definition="definition.publico.executionCourseSitePage" flush="true">
	<tiles:put name="title" beanName="commonComponent" beanProperty="title" />
	<tiles:put name="executionCourseName" beanName="commonComponent" beanProperty="title" />
</tiles:insert>