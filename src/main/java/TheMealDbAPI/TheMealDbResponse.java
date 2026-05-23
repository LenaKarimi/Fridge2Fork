package TheMealDbAPI;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Represents response structure from TheMealDb API.
 * It maps the JSON response and containing a list of meals.
 * It is used to deserialization of API responses into Java objects.
 * @param meals the list of meals returned from the API
 * @author Lena
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record TheMealDbResponse (List<TheMealDbDTO> meals){}

