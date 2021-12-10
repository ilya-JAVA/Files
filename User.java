package files;

import java.util.List;

public class User {
    String name;
    String surname;
    List<String> phoneNumber;
    String mail;
    List<String> role;

    User(String name, String lastName, List phoneNumber, String mail, List role){
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.role = role;
    }

    @Override
    public String toString(){
        return "Name:" + name +
                " Surname:" + surname +
                " PhoneNumber:" + phoneNumber.toString() +
                " Mail:" + mail +
                " Role:" + role.toString();
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public List<String> getPhoneNumber(){
        return phoneNumber;
    }

    public String getMail(){
        return mail;
    }

    public List<String> getRole(){
        return role;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public void setPhoneNumber(List<String> phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public void setRole(List<String> role){
        this.role = role;
    }
}