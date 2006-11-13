package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
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

    public void run(Site site, List<Pair<Section, Section>> arrangements) {
        Map<Section, List<Section>> newOrderMap = new HashMap<Section, List<Section>>();
        
        for (Pair<Section, Section> pair : arrangements) {
            Section parent = pair.getKey();
            Section child  = pair.getValue();
            
            child.setSuperiorSection(parent);
            addChildSection(newOrderMap, parent, child);
        }
        
        updateChildrenOrder(site, newOrderMap);
    }

    private void addChildSection(Map<Section, List<Section>> newOrderMap, Section parent, Section child) {
        if (! newOrderMap.containsKey(parent)) {
            newOrderMap.put(parent, new ArrayList<Section>());
        }
        
        List<Section> children = newOrderMap.get(parent);
        children.add(child);
    }
    
    private void updateChildrenOrder(Site site, Map<Section, List<Section>> newOrderMap) {
        for (Section section : newOrderMap.keySet()) {
            if (section != null) {
                section.setSectionsOrder(newOrderMap.get(section));
            }
            else {
                site.setTopLevelSectionsOrder(newOrderMap.get(section));
            }
        }
    }

}
