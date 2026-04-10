package Model;

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
    Venezulan (CuisineGroup.latin_american),
    Vietnamese (CuisineGroup.asian);

    private final CuisineGroup cuisineGroup;

    Cuisine(CuisineGroup cuisineGroup){
        this.cuisineGroup=cuisineGroup;
    }

    public CuisineGroup getCuisineGroup() {
        return cuisineGroup;
    }
}
