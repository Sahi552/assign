package com.example.login_page;

public class helperclass {
    private String name;
    private String age;
    private String phonenumber;
    private boolean ispremiem;


    public helperclass() {
    }

    // Constructor
    public helperclass(String name, String age, String phonenumber, boolean ispremiem) {
        this.name = name;
        this.age = age;
        this.phonenumber = phonenumber;
        this.ispremiem = ispremiem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public boolean isIspremiem() {
        return ispremiem;
    }

    public void setIspremiem(boolean ispremiem) {
        this.ispremiem = ispremiem;
    }
}
