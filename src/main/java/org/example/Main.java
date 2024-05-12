package org.example;

import org.example.bot.tgBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.sql.*;
import java.time.format.DateTimeFormatter;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public String obj = "141";

    public static final String URL ="jdbc:mysql://localhost:3306/parcing_db";
    public static final String USER = "root";
    public static final String PASSWORD = "3216";


    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new tgBot());
    }

    public String Second(String _day){
        String les = " ";
        boolean trueDate = false;
        boolean end = false;
        int  num = 1;








        try {
            Document document = Jsoup.connect("https://lk.ks.psuti.ru/?mn=2&obj=" + obj).get();
            do{
                Elements day = document.select("body > table:nth-child(5) > tbody > tr:nth-child(" + num + ")");
                num ++;
                if (day.text().contains(_day)){
                    trueDate = true;
                    num ++;
                    System.out.println(true);
                }
                if (num > 60){
                    les = "Нет расписания";
                    trueDate = false;
                    end = true;
                }
            }while(!trueDate && !end);




            while (trueDate ){
                String _number;
                String _time;
                String _lessons;
                Elements End = document.select("body > table:nth-child(5) > tbody > tr:nth-child(" + num + ")");
                Elements Number = document.select("body > table:nth-child(5) > tbody > tr:nth-child("+ num + ") > td:nth-child(1)");
                Elements Time = document.select("body > table:nth-child(5) > tbody > tr:nth-child("+ num + ") > td:nth-child(2)");
                Elements Lessons = document.select("body > table:nth-child(5) > tbody > tr:nth-child("+ num + ") > td:nth-child(4)");
                _number = Number.text();
                _time = Time.text();
                _lessons = Lessons.text();





                if (_lessons.contains("Экономика отрасли Шамбер Лола Низамовна")){
                    _lessons = "Экономика";
                }else if(_lessons.contains("Основы алгоритмизации и программирования Бондаренко Анастасия Вячеславовна")){
                    _lessons = "ОАиП";
                }else if(_lessons.contains("Иностранный язык в профессиональной деятельности Карпеева Александра Сергеевна")){
                    _lessons = "Англиийский";
                }else if(_lessons.contains("МДК.04.02 Обеспечение качества функционирования компьютерных систем Сергеев Роман Алексеевич")){
                    _lessons = "ОКФКС";
                } else if (_lessons.contains("МДК.04.01 Внедрение и поддержка компьютерных систем Шиллер Эмиля Владиславовна")) {
                    _lessons = "ВиПКС";
                }else if(_lessons.contains("Стандартизация, сертификация и техническое документоведение Солодовников Павел Петрович")){
                    _lessons = "Дед";
                }else if(_lessons.contains("Элементы высшей математики Амукова Светлана Николаевна")) {
                    _lessons = "ЭВМ";
                }else if(_lessons.contains("Информационные технологии Резаева Алина Игоревна")){
                    _lessons = "ИТ";
                }else if(_lessons.contains("Основы проектирования баз данных Резаева Алина Игоревна")){
                    _lessons = "ОПБД";
                }else if(_lessons.contains("Теория вероятностей и математическая статистика Андрющенко Анна Вячеславовна")){
                    _lessons = "Теория вероятностей";
                }else if(_lessons.contains("Физическая культура Чернов Валентин Иванович Московское шоссе")){
                    _lessons = "Физра";
                }else if(_lessons.contains("№ пары Время занятий Дисциплина, преподаватель")){
                    _lessons = "";
                }


                System.out.println(_number + " " + _time + " " + _lessons);
                les = les + "\n" + _number+ " " + _time + " " + _lessons;
                num ++;
                if(End.text().isEmpty()){
                    trueDate = false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return les;
    }

    public void checkUser(String chatid){
        try{
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            statement.executeQuery("select * from users where chatid =" + chatid);


            ResultSet resultSet = statement.getResultSet();

            if(resultSet.next()) {
                this.obj = resultSet.getString("obj");
                System.out.println(this.obj);
            }else{
                addUser(chatid, "141");
            }
            

            resultSet.close();

            statement.close();
            connection.close();

        }catch(SQLException e){
            System.out.println(e);
        }
    }

    public void addUser(String chatid, String obj){
        System.out.println("addUser");
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into users (chatid, obj) values ('" + chatid + "', '" + obj + "')");
            statement.close();
            connection.close();
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    public void addObj(String chatid, String obj){
        System.out.println("addObj");
        String sql = "update parcing_db.users set obj = '" + obj + "' where chatid = '" + chatid + "'";
        try{
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
        connection.close();
        }catch (SQLException e){
            System.out.println(e);
        }
    }
}





