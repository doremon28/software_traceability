package tp.software.traceability.ui.gui.controllers;

import tp.software.traceability.ui.models.requests.ProductRequest;
import tp.software.traceability.ui.models.responses.ProductResponse;

public interface ProductController {
    ProductResponse getProductById(Long idProduct);
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long idProduct);
}
