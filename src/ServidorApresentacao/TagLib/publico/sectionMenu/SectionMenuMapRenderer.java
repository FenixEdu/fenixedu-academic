/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.TagLib.publico.sectionMenu;

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import ServidorApresentacao
	.TagLib
	.publico
	.sectionMenu
	.renderers
	.SectionMenuContentRenderer;
import ServidorApresentacao
	.TagLib
	.publico
	.sectionMenu
	.renderers
	.SectionMenuSlotContentRenderer;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SectionMenuMapRenderer {
	private SectionMenuMap sectionMenuMap;
	private SectionMenuSlotContentRenderer sectionMenuSlotContentRenderer;

	/**
	 * 
	 */
	public SectionMenuMapRenderer(
		SectionMenuMap sectionMenuMap,
		SectionMenuSlotContentRenderer sectionMenuSlotContentRenderer) {
		setSectionMenuMap(sectionMenuMap);
		setSectionMenuSlotContentRenderer(sectionMenuSlotContentRenderer);
	}

	/**
	 * @return SectionMenuMap
	 */
	public SectionMenuMap getSectionMenuMap() {
		return sectionMenuMap;
	}

	/**
	 * @return SectionMenuSlotContentRenderer
	 */
	public SectionMenuSlotContentRenderer getSectionMenuSlotContentRenderer() {
		return sectionMenuSlotContentRenderer;
	}

	/**
	 * Sets the sectionMenuMap.
	 * @param sectionMenuMap The sectionMenuMap to set
	 */
	public void setSectionMenuMap(SectionMenuMap sectionMenuMap) {
		this.sectionMenuMap = sectionMenuMap;
	}

	/**
	 * Sets the sectionMenuSlotContentRenderer.
	 * @param sectionMenuSlotContentRenderer The sectionMenuSlotContentRenderer to set
	 */
	public void setSectionMenuSlotContentRenderer(SectionMenuSlotContentRenderer sectionMenuSlotContentRenderer) {
		this.sectionMenuSlotContentRenderer = sectionMenuSlotContentRenderer;
	}

	public StringBuffer render() {
		StringBuffer strBuffer = new StringBuffer("");
		List sections = getSectionMenuMap().getSections();
		Iterator iter = sections.iterator();
		if (sections!=null && !sections.isEmpty()) {
		int i = 0;
		
			while (i!= sections.size()) {
			
				InfoSection infoSection = (InfoSection) sections.get(i);
				SectionMenuSlotContentRenderer sectionMenuSlot =
					new SectionMenuContentRenderer(infoSection);
				strBuffer.append(sectionMenuSlot.renderSectionLabel());
				strBuffer.append(renderSuffix(sections, i));
				
				i=i+1;
			}
		}
		return strBuffer;
	}

	private StringBuffer renderSuffix(List sections, int iterator) {
		StringBuffer strBuffer = new StringBuffer("");
		if (((InfoSection) sections.get(iterator)).getSectionDepth().intValue() != 0 && iterator==sections.size()-1) {
						strBuffer.append("</dl>\n");	
			 
					}
		else { if(iterator!=sections.size()-1) {
		
				
				
				if (((InfoSection) sections.get(iterator+1)).getSectionDepth().intValue() == 0
				&& ((InfoSection) sections.get(iterator)).getSectionDepth().intValue() != 0) {
				strBuffer.append("</dl>\n");				
				}
				if (((InfoSection) sections.get(iterator+1)).getSectionDepth().intValue() != 0
				&& ((InfoSection) sections.get(iterator)).getSectionDepth().intValue() == 0) {				
				strBuffer.append("<dl id='seccao' style=display: none;>\n");
				}}}
				
			
			
		return strBuffer;
	}

}
