package com.example.application.views.products;


import com.example.application.data.entity.Product;
import com.example.application.data.service.ProductService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.crud.BinderCrudEditor;

import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
@RolesAllowed("USER")
public class ProductView extends Div {

    Span status = new Span();
    String id;
    Grid <Product> grid= new Grid<>(Product.class);
    Editor<Product> editor = grid.getEditor();
    VerticalLayout layout = new VerticalLayout();
    H1 title = new H1("Product Table");
    Dialog dialog = new Dialog();



    public ProductView(ProductService productService){
        addClassName("product-view");
        setSizeFull();
        add(createLayout());
        status.setVisible(false);
        dialog.getElement()
                .setAttribute("aria-label", "Are you sure?");
        VerticalLayout dialogLayout = new VerticalLayout();
        dialog.add(dialogLayout);
        add(dialog);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(productService.list());
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, product) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> deleteProduct(productService, product));
                    button.setIcon(new Icon(VaadinIcon.TRASH));

                })).setHeader("Delete");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (buttonEdit, product) -> {
                    buttonEdit.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_PRIMARY,
                            ButtonVariant.LUMO_TERTIARY);
                    buttonEdit.addClickListener(e -> buttonEdit.getUI().ifPresent(ui ->
                            ui.navigate("product/" + product.getId() + "/edit")));
                    buttonEdit.setIcon(new Icon(VaadinIcon.EDIT));
                })).setHeader("Edit");

    }

    private Component createHeaderLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        Button createButton = new Button("Add Product", new Icon(VaadinIcon.PLUS));
        Button modButton = new Button("", new Icon(VaadinIcon.EDIT));
        modButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout header = new HorizontalLayout(title, createButton);
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(createButton);
        createButton.addClickListener(e ->
                createButton.getUI().ifPresent(ui ->
                        ui.navigate("addProducts"))
        );
        return buttonLayout;
    }

    private Component createLayout(){
        layout.add(createHeaderLayout());
        layout.add(grid);
        return layout;
    }

    private Grid<Product> createGrid(){

        return grid;
    }

    private void deleteProduct(ProductService productService, Product product){
        VerticalLayout dialogDelete = createDialogLayout(dialog);
        add(dialogDelete);
        productService.delete(product.getId());
        grid.setItems(productService.list());
        Notification.show("Product deleted.");
    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
        H2 headline = new H2("Are you sure?");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold");
        HorizontalLayout header = new HorizontalLayout(headline);
        header.getElement().getClassList().add("draggable");
        header.setSpacing(false);
        header.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("cursor", "move");
        // Use negative margins to make draggable header stretch over full width,
        // covering the padding of the dialog
        header.getStyle()
                .set("padding", "var(--lumo-space-m) var(--lumo-space-l)")
                .set("margin",
                        "calc(var(--lumo-space-s) * -1) calc(var(--lumo-space-l) * -1) 0");

        Paragraph paragraph = new Paragraph(
                "Do you want to delete this item?");
        VerticalLayout fieldLayout = new VerticalLayout(paragraph);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button confirmButton = new Button("Delete", e -> dialog.close());
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                confirmButton);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(header, fieldLayout,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }



}
