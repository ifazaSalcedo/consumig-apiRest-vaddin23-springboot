package com.apirest.views.autores;

import com.apirest.restdata.AutorDTO;
import com.apirest.restservices.AutorRestService;
import com.apirest.views.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnPathRenderer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Autores")
@Route(value = "autores-lista", layout = MainLayout.class)
public class AutoresView extends VerticalLayout {

    private AutorRestService autorRestService;
    private Button addAutor = new Button("Nuevo Registro");
    private Grid<AutorDTO> autorDTOGrid = new Grid<>();
    private AutoresAbmView formAutor;

    public AutoresView(@Autowired AutorRestService autorRestService) {
        this.autorRestService = autorRestService;


        configureFormAutor();
        setSpacing(false);

        setSizeFull();

        //ACCION BOTON AGREGAR AUTOR
        addAutor.addClickListener(e -> addNewAthor());

    }

    private void addNewAthor() {
        autorDTOGrid.asSingleSelect().clear();
        AutorDTO author = new AutorDTO();
        editAthor(author);
    }

    private void editAthor(AutorDTO author) {
        if (author == null) {
            closeFormAthor();
        } else {
            formAutor.setAthor(author);
            formAutor.open();
        }
    }

    private void closeFormAthor() {
        formAutor.close();
    }

    private void configureFormAutor() {
        formAutor = new AutoresAbmView(autorRestService);
        formAutor.addSaveAthor(this:: ifPersitsAthor);
        formAutor.addDeleteAthor(this:: isDeletedAthor);

    }

    private void isDeletedAthor(AutoresAbmView.DeleteAthorEvent deleteAthorEvent) {
        setAthorsList();
        formAutor.close();
    }

    private void ifPersitsAthor(AutoresAbmView.SaverAthorEvent saverAthorEvent) {
        if (saverAthorEvent.getObjet() != null){
            setAthorsList();
            formAutor.close();
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        layoutDetails();
        super.onAttach(attachEvent);
    }

    private void layoutDetails() {

        autorDTOGrid.setSizeFull();
        autorDTOGrid.addColumn(new ComponentRenderer<>(e -> {
            Button editAthor = new Button("Editar", event -> {
                editAthor(e);
            });
            return editAthor;
        }));
        autorDTOGrid.addColumn(AutorDTO::getNombres)
                .setHeader("NOMBRES");
        autorDTOGrid.addColumn(AutorDTO::getApellidos)
                .setHeader("APELLIDOS");
        autorDTOGrid.addColumn(AutorDTO::getTelefono)
                .setHeader("TELEFONO");

        setAthorsList();

        add(addAutor, autorDTOGrid);

    }

    private void setAthorsList() {
        autorDTOGrid.setItems(autorRestService.findAll());
    }
}
