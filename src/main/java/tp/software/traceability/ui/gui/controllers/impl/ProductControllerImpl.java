package tp.software.traceability.ui.gui.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.software.traceability.services.ProductService;
import tp.software.traceability.shared.dto.ProductDto;
import tp.software.traceability.ui.gui.controllers.ProductController;
import tp.software.traceability.ui.models.requests.ProductRequest;
import tp.software.traceability.ui.models.responses.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductControllerImpl implements ProductController {
    private final ProductService productService;

    @Override
    public ProductResponse getProductById(Long idProduct) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productService.getProductById(idProduct), ProductResponse.class);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productService.createProduct(modelMapper.map(productRequest, ProductDto.class)), ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productService.updateProduct(id, modelMapper.map(productRequest, ProductDto.class)), ProductResponse.class);
    }

    @Override
    public void deleteProduct(Long idProduct) {
        productService.deleteProduct(idProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        ModelMapper modelMapper = new ModelMapper();
        return productService.getAllProducts()
                .stream()
                .map(productDto -> modelMapper.map(productDto, ProductResponse.class))
                .collect(Collectors.toList());
    }
}
