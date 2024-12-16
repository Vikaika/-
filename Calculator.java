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
    public String Calculate(String str) {
        String extra_space = Clearing(str); //убираем лишние пробелы и исправляем ошибки формата
        if (ValidExpression(extra_space)) {
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
            //сли токен это число, то добавляем его в стек
            if (isNumber(token)) {
                values.push(Double.parseDouble(token));
            }
            else {
                ProcessOperator(token);
            }
        }
    }

    //метод для управления логикой вычисления выражений, учитывая приоритет и порядок выполнения операций
    private void ProcessOperator(String operator) {
        //приоритет текущего оператора должен быть меньше или равен приоритету верхнего оператора в стеке
        while (!operators.isEmpty() && operationPriority(operator) <= operationPriority(operators.peek())) {
            //игнорируем ")"
            if (operator.equals(")")) {
                operators.pop();
                return;
            }
            double second_val = values.pop();
            double first_val = values.pop();
            values.push(performOperation(first_val, second_val, operators.pop()));
        }
        //если оператор это не ")", добавляем его в стек
        if (!operator.equals(")")) {
            operators.push(operator);
        }
    }

    //метод по очистке и подготовке выражения для вычисления
    private String Clearing(String expression) {
        expression = expression.replaceAll(" ", ""); //удаляем все пробелы
        if (expression.charAt(0) == '-') expression = "0" + expression; //если первый символ "-"
        return expression.replaceAll("\\(-", "(0-")
                .replaceAll("(\\d)\\(", "$1*(")
                .replaceAll("\\)\\(", ")*(")
                .replaceAll("\\)(\\d)", ")*$1");
    }

    //метод для определения является ли выражение корректным
    private boolean ValidExpression(String expression) {
        return PlacingBrackets(expression) &&
                !double_operator.matcher(expression).find() && //отсутствие двойных операторов
                CountOperator(expression);
    }

    //метод для проверки сбалансированности скобок
    private boolean PlacingBrackets(String expression) {
        int balance = 0;
        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                balance++;
            }
            if (ch == ')') {
                balance--;
            }
            if (balance < 0) {
                return false;
            }
        }
        return balance == 0;
    }

    //метод для проверки корректного кол-ва операторов
    private boolean CountOperator(String expression) {
        expression = expression.replaceAll(" ", ""); //удаление пробелов
        String[] parts = expression.split("[+\\-*/^]");
        int digitCount = parts.length; //кол-во чисел
        int operatorCount = expression.length() - expression.replaceAll("[+\\-*/^]", "").length(); //кол-во операторов

        return digitCount > operatorCount; //true, если чисел больше, чем операторов
    }
    

}
