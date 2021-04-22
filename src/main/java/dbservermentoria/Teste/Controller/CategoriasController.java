package dbservermentoria.Teste.Controller;

import dbservermentoria.Teste.Model.Carros;
import dbservermentoria.Teste.Model.Categorias;
import dbservermentoria.Teste.Repository.CategoriasRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@RestController
@RequestMapping("/v1/categorias")
public class CategoriasController {

    private CategoriasRepository categoriasRepository;
    private ArrayList<Categorias> categoriasArrayList = new ArrayList<>();

    @Autowired
    public CategoriasController(CategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    @PostMapping
    public ResponseEntity<Categorias> save(@RequestBody Categorias categorias)  {

            Categorias adicionaCategoria = categoriasRepository.save(categorias);
            return new ResponseEntity<>(adicionaCategoria, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Categorias>> getCat() {
        List<Categorias> listaDeCategorias = categoriasRepository.findAll();
        return new ResponseEntity<>(listaDeCategorias, HttpStatus.FOUND);
    }

    @GetMapping("/{categoriaId}")
    public ResponseEntity findByIdCar(@PathVariable Integer categoriaId) {
       Optional<Categorias> categoria = categoriasRepository.findById(categoriaId) ;
      return new ResponseEntity(categoria, HttpStatus.FOUND);
    }

    @DeleteMapping("/{categoriaId}")
    public  ResponseEntity<?> delete(@PathVariable Integer categoriaId) {

      categoriasRepository.deleteById(categoriaId);
       return new ResponseEntity<>("Veiculo excluido com sucesso.", HttpStatus.FOUND);
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<?> put(@RequestBody Categorias categoriasBody, @PathVariable Integer categoriaId){
          for(Categorias categoriaDoCarro : categoriasRepository.findAll()){
              if (categoriaDoCarro.getId() == categoriaId){

      //  for ( Categorias categoriaDoCarro : categoriasArrayList ){
           // if (categoriaDoCarro.getId() == categoriaId){
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
