package om.superquizz.diginamic.superquizz.model;

public enum TypeQuestion {
    SIMPLE("simple"),
    BONUS("bonus");

    private String name;

    private TypeQuestion(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public static TypeQuestion getByName(String name) {
        TypeQuestion[] types = values();
        for (TypeQuestion type : types) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }
}
