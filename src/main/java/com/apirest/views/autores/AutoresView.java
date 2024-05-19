package com.apirest.views.autores;

import com.apirest.restdata.AutorDTO;
import com.apirest.restservices.AutorRestService;
import com.apirest.views.MainLayout;
import com.vaadin.flow.component.AttachEvent;
<<<<<<< HEAD
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
=======
>>>>>>> 5319a24 (Inicio de Seguimiento)
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Autores")
@Route(value = "autores-lista", layout = MainLayout.class)
public class AutoresView extends VerticalLayout {

    private AutorRestService autorRestService;
<<<<<<< HEAD
    private Button addAutor= new Button("Nuevo Registro");
    private Grid<AutorDTO> autorDTOGrid= new Grid<>();
    private AutoresAbmView formAutor;

    public AutoresView(@Autowired AutorRestService autorRestService) {

        this.autorRestService= autorRestService;

        add(addAutor);

        layoutDetails();

        configureFormAutor();

=======
    private Grid<AutorDTO> autorDTOGrid= new Grid<>();

    public AutoresView(@Autowired AutorRestService autorRestService) {
        this.autorRestService= autorRestService;

>>>>>>> 5319a24 (Inicio de Seguimiento)
        setSpacing(false);

        setSizeFull();

<<<<<<< HEAD

        //ACCION BOTON AGREGAR AUTOR
        addAutor.addClickListener(this::openDialogAutor);

    }

    private void openDialogAutor(ClickEvent<Button> event) {
        formAutor.open();
    }

    private void configureFormAutor() {
        formAutor= new AutoresAbmView(autorRestService);
=======
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

        layoutDetails();

        super.onAttach(attachEvent);

>>>>>>> 5319a24 (Inicio de Seguimiento)
    }

    private void layoutDetails() {
        autorDTOGrid.setSizeFull();

        autorDTOGrid.addColumn(AutorDTO::getNombres)
                .setHeader("NOMBRES");
        autorDTOGrid.addColumn(AutorDTO::getApellidos)
                .setHeader("APELLIDOS");
        autorDTOGrid.addColumn(AutorDTO::getTelefono)
                .setHeader("TELEFONO");

        autorDTOGrid.setItems(autorRestService.findAll());

        add(autorDTOGrid);

    }
}
