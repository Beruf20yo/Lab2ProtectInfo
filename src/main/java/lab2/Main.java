package lab2;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

import static lab2.Main.Permission.*;
// quit - выход
public class Main {
    static List<Permission> permissions = new ArrayList<>(List.of(FULL_ACCESS, READ_ACCESS, WRITE_ACCESS,
            NO_ACCESS, TRANSFER_ACCESS));
    static List<FileObj> files = new ArrayList<>();
    static List<Person> people = new ArrayList<>();
    static List<String> names = new ArrayList<>(List.of("John", "Nolan", "Cris", "Pol"));
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        createFiles(4);
        Map<FileObj, List<Permission>> adminMap = new HashMap<>();
        for (var file : files) {
            adminMap.put(file, new ArrayList<>(List.of(FULL_ACCESS)));
        }
        people.add(new Person("Admin", adminMap));
        for (var n : names) {
            people.add(createPerson(n));
        }
        displayPermMatrix();
        firstSteps();
    }

    private static void firstSteps(){
        String input = "";
        System.out.println("Введи идентификатор");
        input = scanner.nextLine();
        while ((!names.contains(input)) || input.equals("Admin")) {
            System.out.println("Неверно! Введи идентификатор:");
            input = scanner.nextLine();
        }
        String finalInput = input;
        Person person = people.stream().filter(x -> x.getName().equals(finalInput)).toList().get(0);
        System.out.println("Добро пожаловать в систему, " + person.getName() + " !");
        menu(person);
    }

    private static void menu(Person person) {
        System.out.println("Перечень прав:\n" + person);
        System.out.println("С каким файлом хотите взаимодествовать? (введите номер)");
        String input = scanner.nextLine();
        if(input.equals("quit")){
            firstSteps();
        }
        int idFile = Integer.parseInt(input);
        FileObj file = files.get(idFile);
        workWithFile(person, file);

    }

    private static void workWithFile(Person person, FileObj file) {
        System.out.println(file.getFileName());
        System.out.println("Список прав к файлу: ");
        System.out.println("Выберите действие: (введите номер)");
        System.out.println(checkPermissions(person.getAccess().get(file)));
        String input = scanner.nextLine();
        if(input.equals("quit")){
            firstSteps();
        }
        int chose = Integer.parseInt(input);
        switch (chose) {
            case 1 -> writeFile(person, file);
            case 2 -> readFile(person, file);
            case 3 -> transferPermissions(person, file, person.getAccess().get(file));
        }
    }

    private static void readFile(Person person, FileObj file) {
        System.out.println("Сейчас в файле написано:\n" + file.getFilling());
        menu(person);
    }

    private static void writeFile(Person person, FileObj file) {
        System.out.println("Введите текст, он добавится к уже существующему");
        String input = scanner.nextLine();
        if(input.equals("quit")){
            firstSteps();
        }
        file.writeToFile(input);
        menu(person);
    }

    private static void transferPermissions(Person person, FileObj file, List<Permission> perms) {
        System.out.println("Выберите кому вы хотите передать права: (введите номер)");
        int i = 0;
        for (var pe : people) {
            System.out.println(i++ + ") " + pe.getName());
        }
        String input = scanner.nextLine();
        int chose = Integer.parseInt(input);
        people.get(chose).setPermissions(file, perms);
        displayPermMatrix();
        menu(person);
    }

    private static String checkPermissions(List<Permission> permissions) {
        StringBuilder sb = new StringBuilder();
        if (permissions.contains(FULL_ACCESS)) {
            sb.append("1) - Сделать запись\n").append("2) - Прочитать\n").append("3) - Передать права\n");
        }
        int i = 0;
        if (permissions.contains(READ_ACCESS)) {
            sb.append("2) - ").append("Прочитать\n");
            i++;
        }
        if (permissions.contains(WRITE_ACCESS)) {
            sb.append("1) - ").append("Сделать запись\n");
            i++;
        }
        if (permissions.contains(TRANSFER_ACCESS)) {
            sb.append("3) - ").append("Передать права\n");
        }
        return sb.toString();
    }


    private static void createFiles(int countObj) {
        for (int i = 0; i < countObj; i++) {
            files.add(new FileObj("Файл " + i));
        }
    }

    private static Person createPerson(String name) {
        return new Person(name, createMapForPerson());
    }

    private static Map<FileObj, List<Permission>> createMapForPerson() {
        Map<FileObj, List<Permission>> map = new HashMap<>();
        int minValue = 1;
        int maxValue = 3;
        for (FileObj file : files) {
            int randomValue = minValue + (int) (Math.random() * (maxValue - minValue + 1));
            map.computeIfAbsent(file, k -> new ArrayList<>());
            map.get(file).addAll(generateList(randomValue));
        }
        return map;
    }

    private static List<Permission> generateList(int countPerm) {
        List<Permission> perms = new ArrayList<>();
        int minValue = 0;
        int maxValue = permissions.size() - 1;
        for (int i = 0; i < countPerm; i++) {
            if (perms.contains(FULL_ACCESS) || perms.contains(NO_ACCESS)) {
                break;
            }
            int randomValue = minValue + (int) (Math.random() * (maxValue - minValue + 1));
            Permission perm = permissions.get(randomValue);
            while (perms.contains(perm)) {
                randomValue = minValue + (int) (Math.random() * (maxValue - minValue + 1));
                perm = permissions.get(randomValue);
            }
            perms.add(perm);
        }
        if (perms.contains(FULL_ACCESS)) {
            return List.of(FULL_ACCESS);
        }
        if (perms.contains(NO_ACCESS)) {
            return List.of(NO_ACCESS);
        }
        return perms;
    }

    private static void displayPermMatrix() {
        System.out.printf("%15s |%50s |%50s |%50s |%50s\n", "Объект/Субъект", files.get(0).getFileName(), files.get(1).getFileName(),
                files.get(2).getFileName(), files.get(3).getFileName());
        for (var person : people) {
            System.out.printf("%15s |%50s |%50s |%50s |%50s\n", person.getName(), person.getPermissions(files.get(0)),
                    person.getPermissions(files.get(1)), person.getPermissions(files.get(2)),
                    person.getPermissions(files.get(3)));
        }
    }

    @AllArgsConstructor
    enum Permission {
        FULL_ACCESS("Полный доступ"),
        READ_ACCESS("Доступ на чтение"),
        WRITE_ACCESS("Доступ на запись"),
        TRANSFER_ACCESS("Передача прав"),
        NO_ACCESS("Отсутствие прав");
        @Getter
        private String nameTag;
    }

}


