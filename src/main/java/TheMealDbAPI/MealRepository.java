package TheMealDbAPI;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Responsible for handling communication between the HTTP client and the rest of the application.
 * Retrieves raw JSON data from the HTTPTheMealDbClient and converts it into Java objects using Jackson.
 * It abstracts away JSON handling so that the rest of the application works with domain objects instead of raw API responses.
 * @author Intisaar
 * @author Lena
 */
public class MealRepository {
    private HttpTheMealDbClient client;
    private ObjectMapper mapper;

    /**
     * Constructs a new MealRepository with the given HTTP client.
     * @param client the HTTP client used to communicate with the API
     */
    public MealRepository(HttpTheMealDbClient client){
        this.client=client;
        this.mapper = new ObjectMapper();
    }

    /**
     * Retrieves meals that cointain a specific ingredient.
     * @param mainIngredient the ingredient to search for
     * @return a list of ThemealDbDTO objects matching the ingredient
     * @throws Exception id JSON parsing fails
     * @throws InterruptedException if the request is interrupted
     */
    public List<TheMealDbDTO> getMealsByIngredient(String mainIngredient) throws Exception, InterruptedException{
        String jsonResponse = client.filterByIngredient(mainIngredient);
        TheMealDbResponse response = mapper.readValue(jsonResponse, TheMealDbResponse.class);
        return response.meals();
    }

    /**
     * Retrieves a single meal by its ID.
     * It returns null id the API response is empty or invalid
     * and catches and logs exeptions instead if propagating them
     * @param id the uniqye meal ID
     * @return a TheMealDbDTO if found, otherwise null
     */
    public TheMealDbDTO getMealById(String id){
        try{
            String jsonResponse = client.filterByLookUpId(id);

            if(jsonResponse == null){
                return null;
            }
            TheMealDbResponse response = mapper.readValue(jsonResponse, TheMealDbResponse.class);

            if(response.meals() !=null && !response.meals().isEmpty()){
                return response.meals().get(0);
            }

        } catch (Exception e){
            System.err.println("LOGG: Hoppar över recept ID: " + id + "pga fel: "+ e.getMessage());
        }
        return null;
    }
}
