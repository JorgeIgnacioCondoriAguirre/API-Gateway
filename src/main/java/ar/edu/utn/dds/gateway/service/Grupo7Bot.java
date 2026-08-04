package ar.edu.utn.dds.gateway.service;

import ar.edu.utn.dds.gateway.restClients.ApiExternaService;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
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
                    "/mis_estadisticas - Consultar sus estadísticas\n" +
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

        if(comando.startsWith("/registro")){
            //return "Consultando sistema...\n\n" + apiService.postDonador();
        }

        if (comando.startsWith("/consultar_donador_id")) {
            String[] partes = comando.split(" ");
            if (partes.length == 2) {
                String donadorID = partes[1];
                return "Buscando donador " + donadorID + "...\n" + apiService.consultarDonadorPorID(donadorID);
            } else {
                return "Comando incompleto. Usa el formato: /consultar_donador_id [ID]";
            }
        }

        if (comando.startsWith("/consultar_donadores")){
            return "Consultando sistema...\n\n" + apiService.consultarTodosLosDonadores();
        }

        if (comando.startsWith("/mis_estadisticas")) {
            String[] partes = comando.split(" ");
            if (partes.length == 2) {
                String donadorID = partes[1];
                return "Buscando donador " + donadorID + "...\n" + apiService.consultarEstadisticasDeUnDonador(donadorID);
            } else {
                return "Comando incompleto. Usa el formato: /donadores/id [ID]";
            }
        }

        if(comando.startsWith("/consultar_entidades")){
            return "Consultando sistema...\n\n" + apiService.consultarTodasLasEntidades();
        }

        if (comando.startsWith("/consultar_entidad_id")) {
            String[] partes = comando.split(" ");
            if (partes.length == 2) {
                String entidadID = partes[1];
                return "Buscando donador " + entidadID + "...\n" + apiService.consultarEntidadPorID(entidadID);
            } else {
                return "Comando incompleto. Usa el formato: /consultar_donador_id [ID]";
            }
        }

        if(comando.startsWith("/consultar_necesidad")) {
            String[] partes = comando.split(" ");
            if (partes.length == 2) {
                String necesidadID = partes[1];
                return "Buscando donador " + necesidadID + "...\n" + apiService.consultarNecesidadPorID(necesidadID);
            } else {
                return "Comando incompleto. Usa el formato: /consultar_donador_id [ID]";
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