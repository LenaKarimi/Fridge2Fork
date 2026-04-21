package TheMealDbAPI;

import DTO.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

// klassen mellan rådata och resten av programmet,
// den tar json texten från http klassen, och gör om till objekt med jacksson
//resterande del av programmet behöver ej tänka på jsonfilerna utan det kommer hanteras som objekt då

public class MealRepository {
    private HttpTheMealDbClient client;
    private ObjectMapper mapper;

    public MealRepository(HttpTheMealDbClient client){
        this.client=client;
        this.mapper = new ObjectMapper(); //skapar mapper objektet i konstruktorn
    }

    public List<TheMealDbDTO> getMealsByIngredient(String mainIngredient) throws Exception, InterruptedException{
        String jsonResponse = client.filterByIngredient(mainIngredient); // anropar API för att får json sträng med alla recept som innehåller ingrediensen
        TheMealDbResponse response = mapper.readValue(jsonResponse, TheMealDbResponse.class); //gör om jsonresponse från sträng till objekt (mealresponse) som har eb lista med alla mealApiModel
        return response.meals(); //får tillgång till listan med alla meals-som objekt då
    }
    public TheMealDbDTO getMealById(String id) throws Exception, InterruptedException{
        String jsonResponse = client.filterByLookUpId(id); // anropar API för att får json sträng med alla recept som innehåller ingrediensen
        TheMealDbResponse response = mapper.readValue(jsonResponse, TheMealDbResponse.class); //gör om jsonresponse från sträng till objekt (mealresponse) som har eb lista med alla mealApiModel
        return response.meals().get(0); //får tillgång till listan med alla meals-som objekt då
    }
}
