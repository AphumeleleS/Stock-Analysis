import pandas as pd
import numpy as np
import sys
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import accuracy_score, f1_score, confusion_matrix, precision_score, recall_score
from sklearn.preprocessing import StandardScaler
import os
import warnings
from sklearn.exceptions import ConvergenceWarning
warnings.filterwarnings("ignore", category=ConvergenceWarning)

random_seed = int(sys.argv[1])
random_seed = random_seed % (2**32)

train_path = sys.argv[2]
test_path = sys.argv[3]

np.random.seed(random_seed)

train_df = pd.read_csv(train_path)
test_df = pd.read_csv(test_path)

print("Missing values in train data:")
print(train_df.isnull().sum())
print("Missing values in test data:")
print(test_df.isnull().sum())

X_train = train_df.drop("Output", axis=1)
y_train = train_df["Output"]
X_test = test_df.drop("Output", axis=1)
y_test = test_df["Output"]

scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

mlp = MLPClassifier(hidden_layer_sizes=(100,), max_iter=1000, random_state=random_seed)
mlp.fit(X_train_scaled, y_train)

y_pred = mlp.predict(X_test_scaled)

accuracy = accuracy_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)
precision = precision_score(y_test, y_pred, zero_division=0)
recall = recall_score(y_test, y_pred, zero_division=0)
cm = confusion_matrix(y_test, y_pred)
tp = cm[1, 1]
fp = cm[0, 1]
fn = cm[1, 0]
tn = cm[0, 0]

print("\n--- RESULTS ---")
print(f"Seed: {random_seed}")
print(f"Accuracy: {accuracy}")
print(f"F1 Score: {f1}")
print(f"Precision: {precision}")
print(f"Recall: {recall}")
print("\nConfusion Matrix:")
print(f"TP (Predicted Buy, Actual Buy): {tp}")
print(f"FP (Predicted Buy, Actual Don't Buy): {fp}")
print(f"FN (Predicted Don't Buy, Actual Buy): {fn}")
print(f"TN (Predicted Don't Buy, Actual Don't Buy): {tn}")

results_file = "mlp_results.csv"
file_exists = os.path.isfile(results_file)

with open(results_file, "a") as f:
    if not file_exists:
        f.write("Seed,Accuracy,F1 Score\n")
    f.write(f"{random_seed},{accuracy},{f1}\n")
