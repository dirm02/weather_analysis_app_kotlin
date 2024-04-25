package com.example.weatherapp

import javafx.application.Platform
import okhttp3.OkHttpClient
import okhttp3.Request

import javafx.fxml.FXML
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.json.JSONObject

class HelloController {
    @FXML
    lateinit var endDate: TextField

    @FXML
    lateinit var startDate: TextField

    @FXML
    lateinit var Location: TextField

    @FXML
    lateinit var tempChart: LineChart<String, Number>

    @FXML
    lateinit var humidityChart: LineChart<String, Number>

    @FXML
    lateinit var precipChart: LineChart<String, Number>

    @FXML
    lateinit var windChart: LineChart<String, Number>

    @FXML
    private fun onSubmitClick(event: MouseEvent) {
//        Location.text = "K2A1W1"
//        startDate.text = "2021-10-19T13:59:00"
//        endDate.text = "2021-12-19T13:59:00"

        var result = fetchWeatherData(Location.text, startDate.text, endDate.text)
        if (result != null) {
            plotWeatherData(result)
        }
    }

    fun fetchWeatherData(location: String, startDate: String, endDate: String?): JSONObject? {
        val apiKey = "3KAJKHWT3UEMRQWF2ABKVVVZE"
        val baseURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
        val url = when (endDate) {
            null -> "$baseURL$location/$startDate?key=$apiKey"
            else -> "$baseURL$location/$startDate/$endDate?key=$apiKey"
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        // Use the response
        response.use { res ->
            if (res.isSuccessful) {
                val responseBody = res.body?.string()
//                println("Response Body: $responseBody")
                return JSONObject(responseBody)
            } else {
                println("Failed : ${res.code}")
                return null
            }
        }

    }


    private fun plotWeatherData(data: JSONObject) {

        val days = data.getJSONArray("days")
        val series_temp = XYChart.Series<String, Number>()
        val series_humidity = XYChart.Series<String, Number>()
        val series_precip = XYChart.Series<String, Number>()
        val series_wind = XYChart.Series<String, Number>()

        for (i in 0 until days.length()) {
            val day = days.getJSONObject(i)
            val date = day.getString("datetime")
            val temp = day.getDouble("temp")
            val humidity = day.getDouble("humidity")
            val precip = day.getDouble("precip")
            val wind = day.getDouble("windspeed")

            series_temp.data.add(XYChart.Data(date, temp))
            series_humidity.data.add(XYChart.Data(date, humidity))
            series_precip.data.add(XYChart.Data(date, precip))
            series_wind.data.add(XYChart.Data(date, wind))

        }

        Platform.runLater {
            tempChart.data.clear()
            tempChart.data.add(series_temp)

            tempChart.lookupAll(".chart-line-symbol").forEach { node ->
                node.style = "-fx-background-color: transparent, transparent;"
            }
            (tempChart.xAxis as? CategoryAxis)?.isTickLabelsVisible = false
            tempChart.isLegendVisible = false

            //Humidity CHart
            humidityChart.data.clear()
            humidityChart.data.add(series_humidity)

            humidityChart.lookupAll(".chart-line-symbol").forEach { node ->
                node.style = "-fx-background-color: transparent, transparent;"
            }
            (humidityChart.xAxis as? CategoryAxis)?.isTickLabelsVisible = false
            humidityChart.isLegendVisible = false

            // Precip Chart
            precipChart.data.clear()
            precipChart.data.add(series_precip)

            precipChart.lookupAll(".chart-line-symbol").forEach { node ->
                node.style = "-fx-background-color: transparent, transparent;"
            }
            (precipChart.xAxis as? CategoryAxis)?.isTickLabelsVisible = false
            precipChart.isLegendVisible = false

            windChart.data.clear()
            windChart.data.add(series_wind)

            windChart.lookupAll(".chart-line-symbol").forEach { node ->
                node.style = "-fx-background-color: transparent, transparent;"
            }
            (windChart.xAxis as? CategoryAxis)?.isTickLabelsVisible = false
            windChart.isLegendVisible = false
        }
    }
}