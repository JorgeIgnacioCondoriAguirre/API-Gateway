package ar.edu.utn.dds.gateway.service;

import ar.edu.utn.dds.gateway.restClients.ApiExternaService;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class Grupo7Bot extends TelegramLongPollingBot {

    private final ApiExternaService apiService;

    @Value("${TOKEN_BOT}")
    private String botToken;

    @Value("${NAME_BOT}")
    private String botUsername;

    public Grupo7Bot(ApiExternaService apiService) {
        this.apiService = apiService;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            System.out.println("¡Bot registrado exitosamente en Telegram!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            final String messageTextReceived = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String respuesta = procesarComando(messageTextReceived);
            enviarMensaje(chatId.toString(), respuesta);
        }
    }

    private String procesarComando(String comando) {
        if (comando.equals("/start")) {
            return " Bienvenido al sistema! ¿Qué tipo de usuario sos?\nIngresá /donadores o /Admin";
        }

        if (comando.equals("/donadores")) {
            return "Opciones de Donador (Ingresá el comando para ejecutar):\n" +
                    "/registro - Registrarse\n" +
                    "/mis_estadisticas [ID] - Consultar sus estadísticas\n" +
                    "/consultar_donadores - Ver todos los donadores\n" +
                    "/consultar_donador_id [ID] - Buscar donador por ID";
        }

        if (comando.equals("/Admin")) {
            return "Opciones de Admin (Ingresá el comando para ejecutar):\n" +
                    "/crear_entidad - Crear una entidad\n" +
                    "/editar_entidad [ID] - Editar una entidad\n" +
                    "/consultar_entidades - Consultar todas\n" +
                    "/consultar_entidad_id [ID] - Consultar por ID\n" +
                    "/alta_necesidad - Alta de una necesidad\n" +
                    "/borrar_necesidad [ID] - Borrar necesidad\n" +
                    "/modificar_necesidad [ID] - Modificar necesidad\n" +
                    "/consultar_necesidad [ID] - Consulta de necesidad";
        }

        if (comando.startsWith("/registro")) {
            String datosCrudos = comando.replace("/registro", "").trim();

            if (datosCrudos.isEmpty()) {
                return "Para registrarte, enviá tus datos separados por coma.\n" +
                        "Ejemplo: `/registro Nombre, Apellido, Edad, Email, DNI, Domicilio`";
            }
            String[] datos = datosCrudos.split(",");

            try {
                return apiService.registrarDonador(datos[0].trim(),
                            datos[1].trim(),
                            Integer.parseInt(datos[2].trim()),
                            datos[3].trim(),
                            datos[4].trim(),
                            datos[5].trim());
                } catch (NumberFormatException e) {
                    return "Faltan o sobran datos. Asegurate de enviar los 6 datos separados por comas";
                }
        }

        if (comando.startsWith("/consultar_donador_id")) {
            String[] partes = comando.split(" ",2);
            try {
                return apiService.consultarDonadorPorID(partes[1].trim());
            } catch (NumberFormatException e){
                return "Comando incompleto. Usa el formato: /consultar_donador_id [ID]";
            }
        }

        if (comando.startsWith("/consultar_donadores")){
            return apiService.consultarTodosLosDonadores();
        }

        if (comando.startsWith("/mis_estadisticas")) {
            String[] partes = comando.split(" ",2);
            try {
                return apiService.consultarEstadisticasDeUnDonador(partes[1].trim());
            } catch (NumberFormatException e) {
                return "Comando incompleto. Usa el formato: /mis_estadisticas [ID]";
            }
        }

        if(comando.startsWith("/crear_entidad")){
            String datosCrudos = comando.replace("/crear_entidad", "").trim();

            if (datosCrudos.isEmpty()) {
                return "Para crear una entidad, enviá los datos separados por coma.\n" +
                        "Ejemplo: /crear_entidad Razon social, Domicilio, Telefono, Correo";
            }
            String[] datos = datosCrudos.split(",",4);

            try {
                return apiService.crearEntidad(datos[0].trim(),
                            datos[1].trim(),
                            datos[2].trim(),
                            datos[3].trim());
                } catch (NumberFormatException e) {
                    return "Faltan o sobran datos. Asegurate de enviar los 4 datos separados por comas";
                }
        }

        if(comando.startsWith("/editar_entidad")){
            String datosCrudos = comando.replace("/editar_entidad", "").trim();

            if (datosCrudos.isEmpty()) {
                return "Para modificar una entidad benefica, enviá el ID y la nueva razon social separados por coma.\n" +
                        "Ejemplo: /editar_entidad ID, Razon social";
            }
            String[] partes = comando.split(",",2);
            try{
                return apiService.editarEntidad(partes[0].trim(), partes[1].trim());
            } catch (NumberFormatException e) {
                return "Comando incompleto. Usa el formato: /editar_entidad [ID]";
            }
        }

        if(comando.startsWith("/consultar_entidades")){
            return apiService.consultarTodasLasEntidades();
        }

        if (comando.startsWith("/consultar_entidad_id")) {
            String[] partes = comando.split(" ",2);
            try {
                return apiService.consultarEntidadPorID(partes[1].trim());
            } catch (NumberFormatException e){
                return "Comando incompleto. Usa el formato: /consultar_donador_id [ID]";
            }
        }

        if(comando.startsWith("/alta_necesidad")){
            String datosCrudos = comando.replace("/alta_necesidad", "").trim();

            if (datosCrudos.isEmpty()) {
                return "Para dar de alta una necesidad, enviá los datos separados por coma.\n" +
                        "Ejemplo: /alta_necesidad EntidadID, Nivel de urgencia, Descripcion, Cantidad objetivo, ProductoSolicitadoID, Tipo de necesidad";
            }
            String[] datos = datosCrudos.split(",",6);

            try {
                return apiService.altaNecesidad(
                        datos[0].trim(),
                        Integer.parseInt(datos[1].trim()),
                        datos[2].trim(),
                        Integer.parseInt(datos[3].trim()),
                        datos[4].trim(),
                        TipoNecesidadMaterialEnum.valueOf(datos[5].trim()));
            } catch (NumberFormatException e) {
                return "Faltan o sobran datos. Asegurate de enviar los 6 datos separados por comas";
            }
        }

        if(comando.startsWith("/borrar_necesidad")){
            String[] partes = comando.split(" ");
            if (partes.length == 2) {
                return apiService.borrarNecesidad(partes[1].trim());
            } else {
                return "Comando incompleto. Usa el formato: /borrar_necesidad [ID]";
            }
        }

        if(comando.startsWith("/modificar_necesidad")){
            String datosCrudos = comando.replace("/modificar_necesidad", "").trim();

            if (datosCrudos.isEmpty()) {
                return "Para modificar una necesidad, enviá el ID y *** separados por coma.\n" +
                        "Ejemplo: /modificar_necesidad ID, ***";
            }
            String[] partes = comando.split(",",2);
            try{
                return apiService.modificarNecesidad(partes[0].trim(), partes[1].trim());
            } catch (NumberFormatException e) {
                return "Comando incompleto. Usa el formato: /modificar_necesidad [ID]";
            }
        }

        if(comando.startsWith("/consultar_necesidad")) {
            String[] partes = comando.split(" ",2);
            try {
                return apiService.consultarNecesidadPorID(partes[1].trim());
            } catch (NumberFormatException e){
                return "Comando incompleto. Usa el formato: /consultar_necesidad [ID]";
            }
        }
        return "Comando no reconocido. Usá /start para ver el menú inicial.";
    }

    private void enviarMensaje(String chatId, String texto) {
        SendMessage message = new SendMessage(chatId, texto);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}