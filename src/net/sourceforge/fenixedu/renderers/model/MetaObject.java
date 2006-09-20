package net.sourceforge.fenixedu.renderers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

/**
 * A MetaObject is an abstraction of a real domain object. If provides a
 * serializable representation or view of a concrete object that can be
 * freely manipulated in the renderer's lifecycle. The MetaObject prevents
 * changes from beeing made to the domain object until the object is commited
 * with {@link #commit()}.
 * 
 * @author cfgi
 */
public abstract class MetaObject implements Serializable {
    
    private Properties properties;
    
    private transient UserIdentity user;
    
    private List<MetaSlot> slots;
    private List<MetaSlot> hiddenSlots;

    private InstanceCreator creator;

    private String schemaName;
    private transient Schema schema;
    
    public MetaObject() {
        super();
        
        this.properties = new Properties();
        this.slots = new ArrayList<MetaSlot>();
        this.hiddenSlots = new ArrayList<MetaSlot>();
    }

    /**
     * Allows access to the concrete domain object that is beeing protected 
     * by this meta-object.
     * 
     * @return the domain object hidden by this meta-object
     */
    public abstract Object getObject();
    
    /**
     * @return the type of the domain object
     */
    public Class getType() {
        Object object = getObject();
        
        if (object == null) {
            return null;
        }
        else {
            return object.getClass();
        }
    }
    
    public Schema getSchema() {
        if (this.schema == null && this.schemaName != null) {
            this.schema = RenderKit.getInstance().findSchema(this.schemaName);
        }
        
        return this.schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
        
        if (this.schema != null) {
            this.schemaName = this.schema.getName();
        }
    }

    public Properties getProperties() {
        return this.properties;
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    

    /**
     * @return the key that identifies the underlying object
     * 
     * @see MetaObjectKey
     */
    public abstract MetaObjectKey getKey();

    public UserIdentity getUser() {
        return this.user;
    }
    
    public void setUser(UserIdentity user) {
        if (this.user != null && this.user.equals(user)) {
            return;
        }
        
        this.user = user;
    }

    public List<MetaSlot> getSlots() {
        return this.slots;
    }

    /**
     * Conveniency method that allows you to obtain a particular {@link MetaSlotsssss}
     * by it's name.
     * 
     * @param name the name of the meta slot
     * 
     * @return the meta slot with the given name or <code>null</code> if not found
     */
    public MetaSlot getSlot(String name) {
        if (name == null) {
            return null;
        }
        
        for (MetaSlot slot : getAllSlots()) {
            if (slot.getName().equals(name)) {
                return slot;
            }
        }
        
        return null;
    }
    
    public void addSlot(MetaSlot slot) {
        this.slots.add(slot);
    }
    
    public boolean removeSlot(MetaSlot slot) {
        return this.slots.remove(slot);
    }

    public List<MetaSlot> getHiddenSlots() {
        return this.hiddenSlots;
    }
    
    public void addHiddenSlot(MetaSlot slot) {
        this.hiddenSlots.add(slot);
    }
    
    /**
     * Allows acces to the list of all slots. This list is not backed by the meta object so any changes
     * to the list will not affect the meta object.
     * 
     * @return the list of all slots of this meta object. This includes visible slots and hidden slots.
     */
    public List<MetaSlot> getAllSlots() {
        List<MetaSlot> all = new ArrayList<MetaSlot>();

        all.addAll(getSlots());
        all.addAll(getHiddenSlots());
        
        return all;
    }
    
    public void setInstanceCreator(InstanceCreator creator) {
        this.creator = creator;
    }

    /**
     * If this meta object needs to create a new instance of the object then this {@link InstanceCreator}
     * will be used to create that instance.
     * 
     * @return an instance of the InstanceCreator used to create a new object or null if the object should
     *         be created using the default constructor
     */
    public InstanceCreator getInstanceCreator() {
        return this.creator;
    }
    
    
    /**
     * Since the MetaObject is an abstraction for the real object this method
     * allows a subclass to delay any changes to the application domain until the
     * very last fase of the renderers lifecycle.
     * 
     * <p>
     * This method is called at the end of the renderers' lifecycle when the
     * domain should be updated.
     */
    public abstract void commit();
}
