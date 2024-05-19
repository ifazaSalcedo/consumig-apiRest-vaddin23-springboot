package com.apirest.views.autores;

import com.apirest.restdata.AutorDTO;
import com.apirest.restservices.AutorRestService;
import com.apirest.views.MainLayout;
import com.vaadin.flow.component.AttachEvent;
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
    private Grid<AutorDTO> autorDTOGrid= new Grid<>();

    public AutoresView(@Autowired AutorRestService autorRestService) {
        this.autorRestService= autorRestService;

        setSpacing(false);

        setSizeFull();

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

        layoutDetails();

        super.onAttach(attachEvent);

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
