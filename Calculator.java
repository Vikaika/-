package com.example.proj;

import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Calculator {
    private final Stack<Double> values = new Stack<>(); //стек для числовых значений
    private final Stack<String> operators = new Stack<>(); //стек для операторов


    public Stack<Double> getValues() {
        return values;
    }

    public Stack<String> getOperators() {
        return operators;
    }

    private static final Pattern one_operator = Pattern.compile("[/*\\-+^]"); //поиск одного из операторов
    private static final Pattern double_operator = Pattern.compile("[/*\\-+^]{2}"); //поиск двух подряд идущих символа
    private static final Pattern numbers = Pattern.compile("\\d+(\\.\\d+)?"); //поиск целые и дробные числа

    // метод для вычисления выражения
    public String Calculate(String str) {
        String extra_space = Clearing(str); // убираем лишние пробелы и исправляем ошибки формата
        if (!ValidExpression(extra_space)) {
            throw new IllegalArgumentException("Некорректное выражение");
        }
        RPN(extra_space);
        return Result(); // возвращаем результат без лишних пробелов
    }

    //чтобы не отслеживать приоритет операций
    private void RPN(String str) {
        StringTokenizer tokens = new StringTokenizer(str, "/*-+^()", true); //разбиваем строку на токены
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            //сли токен это число, то добавляем его в стек
            if (Number(token)) {
                values.push(Double.parseDouble(token));
            }
            else {
                ProcessOperator(token);
            }
        }
    }

    //метод для управления логикой вычисления выражений, учитывая приоритет и порядок выполнения операций
    private void ProcessOperator(String operator) {
        while (!operators.isEmpty() && OperationPriority(operator) <= OperationPriority(operators.peek())) {
            if (operator.equals(")")) {
                operators.pop();
                return;
            }

            if (values.size() < 2) {
                throw new IllegalArgumentException("Недостаточно значений в выражении");
            }

            double second_val = values.pop();
            double first_val = values.pop();
            values.push(PerformOperation(first_val, second_val, operators.pop()));
        }

        if (!operator.equals(")")) {
            operators.push(operator);
        }
    }

    //метод по очистке и подготовке выражения для вычисления
    private String Clearing(String expression) {
        expression = expression.replaceAll(" ", ""); // удаляем все пробелы
        if (expression.charAt(0) == '-') expression = "0" + expression; // если первый символ "-"

        // Обработка отрицательных чисел
        return expression.replaceAll("\\(-", "(0-")
                .replaceAll("(\\d)\\(", "$1*(")
                .replaceAll("\\)\\(", ")*(")
                .replaceAll("\\)(\\d)", ")*$1");
    }

    //метод для определения является ли выражение корректным
    private boolean ValidExpression(String expression) {
        expression = expression.replaceAll(" ", ""); // Удаляем пробелы
        if (expression.isEmpty()) return false; // Пустое выражение

        // Проверка на недопустимые символы
        if (!expression.matches("[\\d+\\-*/^().]*")) return false;

        // Проверка на сбалансированные скобки и количество операторов
        return PlacingBrackets(expression) &&
                !double_operator.matcher(expression).find() && // отсутствие двойных операторов
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

            // Возвращаем true, если чисел больше, чем операторов
            return digitCount > operatorCount;
    }

    //метод для приоритета операций
    private int OperationPriority(String operation) {
        return switch (operation) {
            case "(", ")" -> 1;
            case "+", "-" -> 2;
            case "/", "*" -> 3;
            case "^" -> 4;
            default -> -1;
        };
    }

    //метод для выполнения операций
    private double PerformOperation(double a, double b, String operation) {
        return switch (operation) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            //возвращаем результат деления только после проверки деления на 0
            case "/" -> {
                if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
                yield a / b;
            }
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }

    private String Result() {
        //операции выполняются пока есть операторы для обработки
        while (!operators.isEmpty()) {
            double second = values.pop();
            double first = values.pop();
            values.push(PerformOperation(first, second, operators.pop()));
        }
        return String.format("%.2f", values.pop()); //строка с двумя знаками после запятой
    }

    //является ли эелемент числом
    private boolean Number(String token) {
        return token.matches("\\d+(\\.\\d+)?");
    }
}
