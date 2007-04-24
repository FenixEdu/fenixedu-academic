<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:equal name="site" property="showBanner" value="true">
<%--
background: #xxx url(xxx.gif) top left repeat-x 
--%>
<div class="usitebanner" style="width: 100%; float: left;">
	<logic:equal name="site" property="showIntroduction" value="true">
		<div class="usiteintrofloated">
			<fr:view name="site"  property="description" layout="html"/>
		</div>
	</logic:equal>
	<logic:present name="site" property="currentBanner">
		<bean:define id="banner" name="site" property="currentBanner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner"/>
		<img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
	</logic:present>
</div>
<div style="clear: both;"></div>
</logic:equal>

<logic:notEqual name="site" property="showBanner" value="true">
	<logic:equal name="site" property="showIntroduction" value="true">
		<div class="usiteintro">
			<fr:view name="site"  property="description" layout="html"/>
		</div>
	</logic:equal>
</logic:notEqual>

<jsp:include flush="true" page="mainBody.jsp"/>