package dbservermentoria.Teste.Controller;


import dbservermentoria.Teste.Model.Carros;
import dbservermentoria.Teste.Model.Categorias;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/carros")
public class CarrosController {

    private ArrayList<Carros> carrosArrayList = new ArrayList<>();

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Carros carros) {
        carrosArrayList.add(carros);
        return new ResponseEntity<>("Veiculo cadastrado", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getCar() {
        return new ResponseEntity<>(carrosArrayList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Carros carro;
        for ( Carros carroDaLista: carrosArrayList) {
            if (carroDaLista.getId() == id) {
                carro = carroDaLista;
                carrosArrayList.remove(carroDaLista);
                return new ResponseEntity<>(carro, HttpStatus.FOUND);
            }
        }
        return new ResponseEntity<>("Veiculo não encontrado", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody Carros carrosBody, @PathVariable Integer id ) {
        for ( Carros carroDaLista  : carrosArrayList ) {
            if (carroDaLista.getId() == id){
            BeanUtils.copyProperties(carrosBody, carroDaLista,  "id");
                return new ResponseEntity<>(carroDaLista, HttpStatus.FOUND);
            }
        }
        return new ResponseEntity<>("Veiculo não encontrado teste", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@RequestBody Carros carrosBody, @PathVariable Integer id){
        for ( Carros carrosDaLista : carrosArrayList) {
            if (carrosDaLista.getId() == id) {
                BeanUtils.copyProperties(completarCampo(carrosBody,carrosDaLista), carrosDaLista, "id");
                return new ResponseEntity<>(carrosDaLista, HttpStatus.FOUND);
            }
        }
        return new ResponseEntity<>("Veiculo não encontrado", HttpStatus.NOT_FOUND);
    }

    public Carros completarCampo(Carros carrosBody, Carros carroDaLista){
        Stream<Field> fields = Stream.of(carroDaLista.getClass().getDeclaredFields());
        fields.forEach(field -> {
            try {
                field.setAccessible(true);
                if (field.get(carrosBody) == null)
                    field.set(carrosBody, field.get(carroDaLista));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return carrosBody;
    }
}


