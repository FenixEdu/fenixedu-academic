package pt.utl.ist.renderers;

import java.util.ArrayList;
import java.util.List;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;

public class RendererNode {
    public ClassDoc doc;
    public List<MethodDoc> properties = new ArrayList<MethodDoc>();;
    
    public RendererNode parent;
    public List<RendererNode> children = new ArrayList<RendererNode>();
    public List<ConfigurationNode> uses = new ArrayList<ConfigurationNode>();
}
