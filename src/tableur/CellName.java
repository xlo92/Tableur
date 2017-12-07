package tableur;

public class CellName {
    private String let;
    private Integer num;
    
    public CellName(String s){
        String[] part = s.split("(?<=\\D)(?=\\d)");
        this.let = part[0];
        this.num = Integer.parseInt(part[1]);
    }
    
    public String getLet(){
        return this.let;
    }
    
    public Integer getNum(){
        return this.num;
    }
    
    public boolean equals(CellName c){
        if(c.getLet().equals(this.let) && c.getNum() == this.num)
            return true;
        else
            return false;
    }
    
    public String getFullName(){
        return this.let + this.num;
    }
}
