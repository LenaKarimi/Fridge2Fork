package API;

public class Main {
    public static void main (String[] args){
        ApiClient client = new ApiClient();
        MealManager mealManager = new MealManager(client);
        mealManager.searchForRecepie("chicken", "Italian");
        //mealManager.searchForRecepie("Lamb", "Turkish");
    }
}
