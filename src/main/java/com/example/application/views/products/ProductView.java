package com.example.application.views.products;


import com.example.application.data.entity.Product;
import com.example.application.data.service.ProductService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.crud.BinderCrudEditor;

import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
@RolesAllowed("USER")
public class ProductView extends Div {

    Span status = new Span();
    Grid <Product> grid= new Grid<>(Product.class);
    VerticalLayout layout = new VerticalLayout();
    H1 title = new H1("Product Table");
    Button createButton = new Button("Add Product", new Icon(VaadinIcon.PLUS));
    Button modButton = new Button("", new Icon(VaadinIcon.EDIT));
    Button deleteButton = new Button("Delete Product", new Icon(VaadinIcon.TRASH));
    HorizontalLayout header = new HorizontalLayout(title, createButton, modButton, deleteButton);

    public ProductView(ProductService productService){
        addClassName("product-view");
        setSizeFull();
        add(createLayout());
        status.setVisible(false);
        grid.setItems(productService.list());
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, product) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> productService.delete(product.getId()));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        modButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(createButton);
        buttonLayout.add(modButton);
        buttonLayout.add(deleteButton);
        createButton.addClickListener(e ->
                createButton.getUI().ifPresent(ui ->
                        ui.navigate("addProducts"))
        );
        return buttonLayout;
    }

    private Component createLayout(){
        layout.add(header);
        layout.add(grid);
        return layout;
    }

    private Grid<Product> createGrid(){

        return grid;
    }

    private CrudEditor<Product> createEditor(){
        TextField name = new TextField("Product Name");
        TextField sku = new TextField("SKU");
        NumberField cost = new NumberField("Cost");
        NumberField suggested_cell_price = new NumberField("Suggested Cell Price");
        IntegerField current_inventory = new IntegerField("Current Inventory");
        IntegerField minimum_inventory = new IntegerField("Minimum Inventory");
        FormLayout form = new FormLayout(name, sku, cost, suggested_cell_price,
                current_inventory, minimum_inventory);

        Binder<Product> binder = new Binder(Product.class);

        binder.forField(name).asRequired().bind(Product::getName, Product::setName);
        binder.forField(sku).asRequired().bind(Product::getSku, Product::setSku);
        binder.forField(cost).asRequired().bind(Product::getCost, Product::setCost);
        binder.forField(suggested_cell_price).asRequired().bind(Product::getSuggestedCellPrice,
                Product::setSuggestedCellPrice);
        binder.forField(current_inventory).asRequired().bind(Product::getCurrentInventory,
                Product::setCurrentInventory);
        binder.forField(minimum_inventory).asRequired().bind(Product::getMinimumInventory,
                Product::setMinimumInventory);

        return new BinderCrudEditor<>(binder, form);
    }

    public void ConfirmDelete() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        status = new Span();
        status.setVisible(false);

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Are you sure");
        dialog.setText("Do you want to delete this item?");

        dialog.setRejectable(true);
        dialog.setRejectText("Discard");
        dialog.addRejectListener(event -> setStatus("Discarded"));

        dialog.setConfirmText("Save");
        dialog.addConfirmListener(event -> setStatus("Saved"));

        Button button = new Button("Open confirm dialog");
        button.addClickListener(event -> {
            dialog.open();
            status.setVisible(false);
        });

        dialog.open();

        layout.add(button, status);
        add(layout);

        // Center the button within the example
        getStyle().set("position", "fixed").set("top","0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }


    private void setStatus(String value) {
            status.setText("Status: " + value);
            status.setVisible(true);

    }

}
