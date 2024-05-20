package com.apirest.views.autores;

import com.apirest.restdata.AutorDTO;
import com.apirest.restservices.AutorRestService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoresAbmView extends Dialog {
    private AutorRestService autorRestService;
    private TextField nombres= new TextField("Nombres");
    private TextField apellidos= new TextField("Apellidos");
    private TextField telefono= new TextField("Teléfono");
    private Button save= new Button("Grabar");
    private Button cancel= new Button("Cancelar");

    public AutoresAbmView(@Autowired AutorRestService autorRestService) {
        this.autorRestService= autorRestService;
        setHeaderTitle("Autor");
        add(layoutImput());
        getFooter().add(layoutFooter());

        //ACTION BUTTON SAVE
        save.addClickListener(this::grabar);
    }

    private void grabar(ClickEvent<Button> event) {

        AutorDTO autorDTO = new AutorDTO();

        autorDTO.setNombres(nombres.getValue());
        autorDTO.setApellidos(apellidos.getValue());
        autorDTO.setTelefono(telefono.getValue());

        autorRestService.save(autorDTO);

    }

    private Component layoutFooter() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        var layout = new HorizontalLayout();
        layout.add(save,cancel);
        return layout;
    }

    private Component layoutImput() {
        var form= new FormLayout();
        nombres.setClearButtonVisible(true);
        apellidos.setClearButtonVisible(true);
        telefono.setClearButtonVisible(true);
        form.add(nombres,apellidos,telefono);
        form.setSizeFull();
        return form;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        nombres.focus();
    }
}
