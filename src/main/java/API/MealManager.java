package API;
import API.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MealManager {
    private ApiClient client;

    public MealManager(ApiClient client){
        this.client = client;
    }

    public void searchForRecepie(String ingredient, String category){
        try{
            String jsonResponse = client.filterByIngredient(ingredient); //skickar förfrågan med ingrediens som hämtar Json strängen, får lista med alla recept som innehåller ingredient

            ObjectMapper mapper = new ObjectMapper(); // skapar mapper objektet
            MealResponse responseIngredient = mapper.readValue(jsonResponse, MealResponse.class); //gör om denna klass från sträng till objekt
            List<MealApiModel> allMeals = responseIngredient.getMeals(); //får tillgång till listan med alla meals-som objekt då

            //går igenom alla recept
            for (MealApiModel meal : allMeals){
                //hämtar specifikt id för recpektive recept
                String idMeal = meal.getIdMeal();

                String jsonResponseIDMeal = client.filterByLookUpId(idMeal); //skickar ny förfrågan med id, för specifikt recept med alla ingredienser, lookup retunerar en lista med alla objekt drf 0
                MealResponse responseIDMeal = mapper.readValue(jsonResponseIDMeal,MealResponse.class);
                MealApiModel specificMeal = responseIDMeal.getMeals().get(0);

                if (specificMeal.getStrArea().equalsIgnoreCase(category)){
                    specificMeal.buildIngredientLists();// bygger listorna för det specifika objektet
                    List<String> ingredientList = specificMeal.getIngredientList(); //hämtar lista för ingredienser för specifikt recept
                    List<String> measureList = specificMeal.getMeasureList(); //hämtar lista för mått för specifikt recept

                    System.out.println("Catogory " + specificMeal.getStrArea()); //går igenom alla recept och måll och skrver ut dem
                    for (int i = 0; i<ingredientList.size(); i++){
                        System.out.println(ingredientList.get(i) + " - " + measureList.get(i));
                    }
                    System.out.println();
                    System.out.println(specificMeal.getStrInstructions() + "\n"); // skriver ut  instruktioner
                }

            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
