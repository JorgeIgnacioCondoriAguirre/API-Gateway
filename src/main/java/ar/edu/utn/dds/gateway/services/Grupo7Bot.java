package ar.edu.utn.dds.gateway.services;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Grupo7Bot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        final String messageTextReceived = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(messageTextReceived);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "UTNBA_DDS_K3003_GRUPO7BOT";
    }
    @Override
    public String getBotToken() {
        return "8837514281:AAEXk0x6H8w8ww9rWSpUSXiEzrA6_yOiPZY";
    }
    public static void main(String[] args)
            throws TelegramApiException {
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new Grupo7Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

