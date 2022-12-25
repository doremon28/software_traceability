package tp.software.traceability.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.software.traceability.exceptions.ProductServiceException;
import tp.software.traceability.io.entities.ProductEntity;
import tp.software.traceability.io.repositories.ProductRepository;
import tp.software.traceability.services.ProductService;
import tp.software.traceability.shared.dto.ProductDto;
import tp.software.traceability.shared.utils.GenerateUtils;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final GenerateUtils generateUtils;

    @Override
    public List<ProductDto> getAllProducts() {
        ModelMapper modelMapper = new ModelMapper();
        LOGGER.info("Getting all products");
        return productRepository.findAll().stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        LOGGER.info("Getting product by id: {}", id);
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Product with id: {} not found", id);
            throw new ProductServiceException("Product not found");
        });
        LOGGER.info("Product with id: {} found", id);
        return modelMapper.map(productEntity, ProductDto.class);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        ModelMapper modelMapper = new ModelMapper();
        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        productEntity.setId(generateUtils.generateNumericProductId(10));
        LOGGER.info("Creating product with id: {}", productEntity.getId());
        ProductEntity savedProduct = productRepository.save(productEntity);
        LOGGER.info("Product with id: {} created", savedProduct.getId());
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        ModelMapper modelMapper = new ModelMapper();
        LOGGER.info("Updating product with id: {}", id);
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Product with id: {} not found", id);
            throw new ProductServiceException("Product not found");
        });
        productEntity.setName(productDto.getName());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setExpirationDate(productDto.getExpirationDate());
        ProductEntity updatedProduct = productRepository.save(productEntity);
        LOGGER.info("Product with id: {} updated", updatedProduct.getId());
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        LOGGER.info("Deleting product with id: {}", id);
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Product with id: {} not found", id);
            throw new ProductServiceException("Product not found");
        });
        productRepository.delete(productEntity);
        LOGGER.info("Product with id: {} deleted", id);
    }
}
