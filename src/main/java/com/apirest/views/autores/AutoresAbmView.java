package com.apirest.views.autores;

import com.apirest.genericlass.DialogViewEvent;
import com.apirest.restdata.AutorDTO;
import com.apirest.restservices.AutorRestService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoresAbmView extends Dialog {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoresAbmView.class);
    private Binder<AutorDTO> binder = new Binder<>();
    private AutorRestService autorRestService;
    private TextField nombres = new TextField("Nombres");
    private TextField apellidos = new TextField("Apellidos");
    private TextField telefono = new TextField("Teléfono");
    private Button save = new Button("Grabar");
    private Button cancel = new Button("Cancelar");
    private Button delete = new Button("Eliminar");

    public AutoresAbmView(@Autowired AutorRestService autorRestService) {
        this.autorRestService = autorRestService;
        setHeaderTitle("Autor");
        add(layoutImput());
        getFooter().add(layoutFooter());

        //ACTION BUTTON SAVE
        save.addClickListener(event -> {
            try {
                saveAthor();
            } catch (RuntimeException ex) {
                LOGGER.error(ex.getMessage(), ex.getStackTrace());
            }
        });
        delete.addClickListener(event -> {
            try {
                deleteAthor(binder.getBean());
                fireEvent(new DeleteAthorEvent(this, binder.getBean()));
            } catch (Exception ex) {
                LOGGER.error("Error...", ex.getMessage());
            }
        });
        cancel.addClickListener(e -> close());

        //CONFIG INPUT BINDER
        binder.forField(nombres)
                .asRequired("Campo nombre es requerido")
                .bind(AutorDTO::getNombres, AutorDTO::setNombres);
        binder.forField(apellidos)
                .asRequired("Campo apellido es requerido")
                .bind(AutorDTO::getApellidos, AutorDTO::setApellidos);
        binder.forField(telefono)
                .bind(AutorDTO::getTelefono, AutorDTO::setTelefono);

        //
        binder.addStatusChangeListener(statusChangeEvent -> {
            if (binder.getBean() != null) {
                if (binder.getBean() != null) {
                    delete.setEnabled(binder.getBean().getCodigo() != null);
                }
                save.setEnabled(!statusChangeEvent.hasValidationErrors());
            }
        });
    }


    public void setAthor(AutorDTO autorDTO) {
        binder.setBean(autorDTO);
    }

    private void saveAthor() throws RuntimeException {
        if (binder.writeBeanIfValid(binder.getBean())) {
            binder.setBean(
                    autorRestService.save(binder.getBean())
            );
            fireEvent(new SaverAthorEvent(this, binder.getBean()));
        }
    }

    private void deleteAthor(AutorDTO author) throws Exception {
        autorRestService.deleteById(author);
    }

    private Component layoutFooter() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        var layout1 = new HorizontalLayout();
        var layout2 = new HorizontalLayout();
        layout2.add(delete);
        layout2.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        layout2.setPadding(false);
        layout2.setSpacing(false);
        layout2.setWidthFull();
        layout1.add(layout2, save, cancel);
        layout1.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        layout1.setWidthFull();
        return layout1;
    }

    private Component layoutImput() {
        var form = new FormLayout();
        nombres.setClearButtonVisible(true);
        apellidos.setClearButtonVisible(true);
        telefono.setClearButtonVisible(true);
        form.add(nombres, apellidos, telefono);
        form.setSizeFull();
        return form;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        nombres.focus();
    }

    //BUILD EVENT TO DIALOG
    public static class SaverAthorEvent extends DialogViewEvent<AutorDTO, AutoresAbmView> {
        public SaverAthorEvent(AutoresAbmView source, AutorDTO author) {
            super(source, author);
        }
    }

    public Registration addSaveAthor(ComponentEventListener<SaverAthorEvent> listener) {
        return super.addListener(SaverAthorEvent.class, listener);
    }

    public static class DeleteAthorEvent extends DialogViewEvent<AutorDTO, AutoresAbmView> {
        public DeleteAthorEvent(AutoresAbmView source, AutorDTO author) {
            super(source, author);
        }
    }

    public Registration addDeleteAthor(ComponentEventListener<DeleteAthorEvent> listener) {
        return super.addListener(DeleteAthorEvent.class, listener);
    }
}
