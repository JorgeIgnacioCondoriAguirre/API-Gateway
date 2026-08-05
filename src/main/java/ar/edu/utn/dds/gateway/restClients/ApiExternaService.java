package ar.edu.utn.dds.gateway.restClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

    public String registrarDonador(String nombre, String apellido, Integer edad, String email, String nroDocumento, String domicilio) {
        try {
        DonadorDTO donador = new DonadorDTO("", nombre, apellido, edad, email, nroDocumento, domicilio, EstadoDonadorEnum.VERIFICADO, "Ocasional");

        restClient.post()
                .uri("/donadores")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(donador)
                .retrieve()
                .toBodilessEntity();

        return "Donador registrado exitosamente";

        } catch (Exception e) {
        return "Hubo un error al registrar el donador: " + e.getMessage();
        }
    }

    public String consultarEstadisticasDeUnDonador(String donadorID) {
        try {
            String jsonCrudo = restClient.get()
                    .uri("/estadisticas/{donadorID}",donadorID)
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

    public String crearEntidad(String razonSocial, String domicilio, String telefono, String correo) {
        try {
            EntidadBeneficaDTO entidadBenefica = new EntidadBeneficaDTO("", razonSocial, domicilio, telefono, correo);
            restClient.post()
                    .uri("/entidades")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(entidadBenefica)
                    .retrieve()
                    .toBodilessEntity();

            return "Entidad benefica registrada exitosamente";

        } catch (Exception e) {
            return "Hubo un error al registrar la entidad benefica: " + e.getMessage();
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

    public String editarEntidad(String entidadID, String razonSocial){
        try {
            EntidadBeneficaDTO entidadBenefica = new EntidadBeneficaDTO("", razonSocial, "", "", "");

            restClient.patch()
                    .uri("/entidades/{entidadID}/razon-social", entidadID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(entidadBenefica)
                    .retrieve()
                    .toBodilessEntity();

            return "Entidad benéfica modificada exitosamente";

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

    public String altaNecesidad(String entidadID, Integer nivelDeUrgencia, String descripcion, Integer cantidadObjetivo, String productoSolicitadoID, TipoNecesidadMaterialEnum tipo){
        try{
            NecesidadMaterialDTO necesidadMaterial = new NecesidadMaterialDTO("",entidadID,nivelDeUrgencia,descripcion,cantidadObjetivo,productoSolicitadoID,tipo);
            restClient.post()
                    .uri("/necesidades")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(necesidadMaterial)
                    .retrieve()
                    .toBodilessEntity();

            return "Necesidad de material registrada exitosamente";

        } catch (Exception e) {
            return "Hubo un error al registrar la necesidad de material: " + e.getMessage();
        }
    }

    public String modificarNecesidad(String necesidadID, String m) {
        try {
            NecesidadMaterialDTO necesidadMaterial = new NecesidadMaterialDTO("", "", 0, "", 0, "", TipoNecesidadMaterialEnum.valueOf(""));

            restClient.patch()
                    .uri("/necesidades/{necesidadID}/***", necesidadID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(necesidadMaterial)
                    .retrieve()
                    .toBodilessEntity();

            return "Necesidad modificada exitosamente";

        } catch (Exception e) {
            return "Hubo un error o el ID no existe: " + e.getMessage();
        }
    }

    public String borrarNecesidad(String necesidadID) {
        try {
            restClient.delete()
                    .uri("/necesidad/{necesidadID}", necesidadID)
                    .retrieve()
                    .toBodilessEntity();

            return "Necesidad borrada exitosamente";

        } catch (Exception e) {
            return "Hubo un error o el ID no existe: " + e.getMessage();
        }
    }




}