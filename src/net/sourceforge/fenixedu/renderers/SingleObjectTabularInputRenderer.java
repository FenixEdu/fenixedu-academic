package net.sourceforge.fenixedu.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.model.MetaObjectCollection;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;

/**
 * {@inheritDoc}
 * 
 * This renderer can be used to edit a single object using the same layout. The
 * object is wrapped in a list and then is edited as described above.
 * 
 * @author cfgi
 */
public class SingleObjectTabularInputRenderer extends TabularInputRenderer {

    @Override
    public HtmlComponent render(Object object, Class type) {
        if (object instanceof Collection) {
            return super.render(object, type);
        }
        else {
            List list = new ArrayList();
            list.add(object);
            
            MetaObjectCollection multipleMetaObject = MetaObjectFactory.createObjectCollection();
            multipleMetaObject.add(getInputContext().getMetaObject());
            
            getInputContext().setMetaObject(multipleMetaObject);
            
            return super.render(list, list.getClass());
        }
    }
}
