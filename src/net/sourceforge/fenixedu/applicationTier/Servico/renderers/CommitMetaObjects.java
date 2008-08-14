package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;

public class CommitMetaObjects extends Service {
    public void run(List<MetaObject> metaObjects) {
	for (MetaObject object : metaObjects) {
	    object.commit();
	}
    }
}
