package com.example.login_page;

public class helperclass {
    private String name;
    private String age;
    private String phonenumber; // Updated to match "phonenumber" in warning
    private boolean ispremiem;  // Updated to match "ispremiem" in warning

    // No-argument constructor required for Firebase
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

    public String getPhonenumber() { // Updated getter
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public boolean isIspremiem() {
        return ispremiem;
    }

    public void setIspremiem(boolean ispremiem) { // Updated setter
        this.ispremiem = ispremiem;
    }
}
