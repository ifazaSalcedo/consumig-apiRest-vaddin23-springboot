package com.apirest.views.libros;

import com.apirest.genericlass.DialogViewEvent;
import com.apirest.restdata.AutorDTO;
import com.apirest.restdata.LibroDTO;
import com.apirest.restservices.AutorRestService;
import com.apirest.restservices.LibroRestService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibroAbmView extends Dialog {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibroAbmView.class);
    private Binder<LibroDTO> binder = new Binder<>();
    private ComboBox<AutorDTO> author = new ComboBox<>("Autores");
    private TextField isbn = new TextField("Isbn");
    private TextField titleBook = new TextField("Titulo");
    private TextField editorial = new TextField("Editorial");
    private TextField genero = new TextField("Género");
    private IntegerField numberPage = new IntegerField("Número Página");
    private DatePicker editionDate = new DatePicker("Fecha Edición");
    private BigDecimalField price = new BigDecimalField("Precio");
    private Button save = new Button("Grabar");
    private Button cancel = new Button("Cancelar");
    private Button delete = new Button("Eliminar");
    private final AutorRestService autorRestService;
    private final LibroRestService libroRestService;

    public LibroAbmView(AutorRestService autorRestService, LibroRestService libroRestService) {

        this.autorRestService = autorRestService;
        this.libroRestService = libroRestService;

        mainDesign();

        carryListAuthors();//carry list athors

        //ACTION EVENT BUTTON
        save.addClickListener(event -> {
            try {
                saveBook();
            } catch (RuntimeException ex) {
                LOGGER.error(ex.getMessage());
            }

        });
        delete.addClickListener(event -> {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Eliminar Registro");
            confirmDialog.setText("Desea eliminar el registro seleccionado?");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("Cancelar");
            confirmDialog.addCancelListener(cancelEvent -> confirmDialog.close());

            confirmDialog.setRejectable(true);
            confirmDialog.setRejectText("Descartar");
            confirmDialog.addRejectListener(rejectEvent -> close());

            confirmDialog.setConfirmButton("Eliminar", event1 -> {
                deleteBook();
                fireEvent((new DeleteBookEvent(this, binder.getBean())));
            });

            confirmDialog.open();
        });

        cancel.addClickListener(event -> {
            close();
        });

        //CONFIGURE BINDER
        binder.forField(author)
                .asRequired("Campo autor es requerido")
                .bind(LibroDTO::getAutorDTO, LibroDTO::setAutorDTO);
        binder.forField(isbn)
                .asRequired("Campos isbn es requerido")
                .bind(LibroDTO::getIsbn, LibroDTO::setIsbn);
        binder.forField(titleBook)
                .asRequired("Campo titulo es requerido")
                .bind(LibroDTO::getNombre, LibroDTO::setNombre);
        binder.forField(editorial)
                .bind(LibroDTO::getEditorial, LibroDTO::setEditorial);
        binder.forField(genero)
                .bind(LibroDTO::getGenero, LibroDTO::setGenero);
        binder.forField(numberPage)
                .bind(LibroDTO::getNumeroPaginas, LibroDTO::setNumeroPaginas);
        binder.forField(editionDate)
                .bind(LibroDTO::getFechaEdicion, LibroDTO::setFechaEdicion);
        binder.forField(price)
                .bind(LibroDTO::getPrecio, LibroDTO::setPrecio);

        binder.addStatusChangeListener(statusChangeEvent -> {
            if (binder.getBean() != null) {
                if (!statusChangeEvent.hasValidationErrors()) {
                    save.setEnabled(!statusChangeEvent.hasValidationErrors());
                }
                delete.setEnabled(binder.getBean().getCodigo() != null);
            }
        });
    }

    private void deleteBook() {
        try {
            libroRestService.deleteById(binder.getBean());
        } catch (Exception ex) {

        }
    }

    private void saveBook() throws RuntimeException {
        if (binder.writeBeanIfValid(binder.getBean())) {
            binder.setBean(
                    libroRestService.save(binder.getBean())
            );
            fireEvent(new SaveBookEvent(this, binder.getBean()));
        }
    }

    public void setLibroDTO(LibroDTO libroDTO) {
        binder.setBean(libroDTO);
    }

    private void mainDesign() {
        setHeaderTitle("Libros");
        add(layoutHeader());
        getFooter().add(layoutFooter());
        setWidth("500px");
    }

    private Component layoutFooter() {
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        var buttons1 = new HorizontalLayout();
        buttons1.add(delete);
        buttons1.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buttons1.setWidthFull();

        var buttons2 = new HorizontalLayout();
        buttons2.add(buttons1, save, cancel);
        buttons2.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        buttons2.setWidthFull();
        return buttons2;
    }

    private Component layoutHeader() {
        var form = new FormLayout();
        author.setClearButtonVisible(true);
        author.setItemLabelGenerator(autorDTO ->
                //custon display combo
                autorDTO.getNombres() + " "
                        + autorDTO.getApellidos()
        );
        isbn.setClearButtonVisible(true);
        titleBook.setClearButtonVisible(true);
        editionDate.setClearButtonVisible(true);
        editorial.setClearButtonVisible(true);
        numberPage.setClearButtonVisible(true);
        price.setClearButtonVisible(true);
        form.add(author, isbn, titleBook, editionDate, editorial, genero, numberPage, price);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("500px", 1),
                new FormLayout.ResponsiveStep("0", 1)
        );
        return form;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        author.focus();
    }

    private void carryListAuthors() {
        author.setItems(autorRestService.findAll());
    }

    //CONSTRUIR OYENTE PARA EVENTO DE BOTONES DEL FORMULARIO
    //PERMITE CONTROLAR LOS EVENTOS GENERADOS POR LOS BOTONES DESDE OTRA VISTA

    public static class SaveBookEvent extends DialogViewEvent<LibroDTO, LibroAbmView> {
        public SaveBookEvent(LibroAbmView source, LibroDTO libroDTO) {
            super(source, libroDTO);
        }
    }

    public Registration addSaveBookEventEventListener(ComponentEventListener<SaveBookEvent> listener) {
        return addListener(SaveBookEvent.class, listener);
    }

    public static class DeleteBookEvent extends DialogViewEvent<LibroDTO, LibroAbmView> {
        public DeleteBookEvent(LibroAbmView source, LibroDTO libroDTO) {
            super(source, libroDTO);
        }
    }

    public Registration addDeleteBookEventEventListener(ComponentEventListener<DeleteBookEvent> listener) {
        return addListener(DeleteBookEvent.class, listener);
    }
}
