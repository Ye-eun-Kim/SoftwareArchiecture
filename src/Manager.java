public class Manager {


    public static void main(String[] args){
        Name name = new Name("Yeeun", "Kim");
        String fn = name.getFirstName();
        String ini = name.getInitials();
        System.out.println(fn);
        System.out.println(ini);

        Competitor yeeun = new Competitor(100,23,name,"yk23","free","2,4,5,4,2",3.4);
        System.out.println(yeeun.getFullDetails());
        System.out.println(yeeun.getShortDetails());
    }
}
