package net.sourceforge.fenixedu.renderers.components;

public abstract class HtmlInputButton extends HtmlInputComponent {

    private boolean pressed;
    
    public HtmlInputButton(String type) {
        super(type);

        setPressed(false);
    }

    public void setText(String text) {
        super.setValue(text);
    }
    
    public String getText() {
        return getValue();
    }
    
    @Override
    public void setValue(String value) {
        setPressed(value != null);
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
