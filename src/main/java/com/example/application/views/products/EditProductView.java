package com.example.application.views.products;

import com.example.application.data.entity.Product;
import com.example.application.data.service.ProductRepository;
import com.example.application.data.service.ProductService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

@PageTitle("EditProducts")
@Route(value = "product", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class EditProductView extends Div implements HasUrlParameter<String>, AfterNavigationObserver{

    private final ProductRepository repository;
    Product currentProduct = new Product();
    ProductService productService;
    String currentId;
    UUID id;

    private Binder<Product> binder = new Binder(Product.class);
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

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        this.currentId = parameter;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        try{
            id = UUID.fromString((this.currentId));
            if(repository.findById(this.id).isPresent()){
                currentProduct = repository.findById(this.id).get();
                binder.setBean(currentProduct);
            }

        } catch (NullPointerException e) {
            Notification.show("Product not found");
        }
    }


    public EditProductView(ProductService productService, ProductRepository repository){
        this.repository = repository;
        addClassName("edit-product-view");
        setSizeFull();
        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout(productService));
        initBinder();
        dollarPrefix.setText("$");
        dollarPrefix2.setText("$");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        cost.setPrefixComponent(dollarPrefix2);
        suggested_cell_price.setPrefixComponent(dollarPrefix);
        formLayout.add(name, sku, cost, suggested_cell_price, current_inventory, minimum_inventory);
        return formLayout;
    }

    private void initBinder(){

        binder.forField(name).withValidator(
                name -> name.length() > 1, "The name must contains at least 2 characters").asRequired()
                .bind(Product::getName, Product::setName);
        binder.forField(sku).withValidator(sku -> sku.length() >= 10 && sku.length() <= 20,
                "The SKU must contains at least 10 characters and maximum 20 characters").asRequired()
                .bind(Product::getSku, Product::setSku);
        binder.forField(cost).withValidator(cost -> cost > 0, "Cost must be different of Zero").asRequired()
                .bind(Product::getCost, Product::setCost);
        binder.forField(suggested_cell_price).withValidator(suggested_cell_price -> suggested_cell_price > 0,
                "Suggested cell price must be greater than Zero").asRequired()
                .bind(Product::getSuggestedCellPrice, Product::setSuggestedCellPrice);
        binder.forField(current_inventory).withValidator(current_inventory -> current_inventory >= 0,
                "Current inventory must be greater than zero").asRequired()
                .bind(Product::getCurrentInventory, Product::setCurrentInventory);
        binder.forField(minimum_inventory).withValidator(minimum_inventory -> minimum_inventory >= 0,
                        "Current inventory must be greater than zero").asRequired()
                .bind(Product::getMinimumInventory, Product::setMinimumInventory);
    }

    private Component createTitle() {
        return new H2("Edit Product information");
    }

    private Component createButtonLayout(ProductService productService) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addClickListener(e -> cancel.getUI().ifPresent(ui ->
                ui.navigate("products")));
        save.addClickListener(e -> {
            productService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " edited.");
            save.getUI().ifPresent(ui ->
                    ui.navigate("products"));
        });
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        buttonLayout.addClassName("button-layout");
        return buttonLayout;
    }
}