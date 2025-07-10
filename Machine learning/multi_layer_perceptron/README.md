MLP Stock Prediction Classifier
 This project implements a Multi-Layer Perceptron (MLP) to classify whether a stock (e.g., Bitcoin)
 should be bought based on historical financial data. It uses a Python backend for ML training and a
 Java frontend for user input and subprocess control.
 Objective
 To build a classification model that predicts Buy (1) or Don't Buy (0) decisions using past stock data.
 Evaluation is based on:- Accuracy- F1 Score- Precision- Recall- Confusion Matrix
 
 Project Structure
 MLP_COS314/
 btc_train.csv
 btc_test.csv
 MLPCaller.java
 MLPCaller.jar
 mlp.py
 mlp_results.csv


 README.md
 How It Works- Java class MLPCaller.java:- Takes user input (random seed, paths to CSV files)
- Calls mlp.py via subprocess- Displays evaluation metrics- Python script mlp.py:- Reads the training and test data- Trains a Multi-Layer Perceptron model- Evaluates and prints metrics- Writes results to mlp_results.csv

 Requirements
 Python (3.8 - 3.11 recommended)
 Install required libraries:
 pip install numpy pandas scikit-learn
 Java (JDK 8 to 17 recommended)

 How to Run using the prebuilt .jar:
 1. compile
 javac MLPCaller.java
 2. run
 jar cfe MLPCaller.jar MLPCaller MLPCaller.class

 How to Run without prebuilt .jar
 Step 1: Compile Java File
 javac MLPCaller.java
 Step 2: Run the Program
 java MLPCaller
 You will be prompted to enter:- A random seed (e.g., 123456)- Full path to btc_train.csv- Full path to btc_test.csv
Data Format (CSV)
 Each row in btc_train.csv or btc_test.csv:
 | Open | High | Low | Close | Adj Close | Output |
 |------|------|-----|-------|-----------|--------|
 | 0.974 | 0.926 | ... | ... | ... | 1 or 0 |
 
 Output File
 mlp_results.csv: Stores results for different seeds:
 Seed,Accuracy,F1 Score
 1234,0.9011,0.9002
 12345,0.9809,0.9808
