package DataBeans.util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoTeacher;



public class ViewUtils {
	
    public static List buildSectionsList(List sections, ArrayList fatherNode) {
		
		List result = new ArrayList();
		
		Iterator iter = sections.iterator();
		
		while (iter.hasNext()) {
			InfoSection section = (InfoSection) iter.next();
			ArrayList sonNode = (ArrayList) fatherNode.clone();
			sonNode.add(section.getName());
			result.add(sonNode);
			result.addAll(buildSectionsList(section.getInferiorInfoSections(), sonNode));
		}
		
		return result;
    }
	
	public static List buildTeacherNamesList(List teachers) {
		List result = new ArrayList();
		Iterator iter = teachers.iterator();
		
		while (iter.hasNext()) {
			InfoTeacher teacher =  (InfoTeacher) iter.next();
			result.add(teacher.getNome());
		}
		return result;
	}
	//why do i need a list of section names?
    public static List buildQualifiedName(InfoSection sectionView) {
		ArrayList name = new ArrayList();
		
		InfoSection tmpSection = sectionView;
		
		while (tmpSection != null) {
			name.add(0, tmpSection.getName());
			tmpSection = tmpSection.getSuperiorSection();
		}
		
		return name;
    }
}

