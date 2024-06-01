package com.apirest.genericlass;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

public abstract class DialogViewEvent<T, V extends Component> extends ComponentEvent<V> {
    @Getter
    private T objet;
    public DialogViewEvent(V source, T objet) {
        super(source, false);
        this.objet= objet;
    }
}
