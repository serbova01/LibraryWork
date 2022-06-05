package com.example.librarywork;

/**
 * Класс Проверки строк на корректные данные.
 * <p>
 * Данный класс позволяет проверить введенные данные на корректность.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class CheckInfo {
    /**
     * Функция проверки строки на отсутствие других символов кроме символов алфавита.
     * @param str строка для проверки
     * @return результат проверки
     */
    public boolean checkInfoIsLetter(String str){
        if(str.length()>1){
            char[] chars = str.toCharArray();
            for (int i =0; i<chars.length; i++){
                if (!Character.isLetter(chars[i])){
                    return false;
                }
            }
            return true;
        }else return false;
    }
    /**
     * Функция проверки строки на отсутствие других символов кроме символов алфавита и/или чисел.
     * @param str строка для проверки
     * @return результат проверки
     */
    public boolean checkInfoIsLetterOrDigit(String str){
        if(str.length()>0){
            char[] chars = str.toCharArray();
            for (int i =0; i<chars.length; i++){
                if (!Character.isLetterOrDigit(chars[i])){
                    return false;
                }
            }
            return true;
        }else return false;
    }
    /**
     * Функция проверки строки на отсутствие других символов кроме символов алфавита.
     * @param str строка для проверки
     * @return результат проверки
     */
    public boolean checkInfoIsNumeric(String str){
        if (!str.equals("") && !str.equals(" ")){
            char[] chars = str.toCharArray();
            for (int i =0; i<chars.length; i++){
                if (!Character.isDigit(chars[i])){
                    return false;
                }
            }
            return true;
        }else return false;
    }
    /**
     * Функция проверки строки на отсутствие других символов кроме других символов кроме символов алфавита и пробела.
     * @param str строка для проверки
     * @return результат проверки
     */
    public boolean checkInfoIsText(String str){
        if (str.length()>1){
            String[] chars = str.split("");
            for (int i =0; i<chars.length; i++){
                if (!Character.isLetterOrDigit(chars[i].charAt(0)) && !chars[i].equals(" ")){
                    return false;
                }
            }
            return true;
        }else return false;

    }
    /**
     * Функция проверки корректности введенного номера телефона.
     * @param str строка для проверки
     * @return результат проверки
     */
    public boolean checkInfoPhoneNumber(String str){
        if (str.length()>1){
            String[] chars = str.split("");
            for (int i =0; i<chars.length; i++){
                if (Character.isLetter(chars[i].charAt(0))){
                    return false;
                }
            }
            int startIndex=0;
            if (str.length()<17) return false;
            else{
                if (str.length() == 18 && chars[startIndex].equals("+") && chars[startIndex+1].equals("7") ) {
                    startIndex = 1;
                } else{
                    if (str.length() == 17 && chars[startIndex].equals("8")) startIndex = 0;
                    else return false;
                }
                if (!chars[startIndex+1].equals(" ") | !chars[startIndex+2].equals("(") | !chars[startIndex+6].equals(")") |
                        !chars[startIndex+7].equals("-") | !chars[startIndex+11].equals("-") |
                        !chars[startIndex+14].equals("-")){
                    return false;
                }
            }
            return true;
        }else return false;
    }
    /**
     * Функция проверки строки на вещественное число.
     * @param str строка для проверки
     * @return результат проверки
     */
    public boolean checkInfoIsFloat(String str){
        if (str.length()>1){
            boolean flag = true;
            String[] chars = str.split("");
            for (int i =0; i<chars.length; i++){
                System.out.println(chars[i]);
                if (Character.isDigit(chars[i].charAt(0)) | chars[i].equals(".")){
                    flag = true;
                } else{
                    flag = false;
                    break;
                }
            }
            return flag;
        }else return false;

    }
    /**
     * Функция проверки строки на корректность введенного номера ISBN (он может содержать только числа и знак "-").
     * @param str строка для проверки
     * @return результат проверки
     */
    public boolean checkInfoISBN(String str){
        if (str.length()>1){
            boolean flag = true;
            String[] chars = str.split("");
            for (int i =0; i<chars.length; i++){
                System.out.println(chars[i]);
                if (Character.isDigit(chars[i].charAt(0)) | chars[i].equals("-")){
                    flag = true;
                } else{
                    flag = false;
                    break;
                }
            }
            return flag;
        }else return false;

    }
}
