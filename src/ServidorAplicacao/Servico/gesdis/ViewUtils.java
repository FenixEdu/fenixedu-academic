package ServidorAplicacao.Servico.gesdis;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ISection;
import Dominio.ITeacher;


public class ViewUtils {
	
    public static List buildSectionsList(List sections, ArrayList fatherNode) {
		
		List result = new ArrayList();
		
		Iterator iter = sections.iterator();
		
		while (iter.hasNext()) {
			ISection section = (ISection) iter.next();
			ArrayList sonNode = (ArrayList) fatherNode.clone();
			sonNode.add(section.getName());
			result.add(sonNode);
			result.addAll(buildSectionsList(section.getInferiorSections(), sonNode));
		}
		
		return result;
    }
	
	public static List buildTeacherNamesList(List teachers) {
		List result = new ArrayList();
		Iterator iter = teachers.iterator();
		
		while (iter.hasNext()) {
			ITeacher teacher = (ITeacher) iter.next();
			result.add(teacher.getUsername());
		}
		return result;
	}
	
    public static List buildQualifiedName(ISection section) {
		ArrayList name = new ArrayList();
		
		ISection tmpSection = section;
		
		while (tmpSection != null) {
			name.add(0, tmpSection.getName());
			tmpSection = tmpSection.getSuperiorSection();
		}
		
		return name;
    }
}

