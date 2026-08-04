package ar.edu.utn.dds.gateway.restClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;

@Service
public class ApiExternaService {

    private final RestClient restClient;

    public ApiExternaService(@Value("${DONADORESYENTIDADES}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String postDonador(DonadorDTO donadorDTO) {
//        try {
//            restClient.post()
//                    .uri("/donadores")
//                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
//                    .body(String.class)
//                    .retrieve()
//                    .toBodilessEntity();
//        } catch (Exception e) {
//            return "Hubo un error al conectar con la API: " + e.getMessage();
//        }
        return "String";
    }

    public String consultarEstadisticasDeUnDonador(String donadorID) {
        try {
            String jsonCrudo = restClient.get()
                    .uri("/estadisticas/{donadorID}")
                    .retrieve()
                    .body(String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonCrudo);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (Exception e) {
            return "Hubo un error al conectar con la API: " + e.getMessage();
        }
    }

    public String consultarTodosLosDonadores() {
        try {
            String jsonCrudo = restClient.get()
                    .uri("/donadores")
                    .retrieve()
                    .body(String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonCrudo);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (Exception e) {
            return "Hubo un error al conectar con la API: " + e.getMessage();
        }
    }

    public String consultarDonadorPorID(String donadorID) {
        try {
            String jsonCrudo = restClient.get()
                    .uri("/donadores/{donadorID}", donadorID)
                    .retrieve()
                    .body(String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonCrudo);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (Exception e) {
            return "Hubo un error o el ID no existe: " + e.getMessage();
        }
    }

    public String consultarTodasLasEntidades() {
        try {
            String jsonCrudo = restClient.get()
                    .uri("/entidades")
                    .retrieve()
                    .body(String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonCrudo);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (Exception e) {
            return "Hubo un error al conectar con la API: " + e.getMessage();
        }
    }

    public String consultarEntidadPorID(String entidadID) {
        try {
            String jsonCrudo = restClient.get()
                    .uri("/entidades/{entidadID}", entidadID)
                    .retrieve()
                    .body(String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonCrudo);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (Exception e) {
            return "Hubo un error o el ID no existe: " + e.getMessage();
        }
    }

    public String consultarNecesidadPorID(String necesidadID){
        try {
            String jsonCrudo = restClient.get()
                    .uri("/necesidad/{necesidadID}", necesidadID)
                    .retrieve()
                    .body(String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonCrudo);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (Exception e) {
            return "Hubo un error o el ID no existe: " + e.getMessage();
        }
    }



}