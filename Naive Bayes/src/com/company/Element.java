package com.company;




public class Element  {
    // Struktura danych służąca do utrudnienia sobie życia (Przechowywanie osobno wartości dla podstawowych argumentów)
    private int lower_bound;
    private int upper_bound;
    private float arg2_lower_bound;
    private float arg2_upper_bound;
    private String decision_arg;


    public Element(int lower_bound, int upper_bound, float arg2_lower_bound, float arg2_upper_bound, String decision_arg) {
        this.arg2_lower_bound = arg2_lower_bound;
        this.arg2_upper_bound = arg2_upper_bound;
        this.decision_arg = decision_arg;

    }

    public Element(int lower_bound, int upper_bound, float arg2_lower_bound, float arg2_upper_bound) {
        this.lower_bound = lower_bound;
        this.upper_bound = upper_bound;
        this.arg2_lower_bound = arg2_lower_bound;
        this.arg2_upper_bound = arg2_upper_bound;
    }

    public Element(int lower_bound, int upper_bound){
        this.lower_bound = lower_bound;
        this.upper_bound = upper_bound;
    }
    public int getLower_bound() {
        return lower_bound;
    }

    public void setLower_bound(int lower_bound) {
        this.lower_bound = lower_bound;
    }

    public int getUpper_bound() {
        return upper_bound;
    }

    public void setUpper_bound(int upper_bound) {
        this.upper_bound = upper_bound;
    }

    public Element(float arg2_lower_bound, float arg2_upper_bound) {

        this.arg2_lower_bound = arg2_lower_bound;
        this.arg2_upper_bound = arg2_upper_bound;
    }

    public Element() {
    }

    public Element(String decision_arg) {
        this.decision_arg = decision_arg;
    }



    public float getArg2_lower_bound() {
        return arg2_lower_bound;
    }

    public void setArg2_lower_bound(float arg2_lower_bound) {
        this.arg2_lower_bound = arg2_lower_bound;
    }

    public float getArg2_upper_bound() {
        return arg2_upper_bound;
    }

    public void setArg2_upper_bound(float arg2_upper_bound) {
        this.arg2_upper_bound = arg2_upper_bound;
    }

    public String getDecision_arg() {
        return decision_arg;
    }

    public void setDecision_arg(String decision_arg) {
        this.decision_arg = decision_arg;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return
                Float.compare(element.getArg2_lower_bound(), getArg2_lower_bound()) == 0 &&
                Float.compare(element.getArg2_upper_bound(), getArg2_upper_bound()) == 0;
    }

    public String toString(){
        return
                "["+lower_bound+"; "+upper_bound+")" +
                "["+arg2_lower_bound+"; "+arg2_upper_bound+") " + decision_arg ;
    }
}
