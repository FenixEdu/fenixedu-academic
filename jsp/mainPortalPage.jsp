<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col_photo.jsp" flush="true">
  <tiles:put name="title" value=".IST" />
  <tiles:put name="serviceName" value="Portal do Instituto Superior Técnico" />
  <tiles:put name="navGeral" value=""/>
  <tiles:put name="photos" value="/entryPhotos.jsp"/>  
  <tiles:put name="body" value="/mainPortalPage_bd.jsp" />
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>
