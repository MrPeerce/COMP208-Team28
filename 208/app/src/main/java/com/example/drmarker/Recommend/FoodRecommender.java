/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.drmarker.Recommend;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Zisen Lin. lmo.
 */
public class FoodRecommender {
    
    private ArrayList<Food> foods = new ArrayList<>(); // arraylist for store the food.
    private static ArrayList<Food> recommendBreakfast = new ArrayList<>();
    private static ArrayList<Food> recommendLunch = new ArrayList<>();
    private static ArrayList<Food> recommendDinner = new ArrayList<>();
    
    public FoodRecommender(InputStream foodStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(foodStream));

            String str = new String();
            while((str = bufferedReader.readLine()) != null) {
                // each line: name, calories, type, breakfast, lunch, dinner.
                String[] foodInfo = str.split(",");
                String name = foodInfo[0];
                int unit = Integer.parseInt(foodInfo[1]);
                double calories = Double.parseDouble(foodInfo[2]);
                double protein = Double.parseDouble(foodInfo[3]);
                double carbohydrate = Double.parseDouble(foodInfo[4]);
                double fat = Double.parseDouble(foodInfo[5]);
                String type = foodInfo[6];
                boolean forBreakfast = Boolean.parseBoolean(foodInfo[7]);

                Food food = new Food(name, unit, calories, protein, carbohydrate, fat, type, forBreakfast);
                foods.add(food); // add into arraylist.
            }

            // close.
            foodStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.getStackTrace();
            System.out.println("Error for checking file's exist.");
        } catch (NumberFormatException e) {
            e.getStackTrace();
            System.out.println("Error for raw data format.");
        }
    }
    
    public ArrayList<Food> getFoods() {
        return foods;
    }



    private static void recommendBreakfast(String bodyAim,ArrayList<Food> foods) {
        switch (bodyAim) {
            case "StrengthenBody":
                recommendBreakfast.add(randomSelect("d", true, null,foods));
                recommendBreakfast.add(randomSelect("-d", true, null,foods));
                recommendBreakfast.add(randomSelect("-d", false, "c",foods));
                // C:P:F = 65% : 20% : 15%.
                break;
            case "BuildMuscle":
                // C:P:F = 60% : 20% : 20%.
                recommendBreakfast.add(randomSelect("d", true, null,foods));
                recommendBreakfast.add(randomSelect("-d", true, "p",foods));
                break;
            case "KeepBalance":
                // C:P:F = 55% : 15% : 30%.
                recommendBreakfast.add(randomSelect("d", true, null,foods));
                recommendBreakfast.add(randomSelect("-d", true, null,foods));
                break;
            case "LoseWeight":
                // C:P:F = 45% : 30% : 25%.
                recommendBreakfast.add(randomSelect("d", true, null, foods));
                recommendBreakfast.add(randomSelect("-d", true, "-c", foods));
                recommendBreakfast.add(randomSelect("f", false, null, foods));
                break;
        }
    }

    private static void recommendLunch(String bodyAim, ArrayList<Food> foods) {
        switch (bodyAim) {
            case "StrengthenBody":
                // C:P:F = 65% : 20% : 15%.
                recommendLunch.add(randomSelect("d", false, null, foods));
                recommendLunch.add(randomSelect("v", false, null, foods));
                recommendLunch.add(randomSelect("s", false, null, foods));
                break;
            case "BuildMuscle":
                // C:P:F = 60% : 20% : 20%.
                recommendLunch.add(randomSelect("d", false, "-f", foods));
                recommendLunch.add(randomSelect("m", false, "p", foods));
                recommendLunch.add(randomSelect("-df", false, null, foods));
                break;
            case "KeepBalance":
                // C:P:F = 55% : 15% : 30%.
                recommendLunch.add(randomSelect("d", false, null, foods));
                recommendLunch.add(randomSelect("m", false, null, foods));
                recommendLunch.add(randomSelect("v", false, null, foods));
                break;
            case "LoseWeight":
                // C:P:F = 45% : 30% : 25%.
                recommendLunch.add(randomSelect("v", false, null, foods));
                recommendLunch.add(randomSelect("v", false, null, foods));
                break;
        }
    }

    private static void recommendDinner(String bodyAim,ArrayList<Food> foods) {
        switch (bodyAim) {
            case "StrengthenBody":
                // C:P:F = 65% : 20% : 15%.
                recommendDinner.add(randomSelect("f", false, null, foods));
                recommendDinner.add(randomSelect("-df", false, null, foods));
                recommendDinner.add(randomSelect("d", false, null, foods));
                break;
            case "BuildMuscle":
                // C:P:F = 60% : 20% : 20%.
                recommendDinner.add(randomSelect("m", false, null, foods));
                recommendDinner.add(randomSelect("f", false, null, foods));
                break;
            case "KeepBalance":
                // C:P:F = 55% : 15% : 30%.
                recommendDinner.add(randomSelect("d", false, null, foods));
                recommendDinner.add(randomSelect("f", false, null, foods));
                recommendDinner.add(randomSelect("-df", false, null, foods));
                break;
            case "LoseWeight":
                // C:P:F = 45% : 30% : 25%.
                recommendDinner.add(randomSelect("f", false, null, foods));
                recommendDinner.add(randomSelect("f", false, null, foods));
                break;
        }
    }

    private static Food randomSelect(String type, boolean morning, String focus, ArrayList<Food> foods) {
        ArrayList<Food> tempFoods = new ArrayList<>();
        if (type.startsWith("-")) {
            for (Food food: foods) {
                if (morning) {
                    if (type.length() == 2) {
                        if (!food.getType().equalsIgnoreCase(String.valueOf(type.charAt(1))) && food.suitOnBreakFast()) {
                            tempFoods.add(food);
                        }
                    } else if (type.length() == 3) {
                        if (!food.getType().equalsIgnoreCase(String.valueOf(type.charAt(1))) && !food.getType().equalsIgnoreCase(String.valueOf(type.charAt(2))) && food.suitOnBreakFast()) {
                            tempFoods.add(food);
                        }
                    }
                } else {
                    if (!food.getType().equalsIgnoreCase(String.valueOf(type.charAt(1)))) {
                        tempFoods.add(food);
                    }
                }
            }
        } else {
            for (Food food: foods) {
                if (morning) {
                    if (food.getType().equalsIgnoreCase(type) && food.suitOnBreakFast()) {
                        tempFoods.add(food);
                    }
                } else {
                    if (food.getType().equalsIgnoreCase(type)) {
                        tempFoods.add(food);
                    }
                }
            }
        }

        if (focus != null) {
            Iterator<Food> iterator = tempFoods.iterator();
            while (iterator.hasNext()) {
                Food food = iterator.next();
                switch (focus) {
                    case "c":
                        if (food.getCarbohydrate() < food.getProtein() || food.getCarbohydrate() < food.getFat()) {
                            iterator.remove();
                        }
                        break;
                    case "p":
                        if (food.getProtein() < food.getCarbohydrate() || food.getProtein() < food.getFat()) {
                            iterator.remove();
                        }
                        break;
                    case "f":
                        if (food.getFat() < food.getCarbohydrate() || food.getFat() < food.getProtein()) {
                            iterator.remove();
                        }
                        break;
                    case "-c":
                        if (food.getCarbohydrate() > food.getProtein() || food.getCarbohydrate() > food.getFat()) {
                            iterator.remove();
                        }
                        break;
                    case "-p":
                        if (food.getProtein() > food.getCarbohydrate() || food.getProtein() > food.getFat()) {
                            iterator.remove();
                        }
                        break;
                    case "-f":
                        if (food.getProtein() > food.getCarbohydrate() || food.getProtein() > food.getFat()) {
                            iterator.remove();
                        }
                        break;
                }
            }
        }

        int ranItem = (int)(Math.random() * tempFoods.size());
        return tempFoods.get(ranItem);
    }


    // ************* BMR.
    public static double standardBMR(String gender, double weight, double height, int age) {
        if (gender.equalsIgnoreCase("male")) {
            return 13.8 * weight + 5 * height - 6.8 * age + 66;
        } else {
            return 9.6 * weight + 1.8 * height -4.7 * age + 655;
        }
    }

    public static double dailyCalories(int activeType,double BMR){
        double multifactor = 0;
        switch (activeType) {
            case 1:
                System.out.println("You are a Sedentary people (multi-factor=1.2);");
                multifactor = 1.2;
                break;
            case 2:
                System.out.println("You are a Lightly active people (multi-factor=1.375);");
                multifactor = 1.375;
                break;
            case 3:
                System.out.println("You are a Moderately active people (multi-factor=1.55);");
                multifactor = 1.55;
                break;
            case 4:
                System.out.println("You are a Very active people (multi-factor=1.725);");
                multifactor = 1.725;
                break;
            case 5:
                System.out.println("You are a Extremely active people (multi-factor=1.9);");
                multifactor = 1.9;
                break;
        }
        DecimalFormat df = new DecimalFormat("0.000");
        return Double.parseDouble(df.format(multifactor*BMR));
    }

    // ************* END OF BMR.

    // ************* SM.
    public static double SM(String gender, double weight, double height, double waist, double hip, int age) {
        if (gender.equalsIgnoreCase("male")) {
            return 39.5 + 0.665 * weight - 0.185 * waist - 0.418 * hip - 0.08 * age;
        } else {
            return 2.89 + 0.255 * weight - 0.175 * hip - 0.038 * age + 0.118 * height;
        }
    }

    public static String analysisSM(double SM, String gender, int age) {
        if (gender.equalsIgnoreCase("male")) {
            if (age >= 18 && age <= 40) {
                if (SM < 33.4) {
                    return "Low";
                } else if (SM >= 33.4 && SM < 39.5) {
                    return "Normal";
                } else if (SM >= 39.5 && SM < 44.2) {
                    return "High";
                } else {
                    return "Very High";
                }
            } else if (age >= 41 && age <= 60) {
                if (SM < 33.2) {
                    return "Low";
                } else if (SM >= 33.2 && SM < 39.3) {
                    return "Normal";
                } else if (SM >= 39.3 && SM < 44.0) {
                    return "High";
                } else {
                    return "Very High";
                }
            } else {
                if (SM < 33.0) {
                    return "Low";
                } else if (SM >= 33.0 && SM < 38.8) {
                    return "Normal";
                } else if (SM >= 38.8 && SM < 43.5) {
                    return "High";
                } else {
                    return "Very High";
                }
            }
        } else {
            // female.
            if (age >= 18 && age <= 40) {
                if (SM < 24.4) {
                    return "Low";
                } else if (SM >= 24.4 && SM < 30.3) {
                    return "Normal";
                } else if (SM >= 30.3 && SM < 35.3) {
                    return "High";
                } else {
                    return "Very High";
                }
            } else if (age >= 41 && age <= 60) {
                if (SM < 24.2) {
                    return "Low";
                } else if (SM >= 24.2 && SM < 30.4) {
                    return "Normal";
                } else if (SM >= 30.4 && SM < 35.4) {
                    return "High";
                } else {
                    return "Very High";
                }
            } else {
                if (SM < 24.0) {
                    return "Low";
                } else if (SM >= 24.0 && SM < 29.9) {
                    return "Normal";
                } else if (SM >= 29.9 && SM < 34.9) {
                    return "High";
                } else {
                    return "Very High";
                }
            }
        }
    }
    // ************** END OF SM.

    // ************** BMI.
    public static double BMI(double weight, double height) {
        double m_height = height / 100;
        DecimalFormat df = new DecimalFormat("0.000");
        return Double.parseDouble(df.format(weight / (m_height * m_height)));
    }

    public static String analysisBMI(double BMI) {
        if (BMI < 18.5) {
            return "Underweight";
        } else if (BMI >= 18.5 && BMI < 25) {
            return "Normalweight";
        } else if (BMI >= 25 && BMI < 30) {
            return "Overweight";
        } else if (BMI >= 30) {
            return "Obese";
        }
        return "";
    }
    // *************** END OF BMI.

    // *************** BF.
    public static double bodyFat(double BMI, int age, String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 1.39 * BMI + 0.16 * age - 10.34 - 9;
        } else {
            return 1.39 * BMI + 0.16 * age - 9;
        }
    }

    public static String analysisBF(double BF, String gender, int age) {
        if (gender.equalsIgnoreCase("female")) {
            if (age == 18) {
                if (BF <= 16) {
                    return "Underfat";
                } else if (BF > 16 && BF <= 30) {
                    return "Healthy";
                } else if (BF > 30 && BF <= 35) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else if (age == 19) {
                if (BF <= 18) {
                    return "Underfat";
                } else if (BF > 18 && BF <= 31) {
                    return "Healthy";
                } else if (BF > 31 && BF <= 36) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else if (age >= 20 && age <= 39) {
                if (BF <= 20) {
                    return "Underfat";
                } else if (BF > 20 && BF <= 32) {
                    return "Healthy";
                } else if (BF > 32 && BF <= 38) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else if (age >= 40 && age <= 59) {
                if (BF <= 22) {
                    return "Underfat";
                } else if (BF > 22 && BF <= 33) {
                    return "Healthy";
                } else if (BF > 33 && BF <= 39) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else {
                if (BF <= 23) {
                    return "Underfat";
                } else if (BF > 23 && BF <= 35) {
                    return "Healthy";
                } else if (BF > 35 && BF <= 41) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            }

        } else {
            //female.
            if (age == 18) {
                if (BF <= 9) {
                    return "Underfat";
                } else if (BF > 9 && BF <= 19) {
                    return "Healthy";
                } else if (BF > 19 && BF <= 23) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else if (age == 19) {
                if (BF <= 8) {
                    return "Underfat";
                } else if (BF > 8 && BF <= 19) {
                    return "Healthy";
                } else if (BF > 19 && BF <= 23) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else if (age >= 20 && age <= 39) {
                if (BF <= 7) {
                    return "Underfat";
                } else if (BF > 7 && BF <= 19) {
                    return "Healthy";
                } else if (BF > 19 && BF <= 24) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else if (age >= 40 && age <= 59) {
                if (BF <= 10) {
                    return "Underfat";
                } else if (BF > 10 && BF <= 21) {
                    return "Healthy";
                } else if (BF > 21 && BF <= 27) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            } else {
                if (BF <= 12) {
                    return "Underfat";
                } else if (BF > 12 && BF <= 24) {
                    return "Healthy";
                } else if (BF > 24 && BF <= 29) {
                    return "Overfat";
                } else {
                    return "Obese";
                }
            }
        }
    }

    public static String totalAnalysis(String analysisOfBMI, String analysisOfBF, String analysisOfSM) {
        if (analysisOfSM.equals("")) {
            switch (analysisOfBF) {
                case "Underfat":
                    return "StrengthenBody";
                case "Healthy":
                    return "KeepBalance";
                case "Overfat":
                    if (analysisOfBMI.equalsIgnoreCase("Normalweight") || analysisOfBMI.equalsIgnoreCase("Underweight")) {
                        return "BuildMuscle";
                    } else {
                        return "LoseWeight";
                    }
                case "Obese":
                    return "LoseWeight";
            }
        } else {
            switch (analysisOfSM) {
                case "Low":
                    return "BuildMuscle";
                case "Normal":
                    switch (analysisOfBF) {
                        case "Underfat":
                            return "StrengthenBody";
                        case "Healthy":
                            return "KeepBalance";
                        case "Overfat":
                            if (analysisOfBMI.equalsIgnoreCase("Normalweight") || analysisOfBMI.equalsIgnoreCase("Underweight")) {
                                return "BuildMuscle";
                            } else {
                                return "LoseWeight";
                            }
                        case "Obese":
                            return "LoseWeight";
                    }
                default:
                    return "StrengthenBody";
            }
        }
        return "";
    }

    public static ArrayList<Food> getRecommendBreakfast(String bodyAim, ArrayList<Food> foods) {
        recommendBreakfast(bodyAim,foods);
        ArrayList<Food> tempList = recommendBreakfast;
        recommendBreakfast = new ArrayList<>();
        return tempList;
    }

    public static ArrayList<Food> getRecommendLunch(String bodyAim, ArrayList<Food> foods) {
        recommendLunch(bodyAim,foods);
        ArrayList<Food> tempList = recommendLunch;
        recommendLunch = new ArrayList<>();
        return tempList;
    }

    public static ArrayList<Food> getRecommendDinner(String bodyAim,ArrayList<Food> foods) {
        recommendDinner(bodyAim,foods);
        ArrayList<Food> tempList = recommendDinner;
        recommendDinner = new ArrayList<>();
        return tempList;
    }







}
