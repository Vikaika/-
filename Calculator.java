package com.example.proj;

import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
  private final Stack<Double> values = new Stack<>(); //стек для числовых значений
  private final Stack<String> operators = new Stack<>(); //стек для операторов
}
