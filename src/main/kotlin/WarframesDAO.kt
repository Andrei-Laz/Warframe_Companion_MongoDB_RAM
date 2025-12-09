import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import org.bson.Document
import java.util.Scanner
import utility.iteradorNumerosValidosInteger
import utility.iteradorNumerosValidosDouble

data class Warframe(
    val warframeID: Int? = null, //this is so because the id is automatically created by the database
    val name: String,
    val health: Int,
    val armor: Int,
    val energy: Int,
    val sprintSpeed: Double,
    val passive: String
)

object WarframesDAO {

    fun mostrarWarframes() {
        val cursor = coleccionWarframes.find().iterator()
        cursor.use {
            while (it.hasNext()) {
                val doc = it.next()

                val warframeId = doc.getInteger("warframe_id")
                val warframeName = doc.getString("name")
                val warframeHealth = doc.getInteger("health")
                val warframeArmor = doc.getInteger("armor")
                val warframeEnergy = doc.getInteger("energy")
                val warframeSprintSpeed = doc.getDouble("sprint_speed")
                val warframePassive = doc.getString("passive")

                println("Warframe [${warframeId}]" +
                        "\n\tName: $warframeName" +
                        "\n\tHealth: $warframeHealth" +
                        "\n\tArmor: $warframeArmor" +
                        "\n\tEnergy: $warframeEnergy" +
                        "\n\tSprint Speed: $warframeSprintSpeed" +
                        "\n\tPassive: $warframePassive")
            }
        }
    }

    fun insertarWarframe() {
        val scanner = Scanner(System. `in`)

        println("ID del Warframe: ")
        val id_warframe = iteradorNumerosValidosInteger("El ID")
        println("Nombre del Warframe: ")
        val nombre = scanner.nextLine()
        println("Vida del Warframe: ")
        val vida = iteradorNumerosValidosInteger("La vida")
        println("Armadura del Warframe: ")
        val armadura = iteradorNumerosValidosInteger("La armadura")
        println("Energía del Warframe: ")
        val energia = iteradorNumerosValidosInteger("La energía")
        println("Velocidad Sprint: ")
        val velocidadSprint = iteradorNumerosValidosDouble("la velocidad de sprint")
        println("Pasiva: ")
        val pasiva = scanner.nextLine()

        val doc = Document("warframe_id", id_warframe)
            .append("name", nombre)
            .append("health", vida)
            .append("armor", armadura)
            .append("energy", energia)
            .append("sprint_speed", velocidadSprint)
            .append("passive", pasiva)

        coleccionWarframes.insertOne(doc)
        println("Warframe insertado con ID: ${doc.getObjectId("_id")}")
    }

    fun actualizarVidaWarframe() {
        var id_warframe = iteradorNumerosValidosInteger("El ID")
        var warframe = coleccionWarframes.find(Filters.eq("warframe_id", id_warframe)).firstOrNull()

        if (warframe == null) {
            println("No se encontró ningun warframe con ID = \"$id_warframe\".")
        }
        else {
            println("Warframe encontrado: ${warframe.getString("name")} (vida: ${warframe.get("health")})")

            println("La nueva vida sera: ")
            var vida = iteradorNumerosValidosInteger("La vida")

            // Actualizar el documento
            val result = coleccionWarframes.updateOne(
                Filters.eq("warframe_id", id_warframe),
                Document("\$set", Document("health", vida))
            )

            if (result.modifiedCount > 0)
                println("Vida actualizada correctamente (${result.modifiedCount} documento modificado).")
            else
                println("No se modificó ningún documento (la vida quizá ya era la misma).")
        }
    }

    fun eliminarWarframe() {

        println("ID del warframe a eliminar: ")
        var id_warframe = iteradorNumerosValidosInteger("El ID")

        val result = coleccionWarframes.deleteOne(Filters.eq("warframe_id", id_warframe))
        if (result.deletedCount > 0)
            println("Warframe eliminado correctamente.")
        else
            println("No se encontró ningun warframa con ese ID.")
    }

    fun consultarWarframesConFiltros() {
        println("***** Warframes con más de 300 de vida")
        coleccionWarframes.find(Filters.gt("health", 300)).forEach { println(it.toJson()) }

        println("\n***** Warframes con armadura menor a 200")
        coleccionWarframes.find(Filters.lt("armor", 200)).forEach { println(it.toJson()) }

        println("\n***** Warframe con ID 5")
        coleccionWarframes.find(Filters.eq("warframe_id", 5)).forEach { println(it.toJson()) }

        println("\n***** Warframes con velocidad de sprint >= 1.2")
        coleccionWarframes.find(Filters.gte("sprint_speed", 1.2)).forEach { println(it.toJson()) }
    }

    fun proyeccionesWarframes() {

        println("***** Solo nombres de los Warframes")
        coleccionWarframes.find()
            .projection(Projections.include("name"))
            .forEach { println(it.toJson()) }

        println("\n***** Nombres y vida de los Warframes")
        coleccionWarframes.find()
            .projection(Projections.include("name", "health"))
            .forEach { println(it.toJson()) }
    }

    fun agregacionesWarframes() {
        // 1) Promedio de health
        println("***** Vida media de los Warframes")
        val avgPipeline = listOf(
            Document("\$group", Document("_id", null)
                .append("vidaMedia", Document("\$avg", "\$health")))
        )
        coleccionWarframes.aggregate(avgPipeline).forEach { println(it.toJson()) }

        // 2) Armadura Máxima
        println("\n***** Armadura máxima")
        val maxPipeline = listOf(
            Document("\$group", Document("_id", null)
                .append("maxArmor", Document("\$max", "\$armor")))
        )
        coleccionWarframes.aggregate(maxPipeline).forEach { println(it.toJson()) }

        // 3) Número total de Warframes
        println("\n***** Cantidad total de Warframes")
        val countPipeline = listOf(
            Document("\$group", Document("_id", null)
                .append("totalWarframes", Document("\$sum", 1)))
        )
        coleccionWarframes.aggregate(countPipeline).forEach { println(it.toJson()) }
    }
}