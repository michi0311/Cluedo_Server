package kryonet;

/****************************
 * Created by Michael Marolt *
 *****************************/

public interface Callback<T> {

    void callback(T argument);
}
