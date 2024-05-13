package org.example.bot;

import org.example.Main;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class tgBot extends TelegramLongPollingBot {
    Main main = new Main();
    boolean wait_for_obj = false;

    @Override
    public void onUpdateReceived(Update update) {



        SendMessage sendMessage = new SendMessage();
        LocalDate date = LocalDate.now();
        LocalDate tomorrow = date.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = date.format(formatter);
        String _tomorrow = tomorrow.format(formatter);
        String chatId = update.getMessage().getChatId().toString();
        String message = "hello";
        String text = update.getMessage().getText();



        main.checkUser(chatId);


        if(wait_for_obj) {
            main.addObj(chatId, update.getMessage().getText());
            message = "changed on " + update.getMessage().getText();
            wait_for_obj = !wait_for_obj;
        }



        if(!wait_for_obj) {
            if (text.equals("На Сегодня")) {
                message = main.Second(today);
            }
            if (text.equals("На Завтра")) {
                message = main.Second(_tomorrow);
            }
            if (text.equals("Вставить индекс")) {
                wait_for_obj = true;
                message = "Вставьте индекс";
            }
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("На Сегодня"));
        keyboardFirstRow.add(new KeyboardButton("На Завтра"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Вставить индекс"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        System.out.println(wait_for_obj + "wait");
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }





    @Override
    public String getBotToken(){
        return "token";
    }

    @Override
    public String getBotUsername() {
        return "name";
    }
}
