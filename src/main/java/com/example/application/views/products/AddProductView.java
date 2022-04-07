package com.example.application.views.products;

import com.example.application.data.entity.Product;
import com.example.application.data.service.ProductService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("AddProducts")
@Route(value = "addProducts", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class AddProductView extends Div {

    private TextField name = new TextField("Product Name");
    private TextField sku = new TextField("SKU");
    private NumberField cost = new NumberField("Cost");
    private NumberField suggested_cell_price = new NumberField("Suggested Cell Price");
    private IntegerField current_inventory = new IntegerField("Current Inventory");
    private IntegerField minimum_inventory = new IntegerField("Minimum Inventory");
    Div dollarPrefix = new Div();
    Div dollarPrefix2 = new Div();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Product> binder = new Binder(Product.class);

    public AddProductView(ProductService productService){
        addClassName("add-product-view");
        setSizeFull();

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        dollarPrefix.setText("$");
        dollarPrefix2.setText("$");

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            productService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " saved.");
            clearForm();
        });
    }

    private void clearForm() {
        binder.setBean(new Product());
    }

    private Component createTitle() {
        return new H3("Product information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        cost.setPrefixComponent(dollarPrefix);
        suggested_cell_price.setPrefixComponent(dollarPrefix);
        current_inventory.setMin(0);
        minimum_inventory.setMin(0);
        formLayout.add(name, sku, cost, suggested_cell_price, current_inventory, minimum_inventory);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

}
