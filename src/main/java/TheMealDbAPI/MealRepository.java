package TheMealDbAPI;

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




   /** public TheMealDbDTO getMealById(String id) throws Exception, InterruptedException{
        String jsonResponse = client.filterByLookUpId(id); // anropar API för att får json sträng med alla recept som innehåller ingrediensen
        TheMealDbResponse response = mapper.readValue(jsonResponse, TheMealDbResponse.class); //gör om jsonresponse från sträng till objekt (mealresponse) som har eb lista med alla mealApiModel
        return response.meals().get(0); //får tillgång till listan med alla meals-som objekt då
    }*/

    /**NYTT , denna nya metoden är felsäker, tidigare kraschade under sökning om ett id
     * inte hittades exempelvis eller om fel uppstod, nu skickas bara null så att controller kan
     * hoppas över det trasiga och gå vidare till att hämta och visa de fungerande recepten för användaren.
     *
     */

    public TheMealDbDTO getMealById(String id){
        try{
            //hämtar JSON strängen från klienten
            String jsonResponse = client.filterByLookUpId(id);
            //NYTT här kontrolleras det om json är null, detta måste hända så att
            //programmet ej kraschar när den läser ett null värde
            if(jsonResponse == null){
                return null;
            }
            TheMealDbResponse response = mapper.readValue(jsonResponse, TheMealDbResponse.class);
            //NYTT här kontrolleras att listan faktiskt har data
            //detta för att ibland kan apiet svara med null om ett id är borttaget
            //att köra get.0 på en tomlista orsakar IndexOutOFBoundsException

            if(response.meals() !=null && !response.meals().isEmpty()){
            return response.meals().get(0);
            }

        } catch (Exception e){
            //NYTT här fångar catch tekniska fel ex nätverkspaus och json fel
            //felet loggas i konsolen men syns ej för användare
            //detta ska göra så att sökprocessen inte dör mitt i en lista av 50 recept
            System.err.println("LOGG: Hoppar över recept ID: " + id + "pga fel: "+ e.getMessage());

        }
        //hamnvia här har något gått fel och null skickas till controllern
        return null;

    }

}
