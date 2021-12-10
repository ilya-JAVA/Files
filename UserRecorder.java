package files;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRecorder {
    private BufferedReader readIt;
    private Map<String, User> database = new HashMap();

    private void helloMethod() { //Все перечисленное в методе появляется в консоли
        System.out.println("If you want to add a user, press '1'.");
        System.out.println("If you want to see all users, press '2'.");
        System.out.println("If you want to delete the user press '3'.");
        System.out.println("If you want to change user settings, press '4'.");
        System.out.println("If you want to exit press 'Q'.");
    }

    public void run() throws IOException {
        boolean endWorking = false;
        Add add = new Add();
        Delete delete = new Delete();
        Get get = new Get();
        readIt = new BufferedReader(new InputStreamReader(System.in));
        Change change = new Change();
        while (endWorking == false) {
            helloMethod();
            String nStr = readIt.readLine();
            if (nStr.equals("1")) {
                add.addUser();
                continue;
            }
            if (nStr.equals("2")) {
                get.getAllUsers();
                System.out.println();
                continue;
            }
            if (nStr.equals("3")) {
                System.out.println("Enter the name of the user you want to delete");
                String nameDel = readIt.readLine();
                delete.deleteUser(nameDel);
                continue;
            }
            if (nStr.equals("4")) {
                System.out.println("Enter the name of the user you want to make changes");
                String mustChange = readIt.readLine();
                change.changeUser(mustChange);
                continue;
            }
            if (nStr.equals("Q")) {
                endWorking = true;
            } else {
                System.out.println("Enter the correct number: 1, 2, 3 or 4.");
                System.out.println();
                continue;
            }
        }
    }

    private class Add {
        private void addUser() throws IOException { //Метод для добавления информации о пользователе
            FileWriter writer = new FileWriter("users.txt", true);
            System.out.println("Enter the first name.");
            boolean nameRecorder = false;
            writer.write("Name:");
            while (nameRecorder == false) {
                String name = readIt.readLine();
                Pattern pattern = Pattern.compile("[A-ZА-Я]{1}[a-zа-я]{1,}");
                Matcher matcher = pattern.matcher(name);
                if (matcher.matches()) {
                    writer.write(name + "|");
                    nameRecorder = true;
                } else {
                    System.out.println("Name must consist of letters, and begin with a capital letter. Try again.");
                    continue;
                }
            }

            System.out.println("Enter the surname.");
            boolean surnameRecorded = false;
            writer.write("Surname:");
            while (surnameRecorded == false) {
                String surname = readIt.readLine();
                Pattern pattern = Pattern.compile("[A-ZА-Я]{1}[a-zа-я]{1,}");
                Matcher matcher = pattern.matcher(surname);
                if (matcher.matches()) {
                    writer.write(surname + "|");
                    surnameRecorded = true;
                } else {
                    System.out.println("Surname must consist of letters, and begin with a capital letter. Try again.");
                    continue;
                }
            }

            Boolean numberRecorded = false;
            int numberOfPhonesRecorded = 0;
            writer.write("PhoneNumber:[");
            Check check = new Check();
            while (numberRecorded == false || numberOfPhonesRecorded < 3) {
                System.out.println("Enter telephone number. Example : 375** *******.");
                String tellNumber = readIt.readLine();

                if (check.checkPhoneValidity(tellNumber)) {
                    writer.write(tellNumber + "|");
                    numberRecorded = true;
                    numberOfPhonesRecorded++;
                } else {
                    System.out.println("You entered the phone number incorrectly. Check that after the code there is a space 375 ** space *******. Try again.");
                    continue;
                }
                if (numberOfPhonesRecorded < 3) {
                    System.out.println("You can enter " + (3 - numberOfPhonesRecorded) + " more numbers. Y/N");
                    String unswer = readIt.readLine();
                    if (unswer.equals("Y") && numberOfPhonesRecorded < 3) {
                        continue;
                    } else {
                        writer.write("]");
                        break;
                    }
                } else {
                    writer.write("]");
                    break;
                }
            }

            System.out.println("Enter email");
            boolean mailRightAdd = false;
            writer.write("Email:");
            while (mailRightAdd == false) {
                String mail = readIt.readLine();
                if (check.checkMailValidity(mail)) {
                    writer.write(mail + "|");
                    mailRightAdd = true;
                } else {
                    System.out.println("Not correct mail. Example '****@****.***'. Try again");
                    continue;
                }
            }

            boolean roleAdd = false;
            int rolesAdded = 0;
            writer.write("Role:[");
            while (roleAdd == false || rolesAdded < 3) {
                System.out.println("Enter role");
                String role = readIt.readLine();
                writer.write(role + "|");
                roleAdd = true;
                rolesAdded++;
                if (rolesAdded < 3) {
                    System.out.println("You can add " + (3 - rolesAdded) + " role? Y/N");
                    String answer = readIt.readLine();
                    if (answer.equals("Y")) {
                        continue;
                    } else {
                        writer.write("]" + "\n");
                        break;
                    }
                } else {
                    writer.write("]" + "\n");
                    break;
                }
            }
            System.out.println("All users parameters are filled in. The user is added to the list.");
            writer.close();
            System.out.println();
            return;
        }
    }

    private class Check {
        private boolean checkPhoneValidity(String phoneNumber) { //Проверка на корретность ввода номера
            Pattern pattern = Pattern.compile("375[0-9]{2}[\\s][0-9]{7}");
            Matcher matcher = pattern.matcher(phoneNumber);
            return matcher.matches();
        }

        private boolean checkMailValidity(String mail) { //Проверка на корректность ввода электронноой почты
            Pattern pattern = Pattern.compile("([a-zA-Z_0-9]{1,})+@([a-zA-Z_0-9]{1,}+\\.[a-z]{2,4})");
            Matcher matcher = pattern.matcher(mail);
            return matcher.matches();
        }
    }

    private class Get {
        private void getAllUsers() throws FileNotFoundException {
            FromTxt fromTxt = new FromTxt();
            fromTxt.fromTxtToHashMap();
            for (User user : database.values()) {
                System.out.println(user.toString());
            }
        }
    }

    private class FromTxt {
        private void fromTxtToHashMap() throws FileNotFoundException {
            FileReader reader = new FileReader("users.txt");
            Scanner scanner = new Scanner(reader);
            WorkWithLine workWithLine = new WorkWithLine();
            while (scanner.hasNextLine()) {
                String lineFromTxt = scanner.nextLine();
                User notYetAdded = workWithLine.workWithLine(lineFromTxt);
                String key = notYetAdded.getName() + " " + notYetAdded.getSurname();
                database.put(key, notYetAdded);
            }
        }
    }

    private class WorkWithLine {
        private User workWithLine(String line) {
            User newUser = null;
            Take take = new Take();
            String name = null;
            String surname = null;
            List<String> phoneNumberList = new LinkedList();
            String mail = null;
            List<String> roleList = new LinkedList();
            int step = 1;
            Pick pick = new Pick();
            String workingLine = line;
            while (line.length() > 2) {
                if (step == 1) {
                    name = take.takeNecessary(workingLine, ':', '|');
                    workingLine = take.takeUnnecessary(workingLine, '|');
                    step++;
                    continue;
                }
                if (step == 2) {
                    surname = take.takeNecessary(workingLine, ':', '|');
                    workingLine = take.takeUnnecessary(workingLine, '|');
                    step++;
                    continue;
                }
                if (step == 3) {
                    String allPhoneFromWorkList = take.takeNecessary(workingLine, '[', ']');
                    phoneNumberList = pick.pickList(allPhoneFromWorkList, phoneNumberList);
                    workingLine = take.takeUnnecessary(workingLine, ']');
                    step++;
                    continue;
                }
                if (step == 4) {
                    mail = take.takeNecessary(workingLine, ':', '|');
                    workingLine = take.takeUnnecessary(workingLine, '|');
                    step++;
                    continue;
                }
                if (step == 5) {
                    String allRoleFromWorkList = take.takeNecessary(workingLine, '[', ']');
                    roleList = pick.pickList(allRoleFromWorkList, roleList);
                    workingLine = take.takeUnnecessary(workingLine, ']');
                    step++;
                    continue;
                }
                if (workingLine.length() == 0) {
                    break;
                }
            }
            if (workingLine.length() == 0)
                newUser = new User(name, surname, phoneNumberList, mail, roleList);
            return newUser;
        }
    }

    private class Take {
        private String takeNecessary(String line, char first, char next) {
            int indexColon = line.indexOf(first);
            int indexStick = line.indexOf(next);
            String nextString = line.substring(indexColon + 1, indexStick);
            return nextString;
        }

        private String takeUnnecessary(String line, char unit) {
            String workingLine = line.substring(line.indexOf(unit) + 1);
            return workingLine;
        }
    }

    private class Pick {
        private List pickList(String line, List list) {
            List listIn = list;
            String lineIn = line;
            while (lineIn.length() != 0) {
                String number = lineIn.substring(0, lineIn.indexOf('|'));
                listIn.add(number);
                lineIn = lineIn.substring(lineIn.indexOf('|') + 1);

                if (lineIn.length() == 0) {
                    return listIn;
                }
            }
            return listIn;
        }
    }

    private class Delete {
        private void deleteUser(String nameDel) throws IOException { //Класс для удаления пользователя
            RightMapToTxt rightMapToTxt = new RightMapToTxt();
            FromTxt fromTxt = new FromTxt();
            fromTxt.fromTxtToHashMap();
            if (database.containsKey(nameDel)) {
                database.remove(nameDel);
                rightMapToTxt.rightMapToTxt();
                System.out.println(nameDel + " deleted. ");
                System.out.println();
            } else {
                System.out.println("The name is entered incorrectly or this name does not exist.");
            }
        }
    }

    private class RightMapToTxt {
        private void rightMapToTxt() throws IOException {
            Clear clear = new Clear();
            clear.clearTxt();
            GetStringFromList getStringFromList = new GetStringFromList();
            FileWriter writer = new FileWriter("users.txt", true);
            for (Map.Entry entry : database.entrySet()) {
                User fromMap = (User) entry.getValue();
                String name = fromMap.getName();
                String lastName = fromMap.getSurname();
                String phone = getStringFromList.getStringFromList(fromMap.getPhoneNumber());
                String mail = fromMap.getMail();
                String role = getStringFromList.getStringFromList(fromMap.getRole());
                writer.write("Name: " + name + " | " + " LastName: " + lastName + " | " + " PhoneNumber: " + phone + " Mail: " + mail + " | " + " Role: " + role + "\n");
            }
            writer.close();
        }
    }

    private class Clear {
        private void clearTxt() throws IOException {
            FileWriter clear = new FileWriter("users.txt");
            clear.write("");
            clear.close();
        }
    }

    private class GetStringFromList {
        private String getStringFromList(List<String> list) {
            String stringForReturn = "[";
            for (String a : list) {
                stringForReturn += a + "|";
            }
            return stringForReturn + "]";
        }
    }

    private class Change {
        private void changeUser(String mustChangeName) throws IOException { //Класс для изменения данных пользователя
            boolean endWorking = false;
            Clear clear = new Clear();
            RightMapToTxt rightMapToTxt = new RightMapToTxt();
            FromTxt fromTxt = new FromTxt();
            fromTxt.fromTxtToHashMap();
            if (database.containsKey(mustChangeName)) {
                while (endWorking == false) {
                    System.out.println("What do you want to change?");
                    System.out.println("If Name, push - 1.");
                    System.out.println("If Surname, push - 2.");
                    System.out.println("If PhoneNumber, push - 3.");
                    System.out.println("If Mail, push - 4.");
                    System.out.println("If Role, push - 5.");
                    System.out.println("If exit, push - Q.");
                    User userForChange = database.get(mustChangeName);
                    String answer = readIt.readLine();
                    if (answer.equals("1")) {
                        userForChange = changeName(userForChange);
                        continue;
                    }
                    if (answer.equals("2")) {
                        userForChange = changeSurname(userForChange);
                        continue;
                    }
                    if (answer.equals("3")) {
                        userForChange = changePhoneNumber(userForChange);
                        continue;
                    }
                    if (answer.equals("4")) {
                        userForChange = changeMail(userForChange);
                        continue;
                    }
                    if (answer.equals("5")) {
                        userForChange = changeRole(userForChange);
                    }
                    if (answer.equals("Q")) {
                        clear.clearTxt();
                        rightMapToTxt.rightMapToTxt();
                        break;
                    }
                }
            } else {
                System.out.println("The name is entered incorrectly or this name does not exist");
            }
        }

        private User changeName(User userForChange) throws IOException { //Класс для изменения имени
            System.out.println("Enter new name.");
            User toBeChanged = userForChange;
            Boolean nameChanged = false;
            while (nameChanged == false) {
                String newName = readIt.readLine();
                Pattern pattern = Pattern.compile("[A-ZА-Я]{1}[a-zа-я]{1,}");
                Matcher matcher = pattern.matcher(newName);
                if (matcher.matches()) {
                    toBeChanged.setName(newName);
                    nameChanged = true;
                    System.out.println("Name changed.");
                    System.out.println();
                    return toBeChanged;
                } else {
                    System.out.println("Name must consist of letters and begin with a capital letter. Try again.");
                    continue;
                }
            }
            return toBeChanged;
        }

        private User changeSurname(User userForChange) throws IOException { //Класс для изменения фамилии
            System.out.println("Enter new surname.");
            User toBeChanged = userForChange;
            Boolean surnameChanged = false;
            while (surnameChanged == false) {
                String newSurname = readIt.readLine();
                Pattern pattern = Pattern.compile("[A-ZА-Я]{1}[a-zа-я]{1,}");
                Matcher matcher = pattern.matcher(newSurname);
                if (matcher.matches()) {
                    toBeChanged.setSurname(newSurname);
                    surnameChanged = true;
                    System.out.println("Surname changed.");
                    System.out.println();
                    return toBeChanged;
                } else {
                    System.out.println("Surname must consist of letters and begin with a capital letter. Try again.");
                    continue;
                }
            }
            return toBeChanged;
        }

        private User changePhoneNumber(User userForChange) throws IOException { //Метод для корректировки мобильного телефона
            User toBeChanged = userForChange;
            Check check = new Check();
            Boolean changePhoneNumber = false;
            while (changePhoneNumber == false) {
                System.out.println("All numbers that this user has: " + userForChange.getPhoneNumber().toString());
                System.out.println("What number do you want to change, the first, second or third? Enter the number 1, 2 or 3.\n" +
                        "If you want to add a number, press 4. If you want to exit press 5.");
                String answer = readIt.readLine();
                Pattern pattern = Pattern.compile("[1-4]{1}");
                Matcher matcher = pattern.matcher(answer);
                List<String> phoneFromMap = userForChange.getPhoneNumber();
                if (matcher.matches()) {
                    int enteredNumber = Integer.parseInt(answer);

                    if (enteredNumber == 5) {
                        break;
                    }
                    if (enteredNumber == 4) {
                        if (phoneFromMap.size() < 3) {
                            System.out.println("Enter phone number.");
                            String newPhoneNumber = readIt.readLine();
                            phoneFromMap.add(newPhoneNumber);
                            toBeChanged.setPhoneNumber(phoneFromMap);
                            System.out.println("Phone number added");
                            System.out.println();
                            return toBeChanged;
                        } else {
                            System.out.println("You can enter only three numbers.");
                            continue;
                        }
                    }
                    if (enteredNumber <= phoneFromMap.size()) {
                        System.out.println("Entered phone number.");
                        String newPhoneNumber = readIt.readLine();
                        if (check.checkPhoneValidity(newPhoneNumber)) {
                            phoneFromMap.set(enteredNumber - 1, newPhoneNumber);
                            toBeChanged.setPhoneNumber(phoneFromMap);
                            System.out.println("Phone number is added.");
                            System.out.println();
                            return toBeChanged;
                        } else {
                            System.out.println("You entered the phone number incorrectly. Check that after the code there is a space 375 ** space *******. Try again.");
                            continue;
                        }
                    } else {
                        System.out.println("You enter a number larger than the phones listed. If you want to add then press 4.");
                    }
                } else {
                    System.out.println("You entered an invalid number. Enter from 1 to 4.");
                    continue;
                }
            }
            return toBeChanged;
        }

        private User changeMail(User userForChange) throws IOException { //Метод для корректировки электронной почты
            User toBeChanged = userForChange;
            boolean mailChange = false;
            Check check = new Check();
            while (mailChange == false) {
                System.out.println("Your old mail: " + toBeChanged.getMail() + ". If yo want exit press Q");
                System.out.println("Enter new mail.");
                String newMail = readIt.readLine();

                if (newMail.equals("Q")) {
                    break;
                }

                if (check.checkMailValidity(newMail)) {
                    toBeChanged.setMail(newMail);
                    mailChange = true;
                    System.out.println("Mail changed");
                    System.out.println();
                    return toBeChanged;
                } else {
                    System.out.println("Not correct mail. Example '****@****.***'. Try again");
                    continue;
                }
            }
            return toBeChanged;
        }

        private User changeRole(User userForChange) throws IOException { //Метод для корректировки роли
            User toBeChanged = userForChange;
            Boolean chandeRole = false;
            while (chandeRole == false) {
                System.out.println("All role that this user has: " + userForChange.getRole().toString());
                System.out.println("Role under what number do you want to change, the first, second or third? Enter the number 1, 2 or 3. \n" + "If you want to add a role, press 4. If you want to exit press 5.");
                String answer = readIt.readLine();
                Pattern pattern = Pattern.compile("[1-4]{1}");
                Matcher matcher = pattern.matcher(answer);
                List<String> roleFromMap = userForChange.getRole();
                if (matcher.matches()) {
                    int enteredNumber = Integer.parseInt(answer);

                    if (enteredNumber == 5) {
                        break;
                    }
                    if (enteredNumber == 4) {
                        if (roleFromMap.size() < 3) {
                            System.out.println("Enter the role.");
                            String newRole = readIt.readLine();
                            roleFromMap.add(newRole);
                            toBeChanged.setRole(roleFromMap);
                            System.out.println("Role added.");
                            System.out.println();
                            return toBeChanged;
                        } else {
                            System.out.println("You can enter only three roles.");
                            continue;
                        }
                    }

                    if (enteredNumber <= roleFromMap.size()) {
                        System.out.println("Enter the role.");
                        String newRole = readIt.readLine();

                        roleFromMap.set(enteredNumber - 1, newRole);
                        toBeChanged.setRole(roleFromMap);
                        System.out.println("New role is added.");
                        System.out.println();
                        return toBeChanged;
                    } else {
                        System.out.println("You enter a number larger than the role listed. If you want ta add then press 4.");
                    }
                } else {
                    System.out.println("You entered an invalid number. Enter from 1 to 4.");
                    continue;
                }
            }
            return toBeChanged;
        }
    }
}