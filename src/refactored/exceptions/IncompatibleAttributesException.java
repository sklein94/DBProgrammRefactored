package refactored.exceptions;
/**
 * Diese Exception wird geworfen, wenn einen Mitarbeiterattribut ein Wert zugewiesen werden soll, der nicht den Filtern entspricht.
 */
public class IncompatibleAttributesException extends Exception{
    public IncompatibleAttributesException(String message){
        super(message);
    }
}
