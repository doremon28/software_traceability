package tp.software.traceability.ui.gui.controllers.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tp.software.traceability.exceptions.ProductServiceException;
import tp.software.traceability.ui.gui.controllers.ProductController;
import tp.software.traceability.ui.gui.controllers.graphics.utils.ProductModel;
import tp.software.traceability.ui.models.requests.ProductRequest;
import tp.software.traceability.ui.models.responses.ProductResponse;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ProductUiController implements Initializable {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProductUiController.class);

    private final Resource productUiResource;
    private final String productUiTitle;
    private final ApplicationContext applicationContext;
    private final ProductController productController;

    public ProductUiController(@Value("classpath:/product.fxml") Resource productUiResource,
                               @Value("${spring.application.ui.product.title}") String productUiTitle,
                               ApplicationContext applicationContext,
                               ProductController productController) {
        this.productUiResource = productUiResource;
        this.productUiTitle = productUiTitle;
        this.applicationContext = applicationContext;
        this.productController = productController;
    }

    @FXML
    public TextField txt_nom;

    @FXML
    public TextField txt_prix;

    @FXML
    public DatePicker txt_date;

    @FXML
    public Button btn_addProduct;

    @FXML
    public TableView<ProductModel> table_product;

    @FXML
    public TableColumn<ProductModel, String> columnNom;

    @FXML
    public TableColumn<ProductModel, String> columnPrice;

    @FXML
    public TableColumn<ProductModel, String> columnDate;

    @FXML
    public TableColumn<ProductModel, Long> columnId;

    @FXML
    public TextField txt_op_id;

    @FXML
    public TextField txt_op_nom;

    @FXML
    public TextField txt_op_price;

    @FXML
    public DatePicker txt_op_date;

    @FXML
    public Button btn_update;

    @FXML
    public Button btn_delete;

    @FXML
    public RadioButton radio_update;

    @FXML
    public RadioButton radio_delete;

    private ObservableList<ProductModel> productsModels = FXCollections.observableArrayList();


    @FXML
    public void onHandleAddNewProduct() {
        try {
            LOGGER.info("Add new product");
            String nom = txt_nom.getText();
            double prix = Double.parseDouble(txt_prix.getText());
            String dateStringFormat = txt_date.getValue().toString();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringFormat);
            LOGGER.info("Nom: " + nom + " Prix: " + prix + " Date: " + date);
            if (nom.isEmpty() || prix == 0 || date == null) {
                LOGGER.info("Empty fields");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Empty fields", ButtonType.OK);
                alert.showAndWait();
            } else {
                LOGGER.info("Add new product");
                ProductRequest productRequest = ProductRequest.builder()
                        .name(nom)
                        .price(prix)
                        .expirationDate(date)
                        .build();
                ProductResponse productCreated = productController.createProduct(productRequest);
                if (productCreated != null) {
                    LOGGER.info("Product created");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product created", ButtonType.OK);
                    alert.showAndWait();
                    clearFields();
                    loadProducts();
                } else {
                    LOGGER.error("Product not created");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product not created", ButtonType.OK);
                    alert.showAndWait();
                    clearFields();
                }
            }
        } catch (ProductServiceException e) {
            LOGGER.error("Error while adding new product : {} ", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        } catch (ParseException e) {
            LOGGER.error("Error while parsing date {} :", e.getMessage());
        }
    }

    private void clearFields() {
        txt_nom.setText("");
        txt_prix.setText("");
        txt_date.setValue(LocalDate.now());
        txt_op_date.setValue(LocalDate.now());
        txt_op_id.setText("");
        txt_op_nom.setText("");
        txt_op_price.setText("");
        btn_update.setDisable(true);
        btn_delete.setDisable(true);
        radio_update.setSelected(false);
        radio_delete.setSelected(false);
        txt_op_id.setDisable(false);
        txt_op_nom.setDisable(true);
        txt_op_price.setDisable(true);
        txt_op_date.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup group = new ToggleGroup();
        radio_delete.setToggleGroup(group);
        radio_update.setToggleGroup(group);
        columnNom.setCellValueFactory(cellData -> cellData.getValue().getName());
        columnPrice.setCellValueFactory(cellData -> cellData.getValue().getPrice().asString());
        columnDate.setCellValueFactory(cellData -> cellData.getValue().getExpirationDate());
        columnId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        loadProducts();
        table_product.setItems(productsModels);
    }

    @FXML
    public void onBtnSearchHandler() {
        try {
            Long id = Long.parseLong(txt_op_id.getText());
            ProductResponse productResponse = productController.getProductById(id);
            if (productResponse != null) {
                txt_op_nom.setText(productResponse.getName());
                txt_op_price.setText(String.valueOf(productResponse.getPrice()));

                txt_op_date.setValue(convertToLocalDateViaInstant(productResponse.getExpirationDate()));
                LOGGER.info("Product found");
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product not found", ButtonType.OK);
                alert.showAndWait();
            }

        } catch (ProductServiceException e) {
            LOGGER.error("Error while searching product : {}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }

    }

    private void loadProducts() {
        List<ProductResponse> products = productController.getAllProducts();
        productsModels.clear();
        products.forEach(product -> {
            ProductModel productModel = new ProductModel(product.getId(), product.getName(), product.getPrice(),
                    convertToLocalDateViaInstant(product.getExpirationDate()));
            productsModels.add(productModel);
        });
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @FXML
    public void onUpdateActivation() {
        if (radio_update.isSelected()) {
            btn_update.setDisable(false);
            btn_delete.setDisable(true);
            txt_op_id.setDisable(true);
            txt_op_nom.setDisable(false);
            txt_op_price.setDisable(false);
            txt_op_date.setDisable(false);
        } else {
            btn_update.setDisable(true);
            btn_delete.setDisable(false);
        }
    }

    @FXML
    public void onDeleteActivation() {
        if (radio_delete.isSelected()) {
            btn_delete.setDisable(false);
            btn_update.setDisable(true);
        } else {
            btn_delete.setDisable(true);
            btn_update.setDisable(false);
        }
    }

    @FXML
    public void onBtnDeleteHandler() {
        try {
            Long id = Long.parseLong(txt_op_id.getText());
            productController.deleteProduct(id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product deleted", ButtonType.OK);
            alert.showAndWait();
            clearFields();
            loadProducts();
        } catch (ProductServiceException e) {
            LOGGER.error("Error while deleting product : {}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void onBtnUpdateHandler() {
        try {
            Long id = Long.parseLong(txt_op_id.getText());
            String nom = txt_op_nom.getText();
            double prix = Double.parseDouble(txt_op_price.getText());
            String dateStringFormat = txt_op_date.getValue().toString();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringFormat);
            LOGGER.info("Nom: " + nom + " Prix: " + prix + " Date: " + date);
            if (nom.isEmpty() || prix == 0 || date == null) {
                LOGGER.info("Empty fields");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Empty fields", ButtonType.OK);
                alert.showAndWait();
            } else {
                LOGGER.info("Update product");
                ProductRequest productRequest = ProductRequest.builder()
                        .name(nom)
                        .price(prix)
                        .expirationDate(date)
                        .build();
                ProductResponse productUpdated = productController.updateProduct(id, productRequest);
                if (productUpdated != null) {
                    LOGGER.info("Product updated");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product updated", ButtonType.OK);
                    alert.showAndWait();
                    clearFields();
                    loadProducts();
                } else {
                    LOGGER.error("Product not updated");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product not updated", ButtonType.OK);
                    alert.showAndWait();
                    clearFields();
                }
            }
        } catch (ProductServiceException e) {
            LOGGER.error("Error while updating product : {}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        } catch (ParseException e) {
            LOGGER.error("Error while parsing date : {} ", e.getMessage());
        }
    }
}
