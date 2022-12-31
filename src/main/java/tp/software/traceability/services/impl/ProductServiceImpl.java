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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class  ProductServiceImpl implements ProductService {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    private final GenerateUtils generateUtils;

    @Override
    public List<ProductDto> getAllProducts() {
        ModelMapper modelMapper = new ModelMapper();
        logger.info("Getting all products");
        return productRepository.findAll().stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> {
            throw new ProductServiceException("Product not found");
        });
        logger.info("Getting product by id: {}",  id);
        return modelMapper.map(productEntity, ProductDto.class);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        ModelMapper modelMapper = new ModelMapper();
        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        productEntity.setId(generateUtils.generateNumericProductId(3));
        ProductEntity savedProduct = productRepository.save(productEntity);
        logger.info("Creating product: {}", productDto.getName());
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        ModelMapper modelMapper = new ModelMapper();
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product not found");
            throw new ProductServiceException("Product not found");
        });
        productEntity.setName(productDto.getName());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setExpirationDate(productDto.getExpirationDate());
        ProductEntity updatedProduct = productRepository.save(productEntity);
        logger.info("Updating product: {}", productDto.getName());
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product not found");
            throw new ProductServiceException("Product not found");
        });
        logger.info("Deleting product: {}", productEntity.getName());
        productRepository.delete(productEntity);
    }
}
