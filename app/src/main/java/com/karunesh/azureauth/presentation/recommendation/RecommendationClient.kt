package com.karunesh.azureauth.presentation.recommendation

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karunesh.azureauth.presentation.recommendation.diet.DietItem
import com.karunesh.azureauth.presentation.recommendation.util.Config
import com.karunesh.azureauth.presentation.recommendation.util.FileUtils
import com.karunesh.azureauth.presentation.recommendation.util.getJsonDataFromAsset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.HashMap

class RecommendationClient(
    private val context: Context,
    private val config: Config
) {

    private val candidates: MutableMap<Int, DietItem> = HashMap()
    private val dietTypes: MutableMap<String, Int> = HashMap()
    private var tflite: Interpreter? = null

    data class Result(
        val id: Int,
        val item: DietItem,
        val confidence: Float
    ) {
        override fun toString(): String {
            return String.format("[%d] confidence: %.3f, item: %s", id, confidence, item)
        }
    }

    suspend fun load() {
        loadLocalModel()
        val dietList = getContent()
        loadCandidateList(dietList)
        val dietTypes = getContentForDietTypes()
        loadTypesList(dietTypes)

    }

    private suspend fun getContentForDietTypes(): List<String> {
        val dietTypeList = mutableListOf<String>()
        val jsonFileString = getJsonDataFromAsset(context, Config().dietTypeListPath)
        val lines: Array<String> =
            jsonFileString?.split(System.lineSeparator())!!.toTypedArray()
        dietTypeList.addAll(lines)

        return dietTypeList
    }


    private suspend fun getContent(): List<DietItem> {
        val itemsOfDiet = mutableListOf<DietItem>()
        val jsonFileString = getJsonDataFromAsset(context, Config().movieListPath)

        val gson = Gson()
        val listPersonType = object : TypeToken<List<DietItem>>() {}.type

        itemsOfDiet.addAll(gson.fromJson(jsonFileString, listPersonType))

        return itemsOfDiet
    }

    private suspend fun loadTypesList(dietTypeList: List<String>) {
        return withContext(Dispatchers.IO) {
            dietTypes.clear()
            for (item in dietTypeList) {
                dietTypes[item] = dietTypes.size
            }
        }
    }

    private suspend fun loadLocalModel() {
        return withContext(Dispatchers.IO) {
            try {
                val buffer: ByteBuffer =
                    FileUtils.loadModelFile(
                        context.assets, config.modelPath
                    )
             initializeInterpreter(buffer)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }

    private suspend fun initializeInterpreter(model: Any) {
        return withContext(Dispatchers.IO) {
            tflite?.apply {
                close()
            }
            if (model is ByteBuffer) {
                tflite = Interpreter(model)
            } else {
                tflite = Interpreter(model as File)
            }
        }
    }

    private suspend fun loadCandidateList(dietList: List<DietItem>) {
        return withContext(Dispatchers.IO) {
            for (item in dietList) {
                candidates[item.id] = item
            }
            Log.v(TAG, "Candidate list loaded.")
        }
    }


    fun unload() {
        tflite?.close()
        candidates.clear()
    }


    @Synchronized
    private suspend fun preprocessIds(selectedMovies: List<DietItem>): IntArray {
        return withContext(Dispatchers.Default) {
            val inputContext = IntArray(config.inputLength)
            for (i in 0 until config.inputLength) {
                if (i < selectedMovies.size) {
                    val (id) = selectedMovies[i]
                    inputContext[i] = id
                } else {
                    // Padding input.
                    inputContext[i] = config.pad
                }
            }
            inputContext
        }
    }

    @Synchronized
    private suspend fun preprocessRatings(selectedMovies: List<DietItem>): FloatArray {
        return withContext(Dispatchers.Default) {
            val inputContext = FloatArray(config.inputLength)
            for (i in 0 until config.inputLength) {
                if (i < selectedMovies.size) {
                    val (_, _, _, rating) = selectedMovies[i]
                    if (rating != null) {
                        inputContext[i] = rating
                    }
                } else {
                    // Padding input.
                    inputContext[i] = config.pad.toFloat()
                }
            }
            inputContext
        }
    }

    @Synchronized
    private suspend fun preprocessDietTypes(selectedMovies: List<DietItem>): IntArray {
        return withContext(Dispatchers.Default) {

            // Fill inputGenres.
            val inputGenres = IntArray(10)
            Arrays.fill(inputGenres, config.pad) // Fill inputGenres with the default.

            val i = 0
            for (item in selectedMovies) {
                if (i >= inputGenres.size) {
                    break
                }
                inputGenres[i] =
                    (if (dietTypes.containsKey(item.dietType)) dietTypes[item.dietType] else config.pad)!!

            }
            inputGenres
        }
    }


    /** Postprocess to gets results from tflite inference.  */
    @Synchronized
    private suspend fun postprocess(
        outputIds: IntArray, confidences: FloatArray, selectedMovies: List<DietItem>?
    ): List<DietItem> {
        return withContext(Dispatchers.Default) {
            val results = ArrayList<DietItem>()

            // Add recommendation results. Filter null or contained items.
            for (i in outputIds.indices) {
                if (results.size >= config.topK) {
                    break
                }
                val id = outputIds[i]
                val item = candidates[id]
                if (item == null) {
                    continue
                }
                if (selectedMovies != null) {
                    if (selectedMovies.contains(item)) {
                        continue
                    }
                }
                val result = Result(
                    id,
                    item,
                    confidences[i]
                )
                results.add(item)
            }
            results
        }
    }

    @Synchronized
    suspend fun recommend(selectedMovies: List<DietItem>): List<DietItem> {
        return withContext(Dispatchers.Default) {
            val input: MutableList<Any> = ArrayList()
            input.add(preprocessIds(selectedMovies))
            input.add(preprocessRatings(selectedMovies))
            input.add(preprocessDietTypes(selectedMovies))

            val inputs = input.toTypedArray()
            val outputIds = IntArray(10)
            val confidences = FloatArray(10)
            val outputs: MutableMap<Int, Any> = HashMap()
            outputs[config.outputIdsIndex] = outputIds
            outputs[config.outputScoresIndex] = confidences

            tflite!!.runForMultipleInputsOutputs(inputs, outputs)

            postprocess(outputIds, confidences, selectedMovies)
        }
    }


    companion object {
        private const val TAG = "RecommendationClient"
    }
}