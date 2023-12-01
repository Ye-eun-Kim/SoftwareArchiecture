public class Manager {


    public static void main(String[] args){
        Name name = new Name("Yeeun", "Kim");
        String fn = name.getFirstName();
        String ini = name.getInitials();
        System.out.println(fn);
        System.out.println(ini);
    }
}
