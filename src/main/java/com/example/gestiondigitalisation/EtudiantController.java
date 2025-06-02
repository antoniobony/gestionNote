package com.example.gestiondigitalisation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {
    @Valid

    private final EtudiantService etudiantService;

    @GetMapping("{id}")
    public  ResponseEntity<Map<String,Object>> getEtudiantById(@PathVariable Long id) {
        Map<String,Object> response = new HashMap<>();
        try {
            response.put("etudiant",etudiantService.findById(id));
            response.put("code",HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("code",HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> getEtudiants(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String moyenne,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false,defaultValue = "numEt,DESC") String [] sort
    ) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        if(sort[0].contains(",")){
            for(String sortOrder : sort) {
                String[]_sort = sortOrder.split(",");
                orders.add(new Sort.Order(Sort.Direction.fromString(_sort[1]),_sort[0]));
            }
        }else{
            orders.add(new Sort.Order(Sort.Direction.fromString(sort[1]),sort[0]));
        }

        Map<String,Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        Page<Etudiant> page1;

        page1 = etudiantService.findAllPage(pageable);


            try{
                if(nom != null && !nom.isEmpty()){
                    page1 = etudiantService.findByNom(pageable,nom);
                } else if (moyenne != null && !moyenne.isEmpty()) {
                    page1 = etudiantService.findByMoyenne(pageable,moyenne);
                }
            } catch (RuntimeException e) {
                response.put("message", e.getMessage());
                response.put("code",HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
            }

        List<Etudiant> etudiants = page1.getContent();
        List<Object> results = new ArrayList<>();

        etudiants.stream().forEach(etudiant -> {
            Map<String,Object> etudiantMap = new HashMap<>();

            String obs = "admis";
            double moyenneNote = Double.parseDouble(etudiant.getMoyenne());

            if( moyenneNote >= 5 && moyenneNote < 10) {
                obs ="redoublant";
            } else if (moyenneNote < 5) {
                obs ="exclus";
            }
            etudiantMap.put("numEt",etudiant.getNumEt());
            etudiantMap.put("nom", etudiant.getNom());
            etudiantMap.put("moyenne",etudiant.getMoyenne());
            etudiantMap.put("obs", obs);
            results.add(etudiantMap);

        });

        response.put("etudiants",results);
        response.put("currentPage",page1.getNumber());
        response.put("totalPages",page1.getTotalPages());
        response.put("totalElements",page1.getTotalElements());
        response.put("code",HttpStatus.OK.value());
        response.put("sort",sort);
        return new  ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/moyenneClasse")
    public ResponseEntity<Map<String,Object>> getMoyenneClasse() {

        Map<String,Object> response = new HashMap<>();
        List<Etudiant> etudiant =etudiantService.findAll() ;
        
        Double etudiantMax = etudiant.stream().map(etudiant1 -> Double.parseDouble(etudiant1.getMoyenne())).toList()
                .stream().reduce(0.0,Double::max);

        Double etudiantMin = etudiant.stream().map( etudiant1 -> Double.parseDouble(etudiant1.getMoyenne())).toList()
                .stream().reduce(Double.parseDouble(etudiant.get(0).getMoyenne()),Double::min);

        List<Long> etudiantSort = etudiant.stream()
                .map(Etudiant::getNumEt)
                .sorted(Comparator.reverseOrder())
                .toList();

        response.put("moyenneMinimale",etudiantMin);
        response.put("moyenneMaximale",etudiantMax);
        response.put("code",HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping
    public  ResponseEntity<Map<String,Object>> createEtudiant(@RequestBody @Valid EtudiantDto etudiant) {
        Map<String,Object> response = new HashMap<>();
        try {
            response.put("etudiant",etudiantService.create(etudiant));
            response.put("code",HttpStatus.CREATED.value());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        }catch (RuntimeException e){
            response.put("message",e.getMessage());
            response.put("code",HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String,Object>> updateEtudiantById(@PathVariable Long id, @RequestBody @Valid EtudiantDto etudiant) {
        Map<String,Object> response = new HashMap<>();
            try {
                response.put("etudiant",etudiantService.updateEtudiant((etudiant),id));
                response.put("code",HttpStatus.OK.value());

                return new ResponseEntity<>(response,HttpStatus.OK) ;

            }catch (IllegalArgumentException e) {

                response.put("message",e.getMessage());
                response.put("code",HttpStatus.OK.value());
                return new ResponseEntity<>(response,HttpStatus.OK) ;

            }
        }

    @DeleteMapping("{numEt}")
    public ResponseEntity<Map<String,Object>> deleteEtudiantById(@PathVariable Long numEt) {
        Map<String,Object> response = new HashMap<>();
        try {
            response.put("message",etudiantService.deleteEtudiantById(numEt));
            response.put("code",HttpStatus.OK.value());

            return new ResponseEntity<>(response,HttpStatus.OK) ;
        }catch (IllegalArgumentException e) {

            response.put("message",e.getMessage());
            response.put("code",HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
