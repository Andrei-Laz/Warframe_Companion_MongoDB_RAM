package prueba

import org.bson.Document

import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.json.JsonWriterSettings
import org.json.JSONArray
import java.io.File

//variables globales definidas sin inicializar
lateinit var servidor: MongoServer
lateinit var cliente: MongoClient
lateinit var uri: String
lateinit var coleccionPlantas: MongoCollection<Document>
lateinit var coleccionFacturas: MongoCollection<Document>

//BD y colección con la que se trabajará
const val NOM_BD = "florabotanica"
const val NOM_COLECCION1 = "plantas"
const val NOM_COLECCION2 = "facturas"

// Función para conectar a la BD
private fun conectarBDPrueba() {
    servidor = MongoServer(MemoryBackend())
    val address = servidor.bind()
    uri = "mongodb://${address.hostName}:${address.port}"

    cliente = MongoClients.create(uri)
    coleccionPlantas = cliente.getDatabase(NOM_BD).getCollection(NOM_COLECCION1)
    coleccionFacturas = cliente.getDatabase(NOM_BD).getCollection(NOM_COLECCION2)

    println("Servidor MongoDB en memoria iniciado en $uri")
}

// Función para desconectar a la BD
private fun desconectarBDPrueba() {
    cliente.close()
    servidor.shutdown()
    println("Servidor MongoDB en memoria finalizado")
}

fun main() {
    conectarBDPrueba()
    importarBD("src/main/resources/florabotanica_plantas.json", coleccionPlantas)
    importarBD("src/main/resources/facturas.json", coleccionFacturas)

    menu()

    val pipeline = listOf(
        Document("\$lookup", Document()
            .append("from", "plantas")
            .append("localField", "id_planta")
            .append("foreignField", "id_planta")
            .append("as", "planta")
        ),
        Document("\$unwind", "\$planta")
    )

    coleccionFacturas.aggregate(pipeline).forEach { doc ->
        val idFactura = doc.getInteger("id_factura")
        val fecha = doc.getString("fecha")
        val idPlanta = doc.getInteger("id_planta")
        val cantidad = doc.getInteger("cantidad")
        val precio = doc.getInteger("precio")

        val planta = doc["planta"] as Document
        val nombreComun = planta.getString("nombre_comun")

        println("[$idFactura] ($fecha): $nombreComun (id $idPlanta) – $cantidad uds. $precio €")
    }

    exportarBD(coleccionPlantas,"src/main/resources/florabotanica_plantas.json")
    exportarBD(coleccionFacturas,"src/main/resources/facturas.json")
    desconectarBDPrueba()
}

// ****************
// **** MENÚ   ****
// ****************

fun menu(){
    //llamada a listar todas las plantas de la BD
    mostrarPlantas()
}

fun mostrarPlantas() {
    println();
    println("**** Listado de plantas:")
    coleccionPlantas.find().forEach { doc ->
        val id = doc.getInteger("id_planta")
        val nombre_comun = doc.getString("nombre_comun")
        val nombre_cientifico = doc.getString("nombre_cientifico")
        val altura = doc.getInteger("altura")
        println("[$id] $nombre_comun ($nombre_cientifico): ${altura} cm")
    }
}

fun exportarBD(coleccion: MongoCollection<Document>, rutaJSON: String) {
    val settings = JsonWriterSettings.builder().indent(true).build()
    val file = File(rutaJSON)
    file.printWriter().use { out ->
        out.println("[")
        val cursor = coleccion.find().iterator()
        var first = true
        while (cursor.hasNext()) {
            if (!first) out.println(",")
            val doc = cursor.next()
            out.print(doc.toJson(settings))
            first = false
        }
        out.println("]")
        cursor.close()
    }

    println("Exportación de ${coleccion.namespace.collectionName} completada")
}

fun importarBD(rutaJSON: String, coleccion: MongoCollection<Document>) {
    println("Iniciando importación de datos desde JSON...")

    val jsonFile = File(rutaJSON)
    if (!jsonFile.exists()) {
        println("No se encontró el archivo JSON a importar")
        return
    }

    val jsonText = try {
        jsonFile.readText()
    } catch (e: Exception) {
        println("Error leyendo el archivo JSON: ${e.message}")
        return
    }

    val array = try {
        JSONArray(jsonText)
    } catch (e: Exception) {
        println("Error al parsear JSON: ${e.message}")
        return
    }

    val documentos = mutableListOf<Document>()
    for (i in 0 until array.length()) {
        val doc = Document.parse(array.getJSONObject(i).toString())
        doc.remove("_id")  // <-- eliminar _id para que MongoDB genere uno nuevo
        documentos.add(doc)
    }

    if (documentos.isEmpty()) {
        println("El archivo JSON está vacío")
        return
    }

    val db = cliente.getDatabase(NOM_BD)

    val nombreColeccion =coleccion.namespace.collectionName

    // Borrar colección si existe
    if (db.listCollectionNames().contains(nombreColeccion)) {
        db.getCollection(nombreColeccion).drop()
        println("Colección '$nombreColeccion' eliminada antes de importar.")
    }

    // Insertar documentos
    try {
        coleccion.insertMany(documentos)
        println("Importación completada: ${documentos.size} documentos de $nombreColeccion.")
    } catch (e: Exception) {
        println("Error importando documentos: ${e.message}")
    }
}