<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<logic:equal name="site" property="showIntroduction" value="true">
	<div class="usiteintro">
		<fr:view name="site"  property="description" layout="html"/>
	</div>
</logic:equal>

<logic:equal name="site" property="showBanner" value="true">
	<div class="usitebanner">
		<logic:present name="site" property="currentBanner">
		<bean:define id="banner" name="site" property="currentBanner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner"/>
			<img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
		</logic:present>
	</div>
</logic:equal>

<jsp:include flush="true" page="mainBody.jsp"/>