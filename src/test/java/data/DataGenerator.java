package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private static Faker faker = new Faker(new Locale("ru"));

    public DataGenerator() {//Конструктор
    }

    public static String generateDate(int dayRange) {
        return LocalDate.now().plusDays(dayRange).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String getCity() {
        List<String> list = Arrays.asList("Абакан", "Анадырь", "Архангельск", "Астрахань", "Барнаул", "Белгород", "Биробиджан",
                "Благовещенск", "Брянск", "Великий Новгород", "Владивосток", "Владикавказ", "Владимир", "Волгоград", "Вологда", "Воронеж",
                "Горно-Алтайск", "Грозный", "Екатеринбург", "Иваново", "Ижевск", "Иркутск", "Йошкар-Ола", "Казань", "Калининград", "Калуга", "Кемерово",
                "Киров", "Кострома", "Краснодар", "Красноярск", "Курган", "Курск", "Кызыл", "Липецк", "Магадан", "Магас", "Майкоп",
                "Махачкала", "Москва", "Мурманск", "Нальчик", "Нарьян-Мар", "Нижний Новгород", "Новосибирск", "Омск", "Орёл", "Оренбург",
                "Пенза", "Пермь", "Петрозаводск", "Петропавловск-Камчатский", "Псков", "Ростов-на-Дону", "Рязань", "Салехард", "Самара",
                "Санкт-Петербург", "Саранск", "Саратов", "Севастополь", "Симферополь", "Смоленск", "Ставрополь", "Сыктывкар",
                "Тамбов", "Тверь", "Томск", "Тула", "Тюмень", "Улан-Удэ", "Ульяновск", "Уфа", "Хабаровск", "Ханты-Мансийск", "Чебоксары",
                "Челябинск", "Черкесск", "Чита", "Элиста", "Южно-Сахалинск", "Якутск", "Ярославль");
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public static String getName() {
        String name = faker.name().fullName().replace("ё", "е");
        return name;
    }

    public static String getPhone() {
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static class Registration {
        public Registration() {
        }

        public static UserInfo generateValidUser() {
            return new UserInfo(getCity(),
                    faker.name().lastName() + " " + faker.name().firstName(),
                    faker.phoneNumber().phoneNumber());
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
