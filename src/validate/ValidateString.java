package validate;

public class ValidateString {
    private  int min;
    private  int max;

    public ValidateString(int min, int max) {
        this.min = min;
        this.max = max;
    }
    public boolean isVal( String value){
        return value.length() >= min &&  value.length() <= max;
    }
    public String errorMassage(){
        return "do dai phai trong khoan " + min + " -> "+ max +" ki tu !!!";
    }
}
