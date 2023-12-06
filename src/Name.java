public class Name {
    private String firstName;
    private String middleName;
    private String lastName;

    public Name(String firstName, String lastName){
        this.firstName = firstName;
        this.middleName = "";
        this.lastName = lastName;
    }
    public Name(String firstName, String middleName, String lastName){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
    public String getFullName(){
        if (!middleName.equals("")) {
            return firstName+" "+middleName+" "+lastName;
        }
        return firstName+" "+lastName;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getInitials(){
        String initials = "";
        if(!this.firstName.equals(""))
            initials+=this.firstName.charAt(0);
        if (!this.middleName.equals(""))
            initials+=this.middleName.charAt(0);
        if (!this.lastName.equals(""))
            initials+=this.lastName.charAt(0);
        return initials;
    }

}
