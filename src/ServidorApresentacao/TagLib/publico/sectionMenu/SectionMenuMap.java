/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.TagLib.publico.sectionMenu;

import java.util.List;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SectionMenuMap {
	private List sections;
	/**
	 * 
	 */
	public SectionMenuMap(List infoSections) {
		setSections(infoSections);
	}

	/**
	 * @return List
	 */
	public List getSections() {
		return sections;
	}

	/**
	 * Sets the sections.
	 * @param sections The sections to set
	 */
	public void setSections(List sections) {
		this.sections = sections;
	}

}
