GO to project directory and put file path 

Compile using :
javac -cp "lib\weka.jar;lib\mtj.jar" -d bin src\DecisionTreeBTC.java

Run using:
java --add-opens java.base/java.lang=ALL-UNNAMED -cp "bin;lib\weka.jar;lib\mtj.jar" DecisionTreeBTC

Enter seed as it requests: e.g. 69

Enter filepath to training data:  The "data\BTC_train_nominal_FIXED.arff" file
Enter filepath to test data:  The "data\BTC_test_nominal_FIXED.arff" file
