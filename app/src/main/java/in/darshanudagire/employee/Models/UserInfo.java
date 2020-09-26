package in.darshanudagire.employee.Models;

public class UserInfo {

    String name,empID,email,location,number,designation,profile;

    public UserInfo(String name, String email, String location, String number) {
        this.name = name;
        this.email = email;
        this.location = location;
        this.number = number;
    }

    public UserInfo(String name, String empID, String email, String location, String number, String designation, String profile) {
        this.name = name;
        this.empID = empID;
        this.email = email;
        this.location = location;
        this.number = number;
        this.designation = designation;
        this.profile = profile;
    }

    public UserInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
