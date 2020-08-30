package com.company;

import edu.rit.numeric.Histogram;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);
    public static List <String> rawData = new ArrayList <>(); // Wszystkie linijki z pliku, nie parsowane
    public static int n; // Liczba elementów wczytanych do rawData (Liczba wszystkich obiektów)
    private static String[] arg = new String[500]; // Pomocnicza  statyczna tablica pomocna przy parsowaniu
    private static ArrayList<Element> arguments = new ArrayList <>();
    public static int count_ALL_A, count_ALL_B, count_ALL_C = 0;

    private static boolean isFileValid(int n){ // sprawdzanie czy liczba elementow w pliku nie jest przypadkiem ujemna badz równa 0
        return n > 0;
    }

    private static void addToArrays(){

        // Pomocnicze tablice, jest ich tak dużo ponieważ Java oprócz wyrażeń regularnych nie posiada, żadnej wygodnej metody parsowania danych
        String[] arg_xL = new String[151]; //lower bound
        String[] arg_xU = new String[151]; //Upper bound
        String[] arg2_xL = new String[151]; //Lower bound 2nd arg
        String[] arg2_xU = new String[151]; //Upper bound 2nd arg
        String[] arg_dec = new String[151]; //Argumenty decyzyjne

    for(int i=0;i<n;i++) {
         Element element = new Element();
        arg2_xL[i] = StringUtils.deleteWhitespace(arg2_xL[i]);
        arg2_xL[i] = StringUtils.substringBetween(rawData.get(i), "|[", ")|");  // Parsowanie arg[] aby wydobyć dolną wartość oraz górna wartość dla drugiego argumentu

        arg2_xU[i] = StringUtils.substringAfter(arg2_xL[i],";");
        arg2_xL[i] = StringUtils.substringBefore(arg2_xL[i],";");
        arg2_xU[i] = StringUtils.deleteWhitespace(arg2_xU[i]);

    arg_dec[i] = StringUtils.substringAfterLast(rawData.get(i),"|");
    arg[i] = StringUtils.remove(rawData.get(i),"|"); // Parsowanie rawData[] aby wydobyć  wszystkie argumenty łącznie z decyzyjnym

    arg_xL[i] = arg[i].substring(0,arg[i].length()-1);                        // Parsowanie arg[] aby wydobyć dolną wartość oraz górna wartość dla pierwszego argumentu
    arg_xL[i] = StringUtils.deleteWhitespace(arg_xL[i]);
    arg_xL[i] = StringUtils.substringBetween(arg_xL[i], "[", ")");
    arg_xU[i] = StringUtils.substringAfter(arg_xL[i],";");
    arg_xL[i] = StringUtils.substringBefore(arg_xL[i],";");

    arg_xL[i]= arg_xL[i].replace(',','.');
    arg_xU[i]= arg_xU[i].replace(',','.');
    element.setLower_bound(Integer.parseInt(arg_xL[i]));  // tworzenie Obiektu typu Int, Int, Double, Double, Char
    element.setUpper_bound(Integer.parseInt(arg_xU[i]));

    arg2_xL[i]= arg2_xL[i].replace(',','.');
    arg2_xU[i]= arg2_xU[i].replace(',','.');

    element.setArg2_lower_bound(Float.parseFloat(arg2_xL[i]));
    element.setArg2_upper_bound(Float.parseFloat(arg2_xU[i]));

    element.setDecision_arg(arg_dec[i]);

        countAllDecisionArguments(element);
   arguments.add(element);
    } }

    private static double probabilityPv(double count_all ,int argA_){
        return argA_/count_all;
    }
    private static String finalProbability(ArrayList<Double> pv, ArrayList<Double> pd) { // petla, która wyliczy finalnie wartości prawdopodobieństw do podjęcia decyzji
        ArrayList<Double> finalprob = new ArrayList <>();
        int i =0;
        for (int j = 0; j < 3; j++){

                finalprob.add(j,(pv.get(i) * pd.get(j)) * (pv.get(i + 1) * pd.get(j)) * pd.get(j));
                i+=2;
            }

        int maxAt = 0;
        for (int z = 0; z < finalprob.size(); z++) {
            maxAt = finalprob.get(z) > finalprob.get(maxAt) ? z : maxAt;
        }
        return   indexChcker(maxAt);
    }


    private static void makeDecision(double a,double b, double c, double count_f ,double count_s, int argA, int argB, int argC, int arg2A, int arg2B, int arg2C )
    {// Metoda służacą do podejmowania decyzji
        /* 1. Wyznaczanie z próbki wartości P( vk | di ) oraz P( di ) dla wszystkich występujących vk oraz di.
         2. NBay( o* , di ) = P( v1 | di ) * ... * P( vn | di ) * P( di )
         Tablica pd  przechowuje prawdopodobienśtwa wartości decyzyjnych
         Tablica pv przechowuje prawdopodobieństwa wartości podstawowych w kolejności v1 od A, v2 od A, v1 od B etc

        */
        ArrayList<Double> pv = new ArrayList <>();
        ArrayList<Double> pd = new ArrayList <>();
        makeDecSplit(a, count_f, count_s, argA, arg2A, pv, pd, count_ALL_A);
        makeDecSplit2(b, count_f, count_s, argB, arg2B, pv, pd, count_ALL_B);
        makeDecSplit2(c, count_f, count_s, argC, arg2C, pv, pd, count_ALL_C);

        System.out.println(finalProbability(pv, pd));

// podzieliłem kod na dwie metody ponieważ wyglądało to znacznie mniej czytelnie niż jest teraz
        // w skrócie makeDecSplit wywołuje metode do obliczenia pv oraz oblicza pd po czym dodaje je do tablic
   }
    private static void makeDecSplit(double a, double count_f, double count_s, int argA, int arg2A, ArrayList <Double> pv, ArrayList <Double> pd, int count_all_a) {
        if(a == 0){
            pd.add(0D);
            pv.add(0D);
            pv.add(0D);
        }else {
            pd.add(a/ count_all_a);
           pv.add(probabilityPv(count_f,argA));
            pv.add(probabilityPv(count_s,arg2A));
        }
    }

    private static void makeDecSplit2(double b, double count_f, double count_s, int argB, int arg2B, ArrayList <Double> pv, ArrayList <Double> pd, int count_all_b) {
        makeDecSplit(b, count_f, count_s, argB, arg2B, pv, pd, count_all_b);
    }


    private static Element addArgument(){
        Element elem = new Element();
        System.out.println("Wprowadź dolną wartość dla pierwszego argumentu \n");
        elem.setLower_bound(sc.nextInt());
        System.out.println("Wprowadź górną wartość dla pierwszego argumentu \n");
        elem.setUpper_bound(sc.nextInt());
        System.out.println("Wprowadź dolną wartość dla drugiego argumentu  \n");
        elem.setArg2_lower_bound(sc.nextFloat());
        System.out.println("Wprowadź górną wartość dla drugiego argumentu \n");
        elem.setArg2_upper_bound(sc.nextFloat());
        return elem;
    }
    private static Element addRule(){
        Element elem;
        elem = addArgument();
        System.out.println("Wprowadź wartość dla argumentu decyzyjnego \n");
        elem.setDecision_arg(sc.next());
        return elem;
    }

    private static String indexChcker(int x){
        if( x==0){
            return "A";
        }
        else if( x==1){
            return "B";
        }
        else
            return "C";
    }
    public static boolean isArgumentsEqual(Argument elem_temp, Argument temp){
        return (temp.equals(elem_temp));
    } // metoda służacą do sprawdzenia czy podane argumenty są sobie równe

   public static void countAllDecisionArguments(Element elem){
        if(elem.getDecision_arg().equals("A")){
           count_ALL_A+=1;
        }
        else if(elem.getDecision_arg().equals("B")){
            count_ALL_B+=1;
        }
        else count_ALL_C+=1;
   }

    private static void countArguments(Element elem) {

       // int count_B_both = 0;
        // int count_A_both = 0;
        int count_A = 0; // liczba przypadków, w których argumentem decyzjnym dla danego obiektu była klasa A
        int count_B = 0; // liczba przypadków, w których argumentem decyzjnym dla danego obiektu była klasa B
        int count_C = 0; // // liczba przypadków, w których argumentem decyzjnym dla danego obiektu była klasa C
        int count_arg_a= 0, count_arg_b = 0, count_arg_c = 0; // Liczba argumentów, w których argumentem decyzjnym jest A|B|C
        int count_arg2_a = 0, count_arg2_b = 0, count_arg2_c = 0;
        int count_f_arg= 0; // Liczba wszystkich pierwszych argumentów, które są równe argumentowi podanemu przez użytkownika
        int count_s_arg = 0;  // Liczba wszystkich drugich argumentów, które są równe argumentowi podanemu przez użytkownika

        HashSet<Argument> tempArray  = new HashSet <>();
        Argument temp = new Argument(elem.getLower_bound(),elem.getUpper_bound());  // tworzenie tymczasowego modelu do przechowywania wyłącznie podstawowego argumentu
        Argument temp2 = new Argument(elem.getArg2_lower_bound(),elem.getArg2_upper_bound()); // // tworzenie tymczasowego modelu do przechowywania wyłącznie podstawowego argumentu

//System.out.println(arguments.size());
        for (Element elem3 : arguments) {

            Argument elem3_temp = new Argument(elem3.getLower_bound(),(elem3.getUpper_bound()));
            Argument elem3_temp2 = new Argument(elem3.getArg2_lower_bound(),(elem3.getArg2_upper_bound()));

          if((isArgumentsEqual(temp,elem3_temp)&&isArgumentsEqual(temp2,elem3_temp2))) { // Sprawdzanie czy obiekt z głównej tablicy posiada argumenty,
              // które występują w obiekcie podanym przez uzytkownika
                tempArray.add(elem3_temp);
              tempArray.add(elem3_temp2);

              if(elem3.getDecision_arg().equals("A")) { // zliczanie argumentów decyzyjnych oraz argumentów podstawowych zależnie od decyzji
                  count_A += 1;

                  count_arg_a+=1; count_arg2_a+=1;
                  count_s_arg+=1; count_f_arg+=1;
              }
              else if(elem3.getDecision_arg().equals("B")) {
                  count_B+=1;

                  count_arg_b+=1; count_arg2_b+=1;
                  count_s_arg+=1; count_f_arg+=1;
              }else{ count_C+=1;
                  count_s_arg+=1; count_f_arg+=1;
                  count_arg_c+=1; count_arg2_c+=1;}

            }
          else if (isArgumentsEqual(temp,elem3_temp)){
              tempArray.add(elem3_temp);
              if(elem3.getDecision_arg().equals("A")) {
                  count_A += 1;
                  count_f_arg+=1;
                  count_arg_a+=1;
              }
              else if(elem3.getDecision_arg().equals("B")) {
                  count_B += 1;
                  count_f_arg+=1;
                  count_arg_b+=1;
              }else{ count_C+=1;
              count_arg_c+=0;
                  count_f_arg+=1;}
            }
           else if(isArgumentsEqual(temp2,elem3_temp2)){
               tempArray.add((elem3_temp2));
              if(elem3.getDecision_arg().equals("A")) {
                  count_A += 1;
                  count_s_arg+=1;
                  count_arg2_a+=1;

              }else if(elem3.getDecision_arg().equals("B")) {
                  count_B += 1;
                  count_s_arg+=1;
                  count_arg2_b+=1;
              }else{ count_C+=1;
                  count_arg2_c+=1;
              count_s_arg+=1;}
            }
        }

        makeDecision(count_A,count_B,count_C,count_f_arg,count_s_arg,count_arg_a,count_arg_b,count_arg_c,count_arg2_a,count_arg2_b,count_arg2_c);



    }

    @Nullable
    public static void loadFile() throws IOException { // Meotda, która wczytuje plik do tablicy

        rawData = Files.readAllLines(Paths.get("learning.txt")); // Wczytanie pliku do tablicy rawData[]
        n = rawData.size(); // rozmiar danych w tablicy rawData[]

        if(isFileValid(n))
            System.out.println("Liczba wczytanych elementów: " + n +"\n");
        else
            System.out.println("Liczba elementów w tablicy jest równa 0 bądź mniejsza od 0");

    }

    public static void printMenu(){
        System.out.println("\n"+
        "W przypadku liczb zmiennoprzecinkowch należy wpisać przecinek pomiędzy cyframi, " +
        "ponieważ kompilator ma swoje zachcianki \n"+
        "--------------------------------------------------------------------------\n"+
        "1. Oblicz decyzje na podstawie argumentów użytkownika. \n"+
        "2. Dodanie nowej reguły. \n"+
        "3. Wyjście z programu. \n"
        );
    }

    public static void main(String[] args) throws IOException {
        int input;
        loadFile();
        addToArrays();

        do{
            printMenu();
            input = sc.nextInt();
            switch(input){
                case 1:
                    countArguments(addArgument());
                    break;
                case 2:
                   arguments.add(addRule());
                    break;
                case 3:
                    break;


            }



        } while(input!=3);

    }
}






