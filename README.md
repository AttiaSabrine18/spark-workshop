# Apache Spark Workshop: DAG, Execution Plans & Spark UI

This repository contains a hands-on workshop on **Apache Spark 3.5.1** with **Scala 2.12.15**, running inside a reproducible **Docker** environment.

The goal is to **understand how Spark actually executes your code** — beyond the syntax — by exploring the DAG, the Catalyst optimizer, the physical plan, and distributed execution.

---

## 🎯 Learning Objectives

- **Lazy Evaluation**: see how transformations build a plan without processing any data until an action is triggered.
- **Analyze the DAG**: visualize how transformations (`filter`, `select`, `groupBy`, `agg`, `orderBy`) are chained together.
- **Explore the Spark UI**: interpret the Jobs, Stages, Storage, and SQL tabs to understand execution boundaries.
- **Understand Shuffles**: observe how data is redistributed across the network during aggregations and sorts.
- **Optimize with Caching**: demonstrate the performance impact of `cache()` and `unpersist()`.

---

## 🛠️ Technologies Used

| Technology     | Version   | Role                                  |
|----------------|-----------|---------------------------------------|
| Apache Spark   | 3.5.1     | Data processing engine                |
| Scala          | 2.12.15   | Programming language                  |
| Java           | 17        | Runtime platform (JVM)                |
| SBT            | 1.6.2     | Build tool                            |
| Docker         | —         | Isolated and reproducible environment |

---

## 📂 Project Structure

```text
spark-workshop/
├── build.sbt                    # Spark dependencies and build settings
├── project/
│   └── build.properties         # SBT version
└── src/
    └── main/
        └── scala/
            └── Workshop.scala   # Main entry point and Spark transformations
```

---

## 🚀 How to Run

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed on your machine.

### Step 1 — Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/spark-workshop.git
cd spark-workshop
```

### Step 2 — Start the Docker container

This command mounts your current directory into the container and exposes the Spark UI port (4040).

```bash
docker run -it --rm -p 4040:4040 -v "$(pwd):/workshop" -w /workshop \
  hseeberger/scala-sbt:17.0.2_1.6.2_2.12.15 bash
```

> ⏳ The first time, Docker will download the image (a few minutes). Subsequent runs are instant.

### Step 3 — Compile and run

Inside the container terminal:

```bash
sbt run
```

### Step 4 — Explore the Spark UI

The application **pauses for 5 minutes** at the end to give you time to explore. Open your browser at:

```
http://localhost:4040
```

Navigate through the **Jobs**, **Stages**, **Storage**, and **SQL** tabs to see the DAG execution in real time.

---

## 📊 Dataset & Scenario

The workshop simulates an e-commerce order dataset. The objective is to compute the total revenue (`total_ca`) and the number of orders (`nb_orders`) for customers with `"delivered"` orders, sorted by highest revenue.

### Expected Output

```
+--------+--------+---------+
|clientId|total_ca|nb_orders|
+--------+--------+---------+
|    c004|  2000.0|        2|
|    c001|  1750.0|        3|
|    c002|   350.0|        1|
+--------+--------+---------+
```

---

## 💡 Key Concepts

| Concept              | In one sentence                                                             |
|----------------------|-----------------------------------------------------------------------------|
| **Lazy Evaluation**  | Spark waits for an action before executing anything                         |
| **DAG**              | The visual plan of all computation steps                                    |
| **Catalyst**         | The internal optimizer that automatically improves your plan                |
| **Shuffle**          | Redistribution of data across machines (expensive operation)                |
| **Cache**            | Keeping a dataset in memory to reuse it without recomputation               |
| **Stage**            | A group of operations with no shuffle between them                          |
| **Job**              | All the stages triggered by a single action                                 |

---

## 🧭 Going Further

- 📖 [Apache Spark Official Documentation](https://spark.apache.org/docs/latest/)
- 📗 *Spark: The Definitive Guide* — Chambers & Zaharia
- 🔎 Adaptive Query Execution (AQE) — new in Spark 3.x
- ☁️ Spark connectors: Delta Lake, S3, Kafka…
