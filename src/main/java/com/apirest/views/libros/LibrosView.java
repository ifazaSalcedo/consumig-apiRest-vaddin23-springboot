package com.apirest.views.libros;

import com.apirest.restdata.LibroDTO;
import com.apirest.restservices.AutorRestService;
import com.apirest.restservices.LibroRestService;
import com.apirest.views.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Libros")
@Route(value = "libros-autores", layout = MainLayout.class)
public class LibrosView extends VerticalLayout {

    private Grid<LibroDTO> gridLibrosDTO= new Grid<>();
    private LibroAbmView formBook;
    private Button addButtonBook= new Button("Agregar Nuevo Libro");
    private LibroRestService libroRestService;
    private AutorRestService autorRestService;

    public LibrosView(@Autowired LibroRestService libroRestService, @Autowired AutorRestService autorRestService) {

        this.libroRestService= libroRestService;
        this.autorRestService= autorRestService;

        configureFormBook();

        setSizeFull();
        setSpacing(false);


        //CONFIGURE EVENT BUTTONS
        addButtonBook.addClickListener(e -> addNewBook());

    }

    private void configureFormBook() {
        formBook= new LibroAbmView(autorRestService, libroRestService);
        formBook.addSaveBookEventEventListener(this::trueSaveBook);
        formBook.addDeleteBookEventEventListener(this::trueDeleteBook);
    }

    private void trueDeleteBook(LibroAbmView.DeleteBookEvent deleteBookEvent) {
        setItemBookList();
        closeFormBook();
    }

    private void trueSaveBook(LibroAbmView.SaveBookEvent saveBookEvent) {
        if(saveBookEvent != null){
            setItemBookList();
            closeFormBook();
        }
    }

    private void addNewBook() {
        LibroDTO book= new LibroDTO();
        editBook(book);
    }

    private void editBook(LibroDTO book) {
        if(book==null){
            closeFormBook();
        }else{
            formBook.setLibroDTO(book);
            formBook.open();
        }
    }

    private void closeFormBook() {
        formBook.setLibroDTO(null);
        formBook.close();
    }

    private void layoutDetails() {
        gridLibrosDTO.setSizeFull();

        gridLibrosDTO.addColumn(new ComponentRenderer<>(book -> {
            Button buttonEdit = new Button("Editar");
            buttonEdit.addClickListener(e -> editBook(book));
            return buttonEdit;
        }));

        gridLibrosDTO.addColumn(LibroDTO::getAutorDTO)
                .setHeader("AUTOR");
        gridLibrosDTO.addColumn(LibroDTO::getIsbn)
                .setHeader("ISBN");
        gridLibrosDTO.addColumn(LibroDTO::getNombre)
                .setHeader("TITULO");
        gridLibrosDTO.addColumn(LibroDTO::getNumeroPaginas)
                .setHeader("NUMERO PAGINAS");
        gridLibrosDTO.addColumn(LibroDTO::getFechaEdicion)
                .setHeader("FECHA EDICION");
        gridLibrosDTO.addColumn(LibroDTO::getGenero)
                .setHeader("GENERO");
        gridLibrosDTO.addColumn(LibroDTO::getEditorial)
                .setHeader("EDITORIAL");
        gridLibrosDTO.addColumn(LibroDTO::getPrecio)
                        .setHeader("COSTO");

        add(addButtonBook,gridLibrosDTO);

        setItemBookList();

    }

    private void setItemBookList() {
        gridLibrosDTO.setItems(libroRestService.findAll());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        layoutDetails();
    }


}
