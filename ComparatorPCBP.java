import java.util.Comparator;

class ComparatorPCBP implements Comparator<PCBP>{
    
    @Override
    public int Compare(Integer number1 , Integer number2){
        int valor = number1.compareTo(number2);

        if(valor > 0){
            return -1;
        }else if(valor < 0){
            return 1;
        }else{
            return 0;
        }
    }
}
