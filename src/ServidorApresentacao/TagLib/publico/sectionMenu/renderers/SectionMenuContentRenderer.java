/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.TagLib.publico.sectionMenu.renderers;
import javax.servlet.jsp.tagext.TagSupport;

import DataBeans.gesdis.InfoSection;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SectionMenuContentRenderer extends TagSupport
	implements SectionMenuSlotContentRenderer {
	
	private InfoSection infoSection;
	
	public SectionMenuContentRenderer(){}
	
	public SectionMenuContentRenderer(InfoSection infoSection){
		setInfoSection(infoSection);
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionMenuSlotContentRenderer#renderSectionLabel(ServidorApresentacao.TagLib.publico.sectionMenu.SectionMenuSlot)
	 */
	public StringBuffer renderSectionLabel(int i,String path) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(renderDepthContent(getInfoSection(),i,path));
		return strBuffer;
	}

	private StringBuffer renderDepthIdent(InfoSection infoSection) {
		StringBuffer strBuffer = new StringBuffer();
		int depth = infoSection.getSectionDepth().intValue();
		while (depth >0){
			strBuffer.append("&nbsp&nbsp");
			depth--;
		}
		return strBuffer;
	}
	private StringBuffer renderDepthContent(InfoSection infoSection, int i,String path) {
			StringBuffer strBuffer = new StringBuffer();
			int depth = infoSection.getSectionDepth().intValue();
			if (depth == 0){
				//adds the info
			
			strBuffer.append("<ul><li>\n");
			
			
			strBuffer.append("<a href=\""+path+"/viewSection.do?index="+i+"\""+ " onclick=\"houdini('"+infoSection.getName()+"');\">\n");
			
			strBuffer.append(renderDepthIdent(getInfoSection()));
			strBuffer.append(infoSection.getName());
			strBuffer.append("</a>");
			strBuffer.append("</li></ul>\n");}
			else {
				//adds the info
			strBuffer.append("<dd>");
					
			
			strBuffer.append("<a href=\"viewSection.do?index="+i+"\" >");
			//falta o index 
			strBuffer.append(renderDepthIdent(getInfoSection()));
			strBuffer.append(infoSection.getName());
			strBuffer.append("</a>");
			strBuffer.append("</dd>\n");}
			
			return strBuffer;
		}

	/**
	 * @return InfoSection
	 */
	public InfoSection getInfoSection() {
		return infoSection;
	}

	/**
	 * Sets the infoSection.
	 * @param infoSection The infoSection to set
	 */
	public void setInfoSection(InfoSection infoSection) {
		this.infoSection = infoSection;
	}



}
