package dbservermentoria.Teste.Controller;


import com.sun.xml.internal.ws.api.message.Message;
import dbservermentoria.Teste.Model.Carros;
import dbservermentoria.Teste.Model.Categorias;
import dbservermentoria.Teste.Repository.CarrosRepository;
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
@RequestMapping("/v1/carros")
public class CarrosController {

    private CarrosRepository carrosRepository;
    private ArrayList<Carros> carrosArrayList = new ArrayList<>();

    @Autowired
    public CarrosController(CarrosRepository carrosRepository) {
        this.carrosRepository = carrosRepository;
    }


    @PostMapping
    public ResponseEntity<Carros> saveCar(@RequestBody Carros carros) {

        Carros adicionaCarro = carrosRepository.save(carros);
        return new ResponseEntity<>(adicionaCarro, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Carros>> findAllCars() {
        List<Carros> listaDeCarros = carrosRepository.findAll();
        return new ResponseEntity<>(listaDeCarros, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdCar(@PathVariable Integer id) {

        Optional<Carros> carro = carrosRepository.findById(id);
        return new ResponseEntity<>("teste", HttpStatus.FOUND);
       /* Carros carro;
        for (Carros carroDaLista : carrosArrayList) {
            try {
                if (carroDaLista.getId() == id) {
                    carro = carroDaLista;
                    return new ResponseEntity<>(carro, HttpStatus.FOUND);
                } else {
                    return new ResponseEntity<>("Veiculo não encontrado", HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("Requisição não existente", HttpStatus.BAD_REQUEST);*/
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception {
        //   carrosRepository.deleteById(id);
        // return new ResponseEntity<>("teste", HttpStatus.FOUND);
        Carros carro;
        for (Carros carroDaLista : carrosArrayList) {
            if (carroDaLista.getId() == id) {
                carro = carroDaLista;
                carrosArrayList.remove(carroDaLista);
                return new ResponseEntity<>(carro, HttpStatus.FOUND);
            }
        }throw new Exception();
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody Carros carrosBody, @PathVariable Integer id) {

        for (Carros carroDaLista : carrosArrayList) {
            try {
                if (carroDaLista.getId() == id) {
                    BeanUtils.copyProperties(carrosBody, carroDaLista, "id");
                    return new ResponseEntity<>(carroDaLista, HttpStatus.FOUND);
                } else {
                    return new ResponseEntity<>("Veiculo não encontrado teste", HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("Requisição não existente", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@RequestBody Carros carrosBody, @PathVariable Integer id) {
        for (Carros carrosDaLista : carrosArrayList) {
            try {
                if (carrosDaLista.getId() == id)
                    BeanUtils.copyProperties(completarCampo(carrosBody, carrosDaLista), carrosDaLista, "id");
                return new ResponseEntity<>(carrosDaLista, HttpStatus.FOUND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("Veiculo não encontrado", HttpStatus.NOT_FOUND);
    }

    public Carros completarCampo(Carros carrosBody, Carros carroDaLista) {
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


