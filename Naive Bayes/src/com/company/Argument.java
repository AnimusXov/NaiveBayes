package com.company;

import java.util.Objects;

public class Argument  {
// Struktura danych służąca do ułtawienia sobie życia (Przechowywanie wyłącznie argumentów podstawowych)
    private double lower_bound;
    private double upper_bound;


    public Argument(double lower_bound, double upper_bound) {
        this.lower_bound = lower_bound;
        this.upper_bound = upper_bound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return Double.compare(argument.getLower_bound(), getLower_bound()) == 0 &&
                Double.compare(argument.getUpper_bound(), getUpper_bound()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLower_bound(), getUpper_bound());
    }

    public double getLower_bound() {
        return lower_bound;
    }

    public void setLower_bound(int lower_bound) {
        this.lower_bound = lower_bound;
    }

    public double getUpper_bound() {
        return upper_bound;
    }

    public void setUpper_bound(int upper_bound) {
        this.upper_bound = upper_bound;
    }
    public String toString() {
        return
                "[" + lower_bound + "; " + upper_bound + ")";
    }
}
