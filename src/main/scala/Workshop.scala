import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Workshop {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("SparkWorkshop-07-UI-Conclusion")
      .master("local[*]")
      .config("spark.ui.enabled", "true")
      .config("spark.ui.port", "4040")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._

    val orders = Seq(
      (1,  "c001", "Laptop",   1200.0, "delivered"),
      (2,  "c002", "Phone",     800.0, "pending"),
      (3,  "c001", "Tablet",    450.0, "delivered"),
      (4,  "c003", "Laptop",   1200.0, "cancelled"),
      (5,  "c002", "Monitor",   350.0, "delivered"),
      (6,  "c004", "Phone",     800.0, "delivered"),
      (7,  "c001", "Keyboard",  100.0, "delivered"),
      (8,  "c003", "Tablet",    450.0, "pending"),
      (9,  "c004", "Laptop",   1200.0, "delivered"),
      (10, "c002", "Monitor",   350.0, "cancelled")
    ).toDF("orderId", "clientId", "product", "amount", "status")

    val result = orders
      .filter($"status" === "delivered")
      .select($"clientId", $"amount")
      .groupBy($"clientId")
      .agg(
        sum($"amount").alias("total_ca"),
        count("*").alias("nb_orders")
      )
      .orderBy($"total_ca".desc)

    val resultCached = result.cache()

    println("\n" + "=" * 55)
    println("  SECTION 07 — Spark UI & Conclusion")
    println("=" * 55)

    println("\n  Job 1 — show()")
    val t1 = System.currentTimeMillis()
    resultCached.show()
    val t2 = System.currentTimeMillis()
    println(s"  Duree : ${t2 - t1} ms (calcule + mis en cache)")

    println("\n  Job 2 — count()")
    val t3 = System.currentTimeMillis()
    val nb = resultCached.count()
    val t4 = System.currentTimeMillis()
    println(s"  Duree : ${t4 - t3} ms (lecture cache)")
    println(s"  Resultat : $nb clients")

    println("\n  Job 3 — first()")
    val top = resultCached.first()
    println(s"  Meilleur client : ${top.getString(0)}")
    println(s"  CA : ${top.getDouble(1)} EUR")

    println("\n" + "=" * 55)
    println("  BILAN DES PERFORMANCES")
    println("=" * 55)
    println(s"  show()  : ${t2 - t1} ms  → calcul + cache")
    println(s"  count() : ${t4 - t3} ms  → lecture cache")

    println("\n" + "=" * 55)
    println("  SPARK UI DISPONIBLE")
    println("  → http://localhost:4040")
    println()
    println("  Onglet Jobs    : 3 jobs executes")
    println("  Onglet Stages  : stages et durees")
    println("  Onglet Storage : cache en memoire")
    println("  Onglet SQL     : DAG en visuel")
    println()
    println("  La UI reste ouverte 5 minutes.")
    println("  Ouvre http://localhost:4040 maintenant.")
    println("=" * 55)

    // ─────────────────────────────────────────
    // PAUSE 5 minutes = 300 secondes
    // Compte a rebours visible dans le terminal
    // ─────────────────────────────────────────
    val dureeSecondes = 300
    for (i <- dureeSecondes to 1 by -1) {
      val minutes = i / 60
      val secondes = i % 60
      print(s"\r  Fermeture dans : ${minutes}m ${secondes}s   ")
      Thread.sleep(1000)
    }

    println("\n\n" + "=" * 55)
    println("  ENSEIGNEMENTS CLES")
    println("=" * 55)
    println("""
  1. Le DAG est le plan — pas l execution
  2. Lazy evaluation = optimisation Catalyst
  3. Le Shuffle = barriere entre stages
  4. Reverse DAG = lire les dependances
  5. Spark UI = tout voir en temps reel
    """)
    println("  Workshop complet. Merci !")

    resultCached.unpersist()
    spark.stop()
  }
}
