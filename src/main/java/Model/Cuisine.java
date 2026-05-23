package Model;

/**
 * Represents specific world cuisines.
 * Each cuisine belongs to a broader CuisineGroup
 * @author Lena
 */
public enum Cuisine {
    Algerian (CuisineGroup.middle_eastern),
    American (CuisineGroup.american),
    Argentinian(CuisineGroup.latin_american),
    British (CuisineGroup.european),
    Canadian (CuisineGroup.american),
    Chinese (CuisineGroup.asian),
    Croatian (CuisineGroup.european),
    Dutch (CuisineGroup.european),
    Egyptian (CuisineGroup.middle_eastern),
    Filipino (CuisineGroup.asian),
    French (CuisineGroup.european),
    Greek (CuisineGroup.european),
    Indian (CuisineGroup.asian),
    Irish (CuisineGroup.european),
    Italian (CuisineGroup.european),
    Jamaican (CuisineGroup.african),
    Japanese (CuisineGroup.asian),
    Kenyan (CuisineGroup.african),
    Malaysian (CuisineGroup.asian),
    Mexican (CuisineGroup.latin_american),
    Moroccan (CuisineGroup.middle_eastern),
    Norwegian (CuisineGroup.european),
    Polish (CuisineGroup.european),
    Portuguese (CuisineGroup.european),
    Russian (CuisineGroup.european),
    SaudiArabian (CuisineGroup.middle_eastern),
    Slovakian (CuisineGroup.european),
    Spanish (CuisineGroup.european),
    Syrian (CuisineGroup.middle_eastern),
    Thai(CuisineGroup.asian),
    Tunisian (CuisineGroup.middle_eastern),
    Turkish (CuisineGroup.middle_eastern),
    Ukrainian (CuisineGroup.european),
    Uruguayan (CuisineGroup.latin_american),
    Venezuelan (CuisineGroup.latin_american),
    Vietnamese (CuisineGroup.asian);

    private final CuisineGroup cuisineGroup;

    /**
     * Constructs a cuisine with its associated group
     * @param cuisineGroup the broader geographic group
     */
    Cuisine(CuisineGroup cuisineGroup){
        this.cuisineGroup=cuisineGroup;
    }

    /**
     * Returs the cuisine group it belongs to.
     * @return The cuisine group
     */
    public CuisineGroup getCuisineGroup() {
        return cuisineGroup;
    }
}
