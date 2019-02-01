package app.user;

public class PojoUser {
    private String name;
    public int id;
    
    public PojoUser(String name, int id){
        this.name = name;
        this.id = id;
    }
    
    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}