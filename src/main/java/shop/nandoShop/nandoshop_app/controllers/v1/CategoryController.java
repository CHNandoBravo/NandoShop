package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.nandoShop.nandoshop_app.dtos.requests.CategoryRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.ApiResponse;
import shop.nandoShop.nandoshop_app.services.interfaces.CategoryService;

@Validated
@RestController
@RequestMapping("/v1")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest category) {
        try {
            categoryService.create(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("La categoría fue creada con exito", true));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("La categoría no pudo crearse con exito", true, e.getMessage()));
        }
    }
}
