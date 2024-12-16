package com.example.proj;

import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Stack<Double> values = new Stack<>(); //стек для числовых значений
    private final Stack<String> operators = new Stack<>(); //стек для операторов

    private static final Pattern one_operator = Pattern.compile("[/*\\-+^]"); //поиск одного из операторов
    private static final Pattern double_operator = Pattern.compile("[/*\\-+^]{2}"); //поиск двух подряд идущих символа
    private static final Pattern numbers = Pattern.compile("\\d+(\\.\\d+)?"); //поиск целые и дробные числа

    //метод для вычисления выражения
    public String calculate(String str) {
        String extra_space = sanitizeExpression(str); //убираем лишние пробелы и исправляем ошибки формата
        if (isValidExpression(extra_space)) {
            RPN(extra_space);
            return " " + evaluate() + " "; //выполняем вычисления на основе выражения в RPN
        }
        return str;
    }

    //чтобы не отслеживать приоритет операций
    private void RPN(String str) {
        StringTokenizer tokens = new StringTokenizer(str, "/*-+^()", true); //разбиваем строку на токены
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (isNumber(token)) {
                values.push(Double.parseDouble(token));
            }
            else {
            }

        }
    }
}
