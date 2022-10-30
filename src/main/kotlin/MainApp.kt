import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import models.ImageResponse
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.reflect.ParameterizedType
import kotlin.random.Random
import kotlin.random.asJavaRandom

val gson: Gson = GsonBuilder().setPrettyPrinting().create()

fun main() {
    val imageResponse = ImageResponse::class.java.generateFakeData()
    println(imageResponse)
}

inline fun <reified T> Class<T>.generateFakeData(): T {
    val json = generateDataFromClass(this)
    saveToFile(json)
    return gson.fromJson(json, T::class.java)
}

fun <T> generateDataFromClass(clazz: Class<T>): JsonObject {
    val json = JsonObject()
    val random = Random.asJavaRandom()
    clazz.declaredMethods.forEach { parameter ->
        if (parameter.name.startsWith("get").not()) return@forEach
        val key = parameter.name.removePrefix("get").replaceFirstChar { char -> char.lowercaseChar() }
        when {
            parameter.returnType.isAssignableFrom(String::class.java) -> {
                json.addProperty(key, randomString())
            }
            parameter.returnType.isAssignableFrom(Int::class.java) -> {
                json.addProperty(key, (1..1000).random())
            }
            parameter.returnType.isAssignableFrom(Short::class.java) -> {
                json.addProperty(key, (1..1000).random())
            }
            parameter.returnType.isAssignableFrom(Long::class.java) -> {
                json.addProperty(key, random.nextLong())
            }
            parameter.returnType.isAssignableFrom(Float::class.java) -> {
                json.addProperty(key, random.nextFloat())
            }
            parameter.returnType.isAssignableFrom(Double::class.java) -> {
                json.addProperty(key, random.nextDouble())
            }
            parameter.returnType.isAssignableFrom(Boolean::class.java) -> {
                json.addProperty(key, random.nextBoolean())
            }
            parameter.returnType.isAssignableFrom(Char::class.java) -> {
                json.addProperty(key, ('A'..'Z').random())
            }
            !parameter.returnType.typeName.contains("java.lang") && !parameter.returnType.isAssignableFrom(List::class.java) -> {
                val data = generateDataFromClass(parameter.returnType)
                json.add(key, data)
            }

            ((parameter.genericReturnType) as ParameterizedType).rawType == List::class.java -> {
                val className = ((parameter.genericReturnType) as ParameterizedType).actualTypeArguments[0].typeName
                val clazzList = mutableListOf<Any>()
                println("Class Name  $className")
                when (className) {
                    "java.lang.String" -> {
                        (0..5).forEach { _ ->
                            clazzList.add(randomString())
                        }
                    }
                    "java.lang.Integer" -> {
                        (0..5).forEach { _ ->
                            clazzList.add((1..1000).random())
                        }
                    }
                    "java.lang.Short" -> {
                        (0..5).forEach { _ ->
                            clazzList.add((1..1000).random())
                        }
                    }
                    "java.lang.Long" -> {
                        (0..5).forEach { _ ->
                            clazzList.add(random.nextLong())
                        }
                    }
                    "java.lang.Boolean" -> {
                        (0..5).forEach { _ ->
                            clazzList.add(random.nextBoolean())
                        }
                    }
                    "java.lang.Character" -> {
                        (0..5).forEach { _ ->
                            clazzList.add(('A'..'Z').random())
                        }
                    }
                    "java.lang.Double" -> {
                        (0..5).forEach { _ ->
                            clazzList.add(random.nextDouble())
                        }
                    }
                    "java.lang.Float" -> {
                        (0..5).forEach { _ ->
                            clazzList.add(random.nextFloat())
                        }
                    }
                    else -> {
                        val clazz2 = Class.forName(className)
                        (0..5).forEach { _ ->
                            val data = generateDataFromClass(clazz2)
                            clazzList.add(data)
                        }
                    }
                }
                val element = gson.toJsonTree(clazzList, object : TypeToken<List<*>>() {}.type)
                json.add(key, element)
            }
        }
    }
    return json
}

private fun randomString(): String = List(16) {
    (('a'..'z') + ('A'..'Z')).random()
}.joinToString("")

fun saveToFile(json: JsonObject) {
    val fileName = "generatedJsonFile.json"
    val newFile = File(fileName)

    val parser = JsonParser.parseString(json.toString())
    val prettyJsonString = gson.toJson(parser)

    if (newFile.createNewFile()) {
        newFile.createNewFile()
    }

    try {
        val myWriter = FileWriter(fileName)
        myWriter.write(prettyJsonString)
        myWriter.close()
        println("Successfully wrote to the file.")
    } catch (e: IOException) {
        println("An error occurred.")
        e.printStackTrace()
    }
}
