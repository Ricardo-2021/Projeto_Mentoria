package dbservermentoria.Teste.Controller;

import dbservermentoria.Teste.Model.Categorias;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Stream;


@RestController
@RequestMapping("/v1/categorias")
public class CategoriasController {

    private ArrayList<Categorias> categoriasArrayList = new ArrayList<>();

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Categorias categorias) {
        categoriasArrayList.add(categorias);
        return new ResponseEntity<>("Categoria cadastrado", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getCat() {
        return new ResponseEntity<>(categoriasArrayList, HttpStatus.OK);
    }

    @DeleteMapping("/{/id}")
    public ResponseEntity<?> delete(@PathVariable Integer categoriaId) {
        Categorias categoria;
        for ( Categorias categoriaDoCarro : categoriasArrayList) {
            if(categoriaDoCarro.getId() == categoriaId){
                categoria = categoriaDoCarro;
                categoriasArrayList.remove((categoriaDoCarro));
                return new ResponseEntity<>( categoria , HttpStatus.FOUND);
            }
        }
        return new ResponseEntity<>("Categoria não encontrada", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<?> put(@RequestBody Categorias categoriasBody, @PathVariable Integer categoriaId){
        for ( Categorias categoriaDoCarro : categoriasArrayList ){
            if (categoriaDoCarro.getId() == categoriaId){
                BeanUtils.copyProperties(categoriasBody, categoriaDoCarro, "categoriaId");
                return new ResponseEntity<>(categoriaDoCarro, HttpStatus.FOUND);
            }
        }
        return new ResponseEntity<>("Categoria não encontrada", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{categoriaId}")
    public ResponseEntity<?> patch(@RequestBody Categorias categoriasBody, @PathVariable Integer categoriaId){
        for (Categorias categoriaDoCarro : categoriasArrayList ) {
            if (categoriaDoCarro.getId() == categoriaId) {
                BeanUtils.copyProperties(completarCampo(categoriasBody, categoriaDoCarro), categoriaDoCarro, "categoriaId");
                return new ResponseEntity<>(categoriaDoCarro, HttpStatus.FOUND);
            }
        }
        return new ResponseEntity<>("Categoria não encontrada", HttpStatus.NOT_FOUND);
    }

    public Categorias completarCampo(Categorias categoriasBody, Categorias categoriaDoCarro){
        Stream<Field> fields = Stream.of(categoriaDoCarro.getClass().getDeclaredFields());
        fields.forEach(field -> {
            try {
                field.setAccessible(true);
                if (field.get(categoriasBody) == null)
                    field.set(categoriasBody, field.get(categoriaDoCarro));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return categoriasBody;
    }
}
