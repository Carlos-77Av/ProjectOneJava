import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProjectOne {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        //Function that request the information and store in a txt file
        saveData(read);
        //Running the function getData which extract the information from the txt file and return it in an ArrayList
        ArrayList<String> grades = getData();
        //Running function that process the information saved on grade.

        //Running function that prints the statistics
        printInformation(processData(grades), grades);
    }

    public static void saveData(Scanner read){
        try{
            File myDataBase = new File("database.txt");
            PrintWriter pw = new PrintWriter(myDataBase);
            //Capturing information and saving into the txt file
            try{
                for (int i = 1; i <= 5; i++){
                    System.out.print("Enter the " + i + "° student name: ");
                    String studentName = read.next();
                    //Write the Student Name in the txt file
                    pw.println(studentName);
                    System.out.print("Student grade: ");
                    double grade = read.nextDouble();
                    //Verify if the grade is between the range allow
                    if (grade >= 0 && grade <= 10){
                        System.out.println();
                        pw.println(grade);
                    }
                    else{
                        throw new InputMismatchException();
                    }
                }
            }catch (InputMismatchException ex){
                System.out.println();
                System.out.println("The grade must be a number greater or equal to 0 and lower or equal to 10");
                System.exit(0);
            }
            pw.close();
        }catch(IOException ex){
            System.out.println("There have been an error with de txt file.");
        }
    }

    public static ArrayList<String> getData() {
        //Declaration of variables
        ArrayList<String> grades = new ArrayList<>();
        try {
            //Reading information from the txt file
            File myDataBase = new File("database.txt");
            Scanner reader = new Scanner(myDataBase);

            //Extracting and storing grades on a ArrayList
            while(reader.hasNextLine()){
                reader.nextLine();
                grades.add(reader.nextLine());
            }
        }catch (FileNotFoundException ex){
            System.out.println("The txt file was not found.");
        }
        return grades;
    }

    public static String[][] processData(ArrayList<String> grades) {
        int counter = 0, previousCounter = 0;
        int counterLR = 0, previousCounterLR = 0;//I declared variables here because I needed the values of the ArrayList grades
        double min = Double.parseDouble(grades.get(0)), max = min, actual, avg = 0;

        ArrayList<String> mostRepGrade = new ArrayList<>();
        ArrayList<String> lessRepGrade = new ArrayList<>();
        //Looping the data extracted from the txt file
        for (int i = 0; i < grades.size(); i++) {
            //It variable is used because it will be used a lot of time, thus it just be converted once.
            actual = Double.parseDouble(grades.get(i));
            avg += actual;
            for (Object grade : grades) {
                if (grades.get(i).equals(grade)) {
                    //Counter counts how many times a value is repeated
                    counter++;
                } else {
                    //The counterLR variable contains how many times a values has not been repeated
                    //In order to know which is the value less repeated
                    counterLR++;
                }

                //Checking the min and max value
                min = Math.min(min, actual);
                max = Math.max(max, actual);
            }

            //Validation to know if the the actual value of the ArrayList is greater than the previous one
            if (counter > previousCounter) {
                previousCounter = counter;
                //The ArrayList is cleared
                mostRepGrade.clear();
                //Add the new highest value
                mostRepGrade.add(grades.get(i));
            }
            //It conditions check if the actual value of the ArrayList is equal to the previous
            else if (counter == previousCounter) {
                //Verify if the grade it is not on the mostRepGrade (ArrayList)
                //If it is not there, it will be added
                if (!mostRepGrade.contains(grades.get(i))) {
                    mostRepGrade.add(grades.get(i));
                }
            }

            //Validation to know if the the actual value of the ArrayList is greater than the previous one
            if (counterLR > previousCounterLR) {
                previousCounterLR = counterLR;
                //The ArrayList is cleared
                lessRepGrade.clear();
                lessRepGrade.add(grades.get(i));
            }
            //It conditions check if the actual value of the ArrayList is equal to the previous
            else if (counterLR == previousCounterLR) {
                //Verify if the grade it is not on the mostRepGrade (ArrayList)
                //If it is not there, it will be added
                if (!lessRepGrade.contains(grades.get(i))) {
                    lessRepGrade.add(grades.get(i));
                }
            }
            counter = 0;
            counterLR = 0;
        }
        avg /= grades.size();

        //Creating arrays for converting ArrayList to Array and returning as a parameter
        String[] myMostRep = new String[mostRepGrade.size()];
        String[] myLessRep = new String[lessRepGrade.size()];


        myMostRep = mostRepGrade.toArray(myMostRep);
        myLessRep = lessRepGrade.toArray(myLessRep);


        return new String[][]{
                {Double.toString(min), Double.toString(max), Double.toString(avg)},
                myMostRep,
                myLessRep
        };
    }

    public static void printInformation(String[][] data, ArrayList<String> grades){
        //These variables are used in order to clarify the code
        //data[0] contains the following data: {min, max, avg}
        //data[1] contains the most repeated grades
        //data[2] contains the less repeated grades
        String min = data[0][0];
        String max = data[0][1];
        Double avg = Double.parseDouble(data[0][2]);
        String[] MostRepGrade = data[1];
        String[] LessRepGrade = data[2];

        System.out.println(":::: Statistic ::::");
        System.out.println("The lowest grade is: " + min);
        System.out.println("The highest grade is: " + max);
        System.out.println("The average of grade is: " + String.format("%.2f", avg));

        //Checking the ArrayList that contains the highest grades
        if (MostRepGrade.length > 0 && MostRepGrade.length < grades.size()){
            System.out.print("The most repeated grades are: ");
            //Looping the ArrayList for printing all the grades
            for (String grade: MostRepGrade){
                //It verifies if it is the first value in order to avoid use comma before the first value
                if (grade.equals(MostRepGrade[0])){
                    System.out.print(grade);
                }
                else{
                    System.out.print(", " + grade + " ");
                }
            }
            System.out.println();
        }
        else if (MostRepGrade.length == grades.size()){ //I decided to write a message if all the grades were different
            System.out.println("The most repeated grades are: all grades are different");
        }

        //Checking the ArrayList that contains the lowest grades
        //And comparing the amount of both ArrayList for checking
        if (LessRepGrade.length > 0 && LessRepGrade.length < grades.size()){
            System.out.print("The less repeated grade are: ");
            //Looping the ArrayList for printing all the grades
            for (String grade: LessRepGrade){
                //It verifies if it is the first value in order to avoid use comma before the first value
                if (grade.equals(LessRepGrade[0])){
                    System.out.print(grade);
                }
                else{
                    System.out.print(", " + grade + " ");
                }
            }
            System.out.println();
        }
        else if (LessRepGrade.length == grades.size()){ //I decided to write a message if all the grades were different
            System.out.println("The less repeated grades are: all grades are different");
        }
    }
}
