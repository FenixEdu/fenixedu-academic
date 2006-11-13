package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;

/**
 * Rearrange all the given item changing their position to match the one in the
 * given list. No matter the previous order of the item in the section it will
 * be given an order equal to it's position in the given list. So it's advisable
 * to include all items in the list or the section may contain two items with
 * the same order.
 * 
 * @author cfgi
 */
public class RearrangeSectionItems extends Service {

    public void run(Section section, List<Item> items) {
        section.setItemsOrder(items);
    }

}
