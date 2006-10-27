package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Section;
import pt.ist.utl.fenix.utils.Pair;

/**
 * Rearrange all the given sections changing their superior section.
 * This service receives a list of pairs &lt;parent, child&gt;. For 
 * each pair the child's current superior section will be changed to the
 * parent in the pair. 
 * 
 * @author cfgi
 */
public class RearrangeSiteSections extends Service {

    public void run(List<Pair<Section, Section>> arrangements) {
        Map<Section, List<Section>> newOrderMap = new HashMap<Section, List<Section>>();
        
        for (Pair<Section, Section> pair : arrangements) {
            Section parent = pair.getKey();
            Section child  = pair.getValue();
            
            child.setSuperiorSection(parent);
            addChildSection(newOrderMap, parent, child);
        }
        
        updateChildrenOrder(newOrderMap);
    }

    private void addChildSection(Map<Section, List<Section>> newOrderMap, Section parent, Section child) {
        if (! newOrderMap.containsKey(parent)) {
            newOrderMap.put(parent, new ArrayList<Section>());
        }
        
        List<Section> children = newOrderMap.get(parent);
        children.add(child);
    }
    
    private void updateChildrenOrder(Map<Section, List<Section>> newOrderMap) {
        for (List<Section> childList : newOrderMap.values()) {
            int order = 0;
            for (Section section : childList) {
                section.setSectionOrder(order++);
            }
        }
    }

}
